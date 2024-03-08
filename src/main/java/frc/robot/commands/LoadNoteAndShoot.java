package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.apriltag.FieldPOIs;
import frc.robot.apriltag.NotePOIs;

public class LoadNoteAndShoot extends SequentialCommandGroup{
    public LoadNoteAndShoot(RobotContainer c, NotePOIs n, Pose2d speaker, double rpm, double deg){
        Command load_note = new LoadNote(c, n);
        Command shoot = new GoAndShoot(c, rpm, deg, speaker);
        addCommands(load_note, shoot);
    }
}
