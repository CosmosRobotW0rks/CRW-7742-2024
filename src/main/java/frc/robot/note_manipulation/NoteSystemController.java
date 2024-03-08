package frc.robot.note_manipulation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.control.JoystickConfiguration;
import frc.robot.control.JoystickProvider;
import frc.robot.control.JoystickRequester;
import frc.robot.note_manipulation.intake.Intake;
import frc.robot.note_manipulation.shooter.Conveyor;
import frc.robot.note_manipulation.shooter.ConveyorSetPower;
import frc.robot.note_manipulation.shooter.Hinge;
import frc.robot.note_manipulation.shooter.HingeGoto;
import frc.robot.note_manipulation.shooter.Shooter;
import frc.robot.note_manipulation.shooter.ShooterSetRPM;
import frc.robot.note_manipulation.shooter.ShooterSetRPM.HoldMode;

public class NoteSystemController extends SubsystemBase {
    private static final double DEG2RAD = (Math.PI / 180);

    private Intake intake;
    private Conveyor conveyor;
    private Shooter shooter;
    private Hinge hinge;

    private JoystickRequester req;
    private JoystickConfiguration j_conf;
    private NoteSystemConfiguration conf;

    public enum Mode {
        MANUAL,
        LOAD_FROM_HP,
        LOAD,
        WAIT,
        AMP
    }

    boolean detect_note = true;
    boolean auto = false;
    boolean reset_on_shoot = true;

    Mode mode = Mode.MANUAL;
    Command load;
    Command load_hp;
    Command retract;
    ShootCommand shoot;
    Command shoot_amp;
    Command amp_mode;
    ShooterSetRPM spin_up;

    Command stop;

    HingeGoto hinge_motion;

    ArrayList<Command> cmd_queue = new ArrayList<>();

    Command action;
    Command default_cmd;

    boolean action_btn_pressed;
    boolean has_stalled = false;

    double hinge_target = 0;

    int prev_dpad = 0;

    double time_since_last_cmd;

    public void Init(RobotContainer container, NoteSystemConfiguration conf, JoystickProvider p,
            JoystickConfiguration j_conf) {
        intake = container.intake;
        conveyor = container.conveyor;
        shooter = container.shooter;
        hinge = container.hinge;

        this.conf = conf;
        this.j_conf = j_conf;
        this.req = new JoystickRequester(p);

        retract = new ConveyorSetPower(conveyor, conf.ConveyorFeedforwardTime, true)
                .withTimeout(conf.ConveyorFeedforwardTime)
                .andThen(new ConveyorSetPower(conveyor, conf.ConveyorReversePower, true))
                .withTimeout(conf.ConveyorReverseTime)
                .andThen(new ConveyorSetPower(conveyor, conf.ConveyorPushbackPower, true)
                        .withTimeout(conf.ConveyorPushbackTime));
        load = new LoadingCommand(conveyor, shooter, intake, conf, req, j_conf);
        load_hp = new HumanPlayerLoading(conveyor, shooter, intake, conf, req, j_conf);

        shoot = new ShootCommand(conveyor, shooter, hinge, conf, 3500);
        shoot_amp = new AmpDrop(hinge, shooter, conveyor, conf);

        amp_mode = new ShooterSetRPM(shooter, 1000, HoldMode.Indefinite);

        spin_up = new ShooterSetRPM(shooter, 3500, HoldMode.None);

        stop = new ShooterSetRPM(shooter, 0, HoldMode.RPMTarget).alongWith(new ConveyorSetPower(conveyor, 0, false));

        hinge_motion = new HingeGoto(hinge, 0);

        addChild("Conveyor", conveyor);
        addChild("Shooter", shooter);
        addChild("Hinge", hinge);
    }

    public boolean IsBusy() {
        if (action == null)
            return false;
        if (action.isScheduled())
            return true;

        return false;
    }

    public Mode GetMode() {
        return mode;
    }

    public void Shoot() {
        queue(shoot);
        execute();

        if (reset_on_shoot)
            SetMode(Mode.LOAD);
    }

    public void ShootAtAmp() {
        queue(shoot_amp);
        execute();

        if (reset_on_shoot)
            SetMode(Mode.LOAD);
    }

    public void SpinUp() {
        queue(spin_up);
        execute();

    }

    public void Retract() {
        queue(retract);
        execute();

    }

    public void Stop(){
        queue(stop);
        execute();
    }

