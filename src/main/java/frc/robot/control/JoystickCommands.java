package frc.robot.control;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import frc.robot.RobotConfiguration;
import frc.robot.RobotContainer;
import frc.robot.commands.SetPosition;
import frc.robot.commands.SquareUp;
import frc.robot.commands.SquareUp.Position;
import frc.robot.drive.RotateTo;
import frc.robot.note_manipulation.DoAction;
import frc.robot.note_manipulation.SetMode;
import frc.robot.note_manipulation.Shoot;
import frc.robot.note_manipulation.ShootModeConstantVelTargetMotion;
import frc.robot.note_manipulation.ShooterJoystickOverride;
import frc.robot.note_manipulation.NoteSystemController.Mode;
import frc.robot.pneumatics.SetCompressor;
import frc.robot.pneumatics.SetPistons;

public class JoystickCommands {
    private Joystick joystick;
    private JoystickConfiguration conf;
    private RobotContainer c;

    public JoystickCommands() {

    }

    public void Init(Joystick joystick, JoystickProvider p, JoystickConfiguration conf, RobotContainer c) {
        this.joystick = joystick;
        this.conf = conf;
        this.c = c;

        SquareUp square_up = new SquareUp(c);

        conf.IntakeModeTrigger.GetTrigger(joystick).whileTrue(new SetMode(c.note_c, Mode.LOAD));
        conf.ManualModeTrigger.GetTrigger(joystick).whileTrue(new SetMode(c.note_c, Mode.MANUAL));
        conf.HPModeTrigger.GetTrigger(joystick).whileTrue(new SetMode(c.note_c, Mode.LOAD_FROM_HP));
        conf.ShootModeTrigger.GetTrigger(joystick).whileTrue(new SetMode(c.note_c, Mode.WAIT, 3500, 0));
        conf.AmpModeTrigger.GetTrigger(joystick).whileTrue(new SetMode(c.note_c, Mode.AMP));

        conf.CompressorToggleTrigger.GetTrigger(joystick).whileTrue(new SetCompressor(c.pneumatics));
        conf.ClimbToggleTrigger.GetTrigger(joystick).whileTrue(new SetPistons(c.pneumatics));

        conf.RaiseHingeTrigger.GetTrigger(joystick).whileTrue(new ShootModeConstantVelTargetMotion(c.note_c, 20));
        conf.LowerHingeTrigger.GetTrigger(joystick).whileTrue(new ShootModeConstantVelTargetMotion(c.note_c, -20));

        conf.ActionTrigger.GetTrigger(joystick).whileTrue(new DoAction(c.note_c));

        conf.RotateToHomeTrigger.GetTrigger(joystick).whileTrue(new RotateTo(c, 0));
        conf.AlignTrigger.GetTrigger(joystick).whileTrue(square_up);

        conf.SetNearLeft.GetTrigger(joystick).whileTrue(new SetPosition(square_up, Position.NEAR_OR_LEFT));
        conf.SetMiddleCenter.GetTrigger(joystick).whileTrue(new SetPosition(square_up, Position.MIDDLE_OR_FRONT));
        conf.SetFarRight.GetTrigger(joystick).whileTrue(new SetPosition(square_up, Position.FAR_OR_RIGHT));

        new ShooterJoystickOverride(c.shooter,
                conf.ShooterManualControlAxis, new JoystickRequester(p), c.note_c).schedule();
        new ShooterJoystickOverride(c.shooter,
                conf.ConveyorManualControlAxis, new JoystickRequester(p), c.note_c).schedule();
    }
}
