package frc.robot.apriltag;

import java.util.Map;
import java.util.Optional;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class FieldTags {
    public class Tag {
        public Pose3d pose;
        public int priority;

        public Tag(double x, double y, int priority) {
            this.pose = new Pose3d(x, y, 0, new Rotation3d());
            this.priority = priority;
        }
    }

    public Map<Integer, Tag> aprilTags;

    public Map<Integer, Tag> aprilTagsRed = Map.of(
            3,
            new Tag(0, 0.56, 1),

            4,
            new Tag(0, 0, 0),

            10,
            new Tag(15.118, 5.302, 0),
            9,
            new Tag(16.223, 4.664, 0));

    public Map<Integer, Tag> aprilTagsBlue = Map.of(
            8,
            new Tag(0, -0.56, 1),

            7,
            new Tag(0, 0, 0));

    public boolean IsRedAlliance;

    public FieldTags() {
        Optional<Alliance> alliance = DriverStation.getAlliance();
        if (!alliance.isPresent() || alliance.get() != Alliance.Blue) {
            System.out.println("Setting tags for red alliance.");
            aprilTags = aprilTagsRed;
            IsRedAlliance = true;
        } else {
            System.out.println("Setting tags for blue alliance.");
            aprilTags = aprilTagsRed;
            IsRedAlliance = false;
        }
    }

    public Tag Get(int id) {
        return aprilTags.get(id);
    }
}
