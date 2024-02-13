package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.control.JoystickConfiguration;
import frc.robot.control.JoystickRequester;
import frc.robot.note_manipulation.intake.Intake;
import frc.robot.note_manipulation.shooter.Conveyor;
import frc.robot.note_manipulation.shooter.ConveyorSetPower;
import frc.robot.note_manipulation.shooter.Hinge;
import frc.robot.note_manipulation.shooter.Shooter;

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
        WAIT
    }

    boolean current_was_high = false;

    Mode mode = Mode.LOAD;
    Command input;
    Command retract;
    Command shoot;

    Command action;
    Command default_cmd;

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

        addChild("Conveyor", conveyor);
        addChild("Shooter", shooter);
        addChild("Hinge", hinge);
    }

    boolean is_busy() {
        if (action.isScheduled())
            return false;

        return true;
    }

    public void Shoot() {
        if (is_busy())
            return;

        action = shoot;
        action.schedule();
    }

    public void Retract() {
        if (is_busy())
            return;

        action = retract;
        action.schedule();
    }

    void do_action() {
        if (mode == Mode.LOAD)
            Retract();
        else if (mode == Mode.WAIT)
            Shoot();
    }

    Command get_default_cmd() {
        if (mode == Mode.LOAD)
            return input;

        return null;
    }

    void schedule_default_command() {
        Command desired_command = get_default_cmd();
        if (default_cmd != desired_command)
            default_cmd.cancel();

        if (!desired_command.isScheduled()) {
            desired_command.schedule();
            default_cmd = desired_command;
        }
    }

    @Override
    public void periodic() {
        if (!is_busy())
            schedule_default_command();
        else if (default_cmd.isScheduled())
            default_cmd.cancel();

        if (!action_btn_pressed && req.GetButton(j_conf.ActionButton))
            do_action();
        action_btn_pressed = req.GetButton(j_conf.ActionButton);

        if (req.GetDPad() == 270)
            mode = Mode.LOAD;
        else if (req.GetDPad() == 90)
            mode = Mode.WAIT;

        SmartDashboard.putString("Shooter mode", mode.toString());
    }
}
