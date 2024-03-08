package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotConfiguration;
import frc.robot.RobotContainer;
import frc.robot.apriltag.FieldPOIs;
import frc.robot.apriltag.SetConstantLocalization;
import frc.robot.commands.SquareUp.Position;
import frc.robot.drive.LocalizeWaypoint;
import frc.robot.drive.WaypointDriveCommand;
import frc.robot.note_manipulation.NoteSystemController;
import frc.robot.note_manipulation.SetMode;
import frc.robot.note_manipulation.Shoot;

public class GoAndShoot extends SequentialCommandGroup {
    public GoAndShoot(RobotContainer c, double rpm, double deg, Pose2d speaker_loc) {
        Command enable_localization = new SetConstantLocalization(c.apriltag, true);
        Command setShootMode = new SetMode(c.note_c, NoteSystemController.Mode.WAIT, rpm, deg);
        Command square_up = new WaypointDriveCommand(c, speaker_loc, false, 2, 0.2, 1.5);
        Command shoot = new Shoot(c.note_c, rpm, deg);
        Command square_up_again = new SquareUp_Speaker(c, Position.MIDDLE_OR_FRONT);
        Command disable_localization = new SetConstantLocalization(c.apriltag, false);

        addCommands(enable_localization, setShootMode, square_up,
                shoot.alongWith(new WaitCommand(0.2).andThen(square_up_again)), disable_localization);
    }
}
