package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotConfiguration;
import frc.robot.RobotContainer;
import frc.robot.drive.LocalizeWaypoint;
import frc.robot.drive.WaypointDriveCommand;

public class SquareUp_Speaker extends SequentialCommandGroup {
    public SquareUp_Speaker(RobotContainer c) {
        Command go_to_pre = new WaypointDriveCommand(c, new Pose2d(new Translation2d(0.5, 0), new Rotation2d()), false,
                RobotConfiguration.CoarseXSpeed, RobotConfiguration.CoarseYSpeed,
                RobotConfiguration.CoarseLocalizationPrecision, 2);
        Command go_to = new LocalizeWaypoint(c, new Pose2d());

        addCommands(go_to_pre, go_to);
    }
}
