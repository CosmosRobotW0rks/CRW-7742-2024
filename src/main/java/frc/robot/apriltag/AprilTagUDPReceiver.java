package frc.robot.apriltag;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Map;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.RollingAverage;
import frc.lib.UDPReceiver;
import frc.robot.drivetrain.SwerveDrivetrain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AprilTagUDPReceiver extends SubsystemBase {
    private UDPReceiver receiver;
    private ObjectMapper mapper;
    private Transform2d odometry_offset;

    public RollingAverage x_damp = new RollingAverage(0.8);
    public RollingAverage y_damp = new RollingAverage(0.8);

    private double odometry_update_time;

    private SwerveDrivetrain drivetrain;

    public ArrayList<TagData> tags = new ArrayList<>();

    public AprilTagUDPReceiver(SwerveDrivetrain drivetrain) {
        mapper = new ObjectMapper();
        this.drivetrain = drivetrain;
        try {
            receiver = new UDPReceiver(5801, 10, 0);
        } catch (SocketException e) {
            System.err.println("Can't bind to receiver socket!");
            return;
        }
    }

    public TagData GetTag(int id) {
        for (TagData data : tags) {
            if (data.id == id)
                return data;
        }

        return null;
    }

    public Transform3d GetTagTransform(int id) {
        TagData tag = GetTag(id);

        if (tag == null)
            return null;

        Translation3d translation = new Translation3d(tag.relative[2], tag.relative[0], tag.relative[1]);
        Rotation3d rotation = new Rotation3d(tag.euler[0], tag.euler[1], tag.euler[2]);

        return new Transform3d(translation, rotation);
    }

    public Pose3d LocalizeRobot() {
        int id = -1;

        if (tags.size() > 0)
            id = tags.get(0).id;

        Pose3d tag_relative = GetPoseTagRelative(id);
        Pose3d tag_pose = FieldTags.aprilTags.get(id);

        if (tag_relative == null || tag_pose == null)
            return null;

        Transform3d field2tag = new Transform3d(new Pose3d(), tag_pose).inverse();

        return tag_relative.plus(field2tag);
    }

    void update_odometry() {
        Pose2d odom_pose = drivetrain.OdometryOutPose;
        Pose3d pose = LocalizeRobot();

        if (pose == null)
            return;

        Pose2d robot_actual_pose = pose.toPose2d();

        if (robot_actual_pose == null)
            return;

        odometry_offset = new Transform2d(odom_pose, robot_actual_pose);

        // Prevent rotation from updating, NavX is more accurate than AprilTags
        // odometry_offset = new Transform2d(odometry_offset.getTranslation(), new
        // Rotation2d());
        odometry_update_time = Timer.getFPGATimestamp();

        x_damp.AddSample(odometry_offset.getX());
        y_damp.AddSample(odometry_offset.getY());

        drivetrain.OdometryOffset = new Transform2d(new Translation2d(x_damp.GetAverage(), y_damp.GetAverage()), odometry_offset.getRotation());
    }

    public Transform2d GetOdometryOffset() {
        return odometry_offset;
    }

    public Pose3d GetPoseTagRelative(int id) {
        TagData tag = GetTag(id);

        if (tag == null)
            return null;

        Translation3d translation = new Translation3d(tag.relative[2], tag.relative[0], tag.relative[1]);
        Rotation3d rotation = new Rotation3d(tag.euler[0], tag.euler[1], tag.euler[2]);

        return new Pose3d(translation, rotation);
    }

    void parse(String data) {
        try {
            if (data == "")
                return;

            tags = mapper.readValue(data, new TypeReference<ArrayList<TagData>>() {
            });
        } catch (Exception e) {
            // System.err.println("Error while parsing received data!");
        }
    }

    @Override
    public void periodic() {
        parse(receiver.GetLastData());
        update_odometry();
    }
}
