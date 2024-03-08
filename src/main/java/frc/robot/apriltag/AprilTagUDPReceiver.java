package frc.robot.apriltag;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
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
import frc.lib.ValueDelay;
import frc.robot.apriltag.FieldTags.Tag;
import frc.robot.drivetrain.SwerveDrivetrain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AprilTagUDPReceiver extends SubsystemBase {
    private UDPReceiver receiver;
    private ObjectMapper mapper;
    private Transform2d odometry_offset;

    public RollingAverage x_avg = new RollingAverage(0.5);
    public RollingAverage y_avg = new RollingAverage(0.5);

    // public ValueDelay x_delay = new ValueDelay(0.75);
    // public ValueDelay y_delay = new ValueDelay(0.75);
    // public ValueDelay th_delay = new ValueDelay(0.75);

    private double odometry_update_time;

    private SwerveDrivetrain drivetrain;

    public ArrayList<TagData> tags = new ArrayList<>();
    public HashMap<Integer, TagData> tagsByID = new HashMap<>();

    FieldTags field_tags = new FieldTags();

    private Transform3d cam2robot = new Transform3d(new Translation3d(0, -0.35, 0), new Rotation3d());

    private boolean constant = false;

    public AprilTagUDPReceiver(SwerveDrivetrain drivetrain) {
        mapper = new ObjectMapper();
        this.drivetrain = drivetrain;
        try {
            receiver = new UDPReceiver(5801, 100, 1);
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

    public ArrayList<Integer> GetDetectedIDs() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (TagData data : tags)
            ids.add(data.id);

        return ids;
    }

    public Transform3d GetTagTransform(int id) {
        TagData tag = GetTag(id);

        if (tag == null)
            return null;

        Translation3d translation = new Translation3d(tag.relative[2], -tag.relative[0], tag.relative[1]);
        Rotation3d rotation = new Rotation3d(tag.euler[0], tag.euler[1], tag.euler[2]);

        return new Transform3d(translation, rotation);
    }

    public Pose3d GetRobotPose() {
        if (tags.size() == 0)
            return null;

        double x_avg = 0;
        double y_avg = 0;

        for (TagData data : tags) {
            int id = data.id;

            Pose3d tag_relative = GetPoseTagRelative(id);
            Tag tag_pose = field_tags.Get(id);

            if (tag_relative == null || tag_pose == null)
                return null;

            Rotation3d rot = new Rotation3d(0, 0, drivetrain.gyroAngle.getRadians());

            Translation2d absolute_tag_pose = tag_relative.rotateBy(rot).toPose2d().getTranslation();

            Translation2d field2tag = tag_pose.pose.toPose2d().getTranslation();

            Pose2d real_pose = new Pose2d(absolute_tag_pose.plus(field2tag), new Rotation2d());

            x_avg += real_pose.getX();
            y_avg += real_pose.getY();
        }

        x_avg /= tags.size();
        y_avg /= tags.size();

        return new Pose3d(x_avg, y_avg, 0, new Rotation3d());
    }

    public void LocalizeRobot() {
        Pose3d pose = GetRobotPose();

        if (pose == null)
            return;

        Pose2d robot_actual_pose = pose.toPose2d();
        SmartDashboard.putString("Localized pose",
                "x: " + robot_actual_pose.getX() + ", y: " + robot_actual_pose.getY());

        if (Double.isNaN(robot_actual_pose.getX()) || Double.isNaN(robot_actual_pose.getY())) {
            System.out.println("Cannot localize! Pose value is NaN!");
            return;
        }

        drivetrain.SetOdomPose(robot_actual_pose);
        odometry_update_time = Timer.getFPGATimestamp();
    }

    public void SetConstant(boolean constant) {
        this.constant = constant;
    }

    public Transform2d GetOdometryOffset() {
        return odometry_offset;
    }

    public Pose3d GetPoseTagRelative(int id) {
        TagData tag = GetTag(id);

        if (tag == null)
            return null;

        Translation3d translation = new Translation3d(tag.relative[2], -tag.relative[0], tag.relative[1]);
        Rotation3d rotation = new Rotation3d(tag.euler[0], tag.euler[1], tag.euler[2]);

        return new Pose3d(translation, rotation).plus(cam2robot);
    }

    public boolean Connected() {
        return !receiver.HasTimedOut();
    }

    void parse(String data) {
        try {
            tags = new ArrayList<>();
            if (receiver.HasTimedOut())
                return;
            if (data == "")
                return;

            tags = mapper.readValue(data, new TypeReference<ArrayList<TagData>>() {

            });

            String message = "";
            for (TagData tag : tags) {
                tagsByID.put(tag.id, tag);
                message += tag.id + ", ";
            }

            SmartDashboard.putString("Found tags", message);

        } catch (Exception e) {
            // System.err.println("Error while parsing received data!");
        }
    }

    /*
     * void add_pose_delay() {
     * x_delay.AddSample(drivetrain.OdometryOutPose.getX());
     * y_delay.AddSample(drivetrain.OdometryOutPose.getY());
     * th_delay.AddSample(drivetrain.OdometryOutPose.getRotation().getRadians());
     * 
     * }
     */

    @Override
    public void periodic() {
        if (constant)
            LocalizeRobot();

        SmartDashboard.putBoolean("Using red alliance tags", field_tags.IsRedAlliance);
        SmartDashboard.putBoolean("Constant localization", constant);

        parse(receiver.GetLastData());
    }
}
