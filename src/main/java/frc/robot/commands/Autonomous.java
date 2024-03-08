package frc.robot.commands;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.apriltag.FieldPOIs;
import frc.robot.apriltag.SetConstantLocalization;
import frc.robot.note_manipulation.Shoot;

public class Autonomous extends SequentialCommandGroup {
    public static Command NoteLeft(RobotContainer c) {
        return new LoadNoteAndShoot(c, FieldPOIs.NoteLeft, FieldPOIs.Speaker_left, 3800, 0);
    }

    public static Command NoteRight(RobotContainer c) {
        return new LoadNoteAndShoot(c, FieldPOIs.NoteRight, FieldPOIs.Speaker_left, 3800, 0);
    }

    public static Command NoteCenter(RobotContainer c) {
        return new LoadNoteAndShoot(c, FieldPOIs.NoteCenter, FieldPOIs.Speaker_left, 3800, 0);
    }

    public static Command NoteCenterline(RobotContainer c) {
        Command load = new LoadNote(c, FieldPOIs.NoteCenterline5, FieldPOIs.NotePreCenterline5);
        Command shoot = new GoAndShoot(c, 3800, 0, FieldPOIs.Speaker_left);
        return new SetConstantLocalization(c.apriltag, false).andThen(load).andThen(shoot).andThen(new SetConstantLocalization(c.apriltag, true));
    }

    public static Command NoteSelf(RobotContainer c){
        return new AggregateAprilTagLocalize(c.apriltag, c.drivetrain).withTimeout(2)
				.alongWith(new Shoot(c.note_c, 3500, 0));
    }
}
