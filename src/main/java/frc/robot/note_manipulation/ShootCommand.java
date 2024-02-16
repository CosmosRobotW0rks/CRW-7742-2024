package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.control.JoystickConfiguration;
import frc.robot.note_manipulation.shooter.Conveyor;
import frc.robot.note_manipulation.shooter.ConveyorSetPower;
import frc.robot.note_manipulation.shooter.Shooter;
import frc.robot.note_manipulation.shooter.ShooterSetRPM;
import frc.robot.note_manipulation.shooter.ShooterSetRPM.HoldMode;

public class ShootCommand extends SequentialCommandGroup {
    public ShootCommand(Conveyor conveyor, Shooter shooter, NoteSystemConfiguration conf, JoystickConfiguration j_conf,
            double rpm) {
        Command shooter_rpm = new ShooterSetRPM(shooter, rpm, HoldMode.RPMTarget).withTimeout(2);
        Command shooter_rpm_wait = new ShooterSetRPM(shooter, rpm, HoldMode.Indefinite).withTimeout(conf.PreShootHoldTime);
        Command shooter_rpm_hold = new ShooterSetRPM(shooter, rpm, HoldMode.Indefinite).withTimeout(2);
        Command conveyor_retract = new ConveyorSetPower(conveyor, conf.ShootConveyorReversePower, true)
                .withTimeout(conf.ShootConveyorReverseTime);
        Command conveyor_push = new ConveyorSetPower(conveyor, conf.ShootConveyorPower, true)
                .withTimeout(conf.ShootConveyorTime);
        Command shooter_stop = new ShooterSetRPM(shooter, 0, HoldMode.Indefinite);

        Command spin_up = shooter_rpm.alongWith(conveyor_retract).andThen(shooter_rpm_wait);
        Command push_end = conveyor_push.andThen(shooter_rpm_hold).withTimeout(conf.PostShootHoldTime)
                .andThen(shooter_stop).withTimeout(0.5);

        addCommands(spin_up, push_end);
    }

    @Override
    public InterruptionBehavior getInterruptionBehavior() {
        return InterruptionBehavior.kCancelIncoming;
    }
}
