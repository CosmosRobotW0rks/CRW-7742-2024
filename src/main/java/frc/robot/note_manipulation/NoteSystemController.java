package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.control.JoystickConfiguration;
import frc.robot.control.JoystickRequester;
import frc.robot.note_manipulation.intake.Intake;
import frc.robot.note_manipulation.shooter.ConveyorSetPower;
import frc.robot.note_manipulation.shooter.Hinge;
import frc.robot.note_manipulation.shooter.HingeJoystickControl;
import frc.robot.note_manipulation.shooter.Shooter;
import frc.robot.note_manipulation.shooter.Conveyor;

public class NoteSystemController extends SubsystemBase {
    private Intake intake;
    private Conveyor conveyor;
    private Shooter shooter;
    private Hinge hinge;

    private JoystickRequester req;
    private JoystickConfiguration j_conf;
    private NoteSystemConfiguration conf;

    private enum Mode {
        INPUT,
        SHOOT
    }

    boolean current_was_high = false;

    Mode mode = Mode.INPUT;
    Command input;
    Command retract;
    Command shoot;

    Command running_command;
    HingeJoystickControl hinge_j;
    boolean mode_btn_pressed;

    public void Init(RobotContainer container, JoystickConfiguration j_conf, NoteSystemConfiguration conf) {
        intake = container.intake;
        conveyor = container.conveyor;
        shooter = container.shooter;
        hinge = container.hinge;

        req = new JoystickRequester(container.main);
        this.j_conf = j_conf;
        this.conf = conf;

        retract = new ConveyorSetPower(conveyor, conf.ConveyorReversePower, true)
                .withTimeout(conf.ConveyorReverseTime)
                .andThen(new ConveyorSetPower(conveyor, conf.ConveyorPushbackPower, true)
                        .withTimeout(conf.ConveyorPushbackTime));
        input = new InputOperationCommand(conveyor, shooter, intake, conf, req, j_conf);

        shoot = new ShootCommand(conveyor, shooter, conf, j_conf, 10000);
        hinge_j = new HingeJoystickControl(hinge, j_conf.HingeManualControlAxis, req);

        hinge_j.min_power = conf.HingeMinPower;
        hinge_j.max_power = conf.HingeMaxPower;
    }

    @Override
    public void periodic() {
        if (!running_command.isScheduled()) {
            if (mode == Mode.INPUT) {
                if (hinge_j.isScheduled())
                    hinge_j.cancel();

                if (!input.isScheduled())
                    input.schedule();

                if (conveyor.GetCurrent() > 4.2)
                    current_was_high = true;
                else if (current_was_high && conveyor.GetCurrent() < 3) {
                    current_was_high = false;
                    mode = Mode.SHOOT;
                }
            } else if (mode == Mode.SHOOT) {
                if (input.isScheduled())
                    input.cancel();

                if (!hinge_j.isScheduled())
                    hinge_j.schedule();
            }
        }

        if (req.GetButton(j_conf.ModeSwitchButton) && !mode_btn_pressed) {
            if (mode == Mode.SHOOT)
                running_command = shoot;
            else if (mode == Mode.INPUT)
                running_command = retract;
            running_command.schedule();
        }

        mode_btn_pressed = req.GetButton(j_conf.ModeSwitchButton);

        if (req.GetDPad() == 270)
            mode = Mode.INPUT;
        else if (req.GetDPad() == 90)
            mode = Mode.SHOOT;

        if (mode == Mode.INPUT)
            SmartDashboard.putString("Shooter mode", "INPUT");
        else if (mode == Mode.SHOOT)
            SmartDashboard.putString("Shooter mode", "SHOOT");
    }
}