    public void SetMode(Mode mode) {
        transition_to(this.mode, mode);
        this.mode = mode;
    }

    public void SetShootHingeTarget(double target) {
        hinge_target = target;
    }

    public double GetShootHingeTarget() {
        return hinge_target;
    }

    public void SetShootRPMTarget(double target) {
        System.out.println("Setting target shoot RPM: " + target);
        shoot.SetRPM(target);
        spin_up.SetRPM(target);
    }

    void hinge_goto(double degrees) {
        hinge_motion.SetTarget(degrees * DEG2RAD);
        queue(hinge_motion);
        execute();

    }

    void schedule_default_command() {
        Command desired_command = get_default_cmd();
        if (desired_command == null) {
            if (default_cmd != null && default_cmd.isScheduled())
                default_cmd.cancel();
            return;
        }
        if (default_cmd != null && default_cmd != desired_command)
            default_cmd.cancel();

        if (!desired_command.isScheduled()) {
            desired_command.schedule();
            default_cmd = desired_command;
        }
    }

    void check_hinge_pos() {
        if (IsBusy())
            return;

        hinge_target = Math.min(Math.max(hinge_target, 0), 160);

        if (mode == Mode.LOAD) {
            if (hinge.GetTarget() >= 0.05)
                hinge_goto(0);
        } else if (mode == Mode.LOAD_FROM_HP) {
            double target = 160;
            if (Math.abs(hinge.GetTarget() - target * DEG2RAD) >= 0.05)
                hinge_goto(target);
        } else if (mode == Mode.WAIT) {
            if (hinge.GetTarget() != hinge_target * DEG2RAD)
                hinge_goto(hinge_target);
        } else if (mode == Mode.AMP) {
            double target = 0;
            if (Math.abs(hinge.GetTarget() - target * DEG2RAD) >= 0.05)
                hinge_goto(target);
        }
    }

    void queue(Command action) {
        cmd_queue.add(action);
    }

    void execute() {
        if (IsBusy() || cmd_queue.size() == 0)
            return;

        action = cmd_queue.remove(0);
        action.schedule();
    }

    void DoAction() {
        if (mode == Mode.LOAD)
            Retract();
        else if (mode == Mode.WAIT)
            Shoot();
        else if (mode == Mode.AMP)
            ShootAtAmp();
    }

    Command get_default_cmd() {
        if (mode == Mode.LOAD)
            return load;
        else if (mode == Mode.LOAD_FROM_HP)
            return load_hp;
        else if (mode == Mode.AMP)
            return amp_mode;

        return null;
    }

    void transition_to(Mode from, Mode to) {
        if (from == Mode.LOAD && to != Mode.MANUAL) {
            Retract();
        }

        if(to == Mode.LOAD){
            time_since_last_cmd = Timer.getFPGATimestamp();
        }

        if (to == Mode.WAIT) {
            SpinUp();
        }

        if(to == Mode.MANUAL){
            Stop();
        }
    }

    void prev_mode() {
        Mode next = Mode.LOAD_FROM_HP;
        if (mode == Mode.MANUAL)
            next = Mode.LOAD_FROM_HP;

        if (mode == Mode.LOAD)
            next = Mode.MANUAL;

        if (mode == Mode.WAIT)
            next = Mode.LOAD;

        SetMode(next);
    }

    void next_mode() {
        Mode next = Mode.WAIT;

        if (mode == Mode.MANUAL)
            next = Mode.LOAD;

        if (mode == Mode.LOAD)
            next = Mode.WAIT;

        if (mode == Mode.LOAD_FROM_HP)
            next = Mode.MANUAL;

        SetMode(next);
    }

    @Override
    public void periodic() {
        check_hinge_pos();
        execute();

        if (!IsBusy())
            schedule_default_command();
        else if (default_cmd != null && default_cmd.isScheduled())
            default_cmd.cancel();

        if (detect_note && mode == Mode.LOAD && !IsBusy()) {
            if (conveyor.Stalled && Timer.getFPGATimestamp() - time_since_last_cmd > 0.5)
                has_stalled = true;

            if (has_stalled && !conveyor.Stalled) {
                has_stalled = false;
                SetMode(Mode.WAIT);
            }
        }

        if (!IsBusy() && mode == Mode.WAIT && auto)
            Shoot();

        SmartDashboard.putString("Shooter mode", mode.toString());
    }
}
