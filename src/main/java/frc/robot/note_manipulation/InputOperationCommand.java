package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.control.JoystickConfiguration;
import frc.robot.control.JoystickRequester;
import frc.robot.note_manipulation.intake.Intake;
import frc.robot.note_manipulation.intake.IntakeJoystickControl;
import frc.robot.note_manipulation.shooter.ConveyorJoystickControl;
import frc.robot.note_manipulation.shooter.Shooter;
import frc.robot.note_manipulation.shooter.Conveyor;
import frc.robot.note_manipulation.shooter.ShooterSetRPM;
import frc.robot.note_manipulation.shooter.ShooterSetRPM.HoldMode;

public class InputOperationCommand extends ParallelCommandGroup {
    public InputOperationCommand(Conveyor conveyor, Shooter shooter, Intake intake, NoteSystemConfiguration conf, JoystickRequester req, JoystickConfiguration j_conf) {
        ConveyorJoystickControl conveyor_j = new ConveyorJoystickControl(conveyor, j_conf.IntakeOperationThrottle, req);
        conveyor_j.min_power = conf.ConveyorMinPower;
        conveyor_j.max_power = conf.ConveyorMaxPower;

        IntakeJoystickControl intake_j = new IntakeJoystickControl(intake, j_conf.IntakeOperationThrottle, req);
        intake_j.min_power = conf.IntakeMinPower;
        intake_j.max_power = conf.IntakeMaxPower;

        ShooterSetRPM shooter_rpm = new ShooterSetRPM(shooter, conf.InputOperationShooterSpeed, HoldMode.Indefinite);
        addCommands(conveyor_j, intake_j, shooter_rpm);
    }
}