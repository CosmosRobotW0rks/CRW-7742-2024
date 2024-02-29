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
import frc.robot.note_manipulation.NoteSystemController;
import frc.robot.note_manipulation.SetMode;
import frc.robot.note_manipulation.Shoot;

public class GoAndShoot extends SequentialCommandGroup {
    public GoAndShoot(RobotContainer c, double rpm) {
        Command setShootMode = new SetMode(c.note_c, NoteSystemController.Mode.WAIT);
        Command squareUp = new SquareUp_Speaker(c);
        Command shoot = new Shoot(c.note_c);

        addCommands(setShootMode, squareUp, shoot);
    }
}
