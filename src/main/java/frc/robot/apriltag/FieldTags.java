package frc.robot.apriltag;

import java.util.Map;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;

public class FieldTags {
    public static final Map<Integer, Pose3d> aprilTags = Map.of(
            1,
            new Pose3d(
                    0, 0, 0,
                    new Rotation3d(0.0, 0.0, Math.PI)));
}
