package frc.robot.apriltag;

import edu.wpi.first.math.geometry.Pose2d;

public class SourcePOIs {
    public Pose2d near;
    public Pose2d middle;
    public Pose2d far;

    public SourcePOIs(Pose2d near, Pose2d middle, Pose2d far){
        this.near = near;
        this.middle = middle;
        this.far = far;
    }
}
