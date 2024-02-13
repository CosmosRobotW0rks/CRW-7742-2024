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
        LOAD,
        IDLE
    }

    boolean current_was_high = false;

    Mode mode = Mode.LOAD;
    Command input;
    Command retract;
    Command shoot;

    Command action;
    boolean action_btn_pressed;

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
        input = new LoadingCommand(conveyor, shooter, intake, conf, req, j_conf);

        shoot = new ShootCommand(conveyor, shooter, conf, j_conf, 10000);
    }

    boolean ActionRunning() {
        if (action.isScheduled())
            return false;

        return true;
    }

    @Override
    public void periodic() {
        if (!ActionRunning()) { // SPHAGETTI!!!
            if (mode == Mode.LOAD) {
                if (!input.isScheduled())
                    input.schedule();
            } else if (mode == Mode.IDLE) {
                if (input.isScheduled())
                    input.cancel();
            }
        }

        if (!action_btn_pressed && req.GetButton(j_conf.ActionButton)) {
            if (mode == Mode.LOAD)
                action = retract;
            else if (mode == Mode.IDLE)
                action = shoot;
            action.schedule();
        }
        action_btn_pressed = req.GetButton(j_conf.ActionButton);

        if (req.GetDPad() == 270)
            mode = Mode.LOAD;
        else if (req.GetDPad() == 90)
            mode = Mode.IDLE;

        SmartDashboard.putString("Shooter mode", mode.toString());
    }
}
