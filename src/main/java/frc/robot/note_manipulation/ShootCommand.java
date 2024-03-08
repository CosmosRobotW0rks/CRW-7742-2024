package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.control.JoystickConfiguration;
import frc.robot.note_manipulation.shooter.Conveyor;
import frc.robot.note_manipulation.shooter.ConveyorSetPower;
import frc.robot.note_manipulation.shooter.Hinge;
import frc.robot.note_manipulation.shooter.HingeGoto;
import frc.robot.note_manipulation.shooter.Shooter;
import frc.robot.note_manipulation.shooter.ShooterSetRPM;
import frc.robot.note_manipulation.shooter.ShooterSetRPM.HoldMode;

public class ShootCommand extends SequentialCommandGroup {
        ShooterSetRPM shooter_rpm;
        ShooterSetRPM shooter_rpm_wait;
        ShooterSetRPM shooter_rpm_hold;

        public ShootCommand(Conveyor conveyor, Shooter shooter, Hinge hinge, NoteSystemConfiguration conf,
                        double rpm, double angle) {
                shooter_rpm = new ShooterSetRPM(shooter, rpm, HoldMode.RPMTarget);
                shooter_rpm_wait = new ShooterSetRPM(shooter, rpm, HoldMode.Indefinite);
                shooter_rpm_hold = new ShooterSetRPM(shooter, rpm, HoldMode.Indefinite);

                Command shooter_rpm_g = shooter_rpm.withTimeout(1);
                Command shooter_rpm_wait_g = shooter_rpm_wait
                                .withTimeout(conf.PreShootHoldTime);
                Command shooter_rpm_hold_g = shooter_rpm_hold.withTimeout(1);
                Command conveyor_retract = new ConveyorSetPower(conveyor, conf.ShootConveyorReversePower, true)
                                .withTimeout(conf.ShootConveyorReverseTime);
                Command conveyor_push = new ConveyorSetPower(conveyor, conf.ShootConveyorPower, true)
                                .withTimeout(conf.ShootConveyorTime);

                Command conveyor_retract_again = new ConveyorSetPower(conveyor, conf.ShootConveyorReversePower, true)
                                .withTimeout(conf.ShootConveyorReverseTime);
                Command conveyor_push_again = new ConveyorSetPower(conveyor, conf.ShootConveyorPower, true)
                                .withTimeout(conf.ShootConveyorTime);

                Command shooter_stop = new ShooterSetRPM(shooter, 0, HoldMode.Indefinite);

                Command hinge_goto = new HingeGoto(hinge, angle);

                Command spin_up = hinge_goto.alongWith(shooter_rpm_g).alongWith(conveyor_retract)
                                .andThen(shooter_rpm_wait_g);
                Command push_end = conveyor_push.andThen(shooter_rpm_hold_g).withTimeout(conf.PostShootHoldTime)
                                .andThen(shooter_stop).withTimeout(0.5);

                addCommands(spin_up, push_end);
        }

        public ShootCommand(Conveyor conveyor, Shooter shooter, Hinge hinge, NoteSystemConfiguration conf,
                        double rpm) {
                shooter_rpm = new ShooterSetRPM(shooter, rpm, HoldMode.RPMTarget);
                shooter_rpm_wait = new ShooterSetRPM(shooter, rpm, HoldMode.Indefinite);
                shooter_rpm_hold = new ShooterSetRPM(shooter, rpm, HoldMode.Indefinite);

                Command shooter_rpm_g = shooter_rpm.withTimeout(2);
                Command shooter_rpm_wait_g = shooter_rpm_wait
                                .withTimeout(conf.PreShootHoldTime);
                Command shooter_rpm_hold_g = shooter_rpm_hold.withTimeout(2);

                Command conveyor_retract = new ConveyorSetPower(conveyor, conf.ShootConveyorReversePower, true)
                                .withTimeout(conf.ShootConveyorReverseTime);
                Command conveyor_push = new ConveyorSetPower(conveyor, conf.ShootConveyorPower, true)
                                .withTimeout(conf.ShootConveyorTime);
                Command conveyor_retract_again = new ConveyorSetPower(conveyor, conf.ShootConveyorReversePower, true)
                                .withTimeout(conf.ShootConveyorReverseTime);
                Command conveyor_push_again = new ConveyorSetPower(conveyor, conf.ShootConveyorPower, true)
                                .withTimeout(conf.ShootConveyorTime);
                Command shooter_stop = new ShooterSetRPM(shooter, 0, HoldMode.Indefinite);

                Command spin_up = shooter_rpm_g.alongWith(conveyor_retract)
                                .andThen(shooter_rpm_wait_g);
                Command push_end = conveyor_push.andThen(shooter_rpm_hold_g).withTimeout(conf.PostShootHoldTime)
                                .andThen(shooter_stop).withTimeout(0.5);

                addCommands(spin_up, push_end);
        }

        @Override
        public InterruptionBehavior getInterruptionBehavior() {
                return InterruptionBehavior.kCancelIncoming;
        }

        public void SetRPM(double rpm) {
                shooter_rpm.SetRPM(rpm);
                shooter_rpm_wait.SetRPM(rpm);
                shooter_rpm_hold.SetRPM(rpm);
        }
}
