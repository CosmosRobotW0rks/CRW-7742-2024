package frc.robot.apriltag;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;

public class FieldPOIs {
        public static Pose2d Speaker_front = new Pose2d(1.88, 0, Rotation2d.fromDegrees(0));
        public static Pose2d Speaker_front_far = new Pose2d(2.55, 0, Rotation2d.fromDegrees(0));

        public static Pose2d Speaker_left = new Pose2d(1.23, 1.6, Rotation2d.fromDegrees(50));
        public static Pose2d Speaker_right = new Pose2d(1.23, -1.6, Rotation2d.fromDegrees(-50));

        public static SourcePOIs RedSource = new SourcePOIs(new Pose2d(14.7, 4.5, Rotation2d.fromDegrees(-60)),
                        new Pose2d(15.2, 4.1, Rotation2d.fromDegrees(-60)),
                        new Pose2d(15.7, 3.9, Rotation2d.fromDegrees(-60)));

        public static SourcePOIs BlueSource = new SourcePOIs(new Pose2d(14.7, -4.5, Rotation2d.fromDegrees(-60)),
                        new Pose2d(15.2, -4.1, Rotation2d.fromDegrees(-60)),
                        new Pose2d(15.7, -3.9, Rotation2d.fromDegrees(-60)));

        public static NotePOIs NoteLeft = new NotePOIs(new Pose2d(3.2, 1.15, Rotation2d.fromDegrees(45)),
                        new Pose2d(3.9, 2, Rotation2d.fromDegrees(45)));
        public static NotePOIs NoteCenter = new NotePOIs(new Pose2d(3.2, 0, Rotation2d.fromDegrees(0)),
                        new Pose2d(3.9, 0, Rotation2d.fromDegrees(0)));
        public static NotePOIs NoteRight = new NotePOIs(new Pose2d(3.2, -1.15, Rotation2d.fromDegrees(-45)),
                        new Pose2d(3.9, -2, Rotation2d.fromDegrees(-45)));

        public static NotePOIs NoteCenterline1 = new NotePOIs(new Pose2d(8.3, 1.97, Rotation2d.fromDegrees(0)),
                        new Pose2d(9.4, 1.97, Rotation2d.fromDegrees(0)));

        public static Pose2d NotePreCenterline5 = new Pose2d(2, 3.795, Rotation2d.fromDegrees(0));
        public static NotePOIs NoteCenterline5 = new NotePOIs(new Pose2d(8.3, 4.795, Rotation2d.fromDegrees(0)),
                        new Pose2d(9.4, 4.795, Rotation2d.fromDegrees(0)));

}
