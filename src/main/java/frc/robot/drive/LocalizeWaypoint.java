package frc.robot.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotConfiguration;
import frc.robot.RobotContainer;

public class LocalizeWaypoint extends SequentialCommandGroup {
    public LocalizeWaypoint(RobotContainer c, Pose2d target) {
        Command coarse = new WaypointDriveCommand(c, target, false, RobotConfiguration.CoarseXSpeed, RobotConfiguration.CoarseYSpeed,
                RobotConfiguration.CoarseLocalizationPrecision, 0.75);

        Command fine = new WaypointDriveCommand(c, target, false, RobotConfiguration.FineTransSpeed,
                RobotConfiguration.FineLocalizationPrecision, 3);

        addCommands(coarse, fine);
    }
}
