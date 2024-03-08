package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.note_manipulation.shooter.Conveyor;
import frc.robot.note_manipulation.shooter.ConveyorSetPower;
import frc.robot.note_manipulation.shooter.Hinge;
import frc.robot.note_manipulation.shooter.HingeGoto;
import frc.robot.note_manipulation.shooter.Shooter;
import frc.robot.note_manipulation.shooter.ShooterSetRPM;
import frc.robot.note_manipulation.shooter.ShooterSetRPM.HoldMode;

public class AmpDrop extends SequentialCommandGroup {
    public AmpDrop(Hinge hinge, Shooter shooter, Conveyor conveyor, NoteSystemConfiguration conf){
        Command hinge_goto = new HingeGoto(hinge, 15 * (Math.PI / 180));
        Command shoot = new ShootCommand(conveyor, shooter, hinge, conf, 1000);

        addCommands(hinge_goto, shoot);
    }
}
