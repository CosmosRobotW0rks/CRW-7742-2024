package frc.robot.apriltag;

import edu.wpi.first.math.geometry.Pose2d;

public class NotePOIs {
    public Pose2d LoadPre;
    public Pose2d LoadPost;

    public NotePOIs(Pose2d pre, Pose2d post) {
        LoadPre = pre;
        LoadPost = post;
    }
}