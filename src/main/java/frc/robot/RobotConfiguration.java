package frc.robot;

import frc.robot.control.JoystickConfiguration;
import frc.robot.control.JoystickTrigger;
import frc.robot.control.JoystickTrigger.Source;
import frc.robot.note_manipulation.NoteSystemConfiguration;

public class RobotConfiguration {
    public static final double CoarseXSpeed = 1.5;
    public static final double CoarseYSpeed = 0.5;

    public static final double FineTransSpeed = 1;

    public static final double CoarseLocalizationPrecision = 1;
    public static final double FineLocalizationPrecision = 0.1;

    public static final JoystickConfiguration MainJoystick() {
        JoystickConfiguration conf = new JoystickConfiguration();
        conf.ForwardAxis = 1;
        conf.RightAxis = 0;
        conf.RotationAxis = 4;

        conf.ForwardCoefficient = -1;
        conf.RightCoefficient = -1;
        conf.RotationCoefficient = -0.15;

        conf.ThrottleAxis = 3;
        conf.ThrottleCoefficient = 10;

        conf.BrakeButton = 6;

        conf.LoadingThrottle = 2;

        conf.IntakeModeTrigger = new JoystickTrigger(90, JoystickTrigger.Source.POV);
        conf.ManualModeTrigger = new JoystickTrigger(180, JoystickTrigger.Source.POV);
        conf.HPModeTrigger = new JoystickTrigger(270, JoystickTrigger.Source.POV);
        conf.AmpModeTrigger = new JoystickTrigger(8, JoystickTrigger.Source.BUTTON);
        conf.ShootModeTrigger = new JoystickTrigger(0, JoystickTrigger.Source.POV);

        conf.CompressorToggleTrigger = new JoystickTrigger(7, JoystickTrigger.Source.BUTTON);
        conf.CompressorEnableTrigger = new JoystickTrigger();
        conf.CompressorDisableTrigger = new JoystickTrigger();

        conf.ClimbToggleTrigger = new JoystickTrigger(6, JoystickTrigger.Source.BUTTON);

        conf.ClimbLeftToggleTrigger = new JoystickTrigger();
        conf.ClimbRightToggleTrigger = new JoystickTrigger();

        conf.ClibmUpTrigger = new JoystickTrigger();
        conf.ClibmDownTrigger = new JoystickTrigger();

        conf.RaiseHingeTrigger = new JoystickTrigger(2, JoystickTrigger.Source.BUTTON);
        conf.LowerHingeTrigger = new JoystickTrigger(1, JoystickTrigger.Source.BUTTON);

        conf.ActionTrigger = new JoystickTrigger(5, JoystickTrigger.Source.BUTTON);

        conf.AlignTrigger = new JoystickTrigger(9, JoystickTrigger.Source.BUTTON);
        conf.RotateToHomeTrigger = new JoystickTrigger(10, JoystickTrigger.Source.BUTTON);

        conf.SetNearLeft = new JoystickTrigger(3, JoystickTrigger.Source.BUTTON);
        conf.SetMiddleCenter = new JoystickTrigger(4, JoystickTrigger.Source.BUTTON);
        conf.SetFarRight = new JoystickTrigger(2, JoystickTrigger.Source.BUTTON);

        conf.Deadzone = 0.05;

        return conf;
    }

    public static final JoystickConfiguration SecondaryJoystick() {
        JoystickConfiguration conf = new JoystickConfiguration();

        conf.ShooterManualControlAxis = 1;
        conf.ConveyorManualControlAxis = 5;

        conf.CompressorEnableTrigger = new JoystickTrigger(8, JoystickTrigger.Source.BUTTON);
        conf.CompressorDisableTrigger = new JoystickTrigger(7, JoystickTrigger.Source.BUTTON);

        conf.ClimbLeftToggleTrigger = new JoystickTrigger(270, JoystickTrigger.Source.POV);
        conf.ClimbRightToggleTrigger = new JoystickTrigger(90, JoystickTrigger.Source.BUTTON);

        conf.ClibmUpTrigger = new JoystickTrigger(0, JoystickTrigger.Source.POV);
        conf.ClibmDownTrigger = new JoystickTrigger(180, JoystickTrigger.Source.POV);

        conf.RaiseHingeTrigger = new JoystickTrigger(4, JoystickTrigger.Source.BUTTON);
        conf.LowerHingeTrigger = new JoystickTrigger(3, JoystickTrigger.Source.BUTTON);

        conf.ActionTrigger = new JoystickTrigger(1, JoystickTrigger.Source.BUTTON);

        conf.Deadzone = 0.05;

        return conf;
    }

    public static final NoteSystemConfiguration GetNoteSystemConfiguration() {
        NoteSystemConfiguration conf = new NoteSystemConfiguration();
        conf.IntakeMinPower = 1;
        conf.IntakeMaxPower = -1;

        conf.ShooterMinRPM = 1000.0;
        conf.ShooterMaxRPM = 5600.0;

        conf.ConveyorMinPower = 0.35;
        conf.ConveyorMaxPower = -0.3;
        conf.ConveyorFeedforwardPower = 1;
        conf.ConveyorFeedforwardTime = 0.5;
        conf.ConveyorReversePower = -0.3;
        conf.ConveyorReverseTime = 0.35;
        conf.ConveyorPushbackPower = 0.15;
        conf.ConveyorPushbackTime = 0.3;
        conf.InputOperationShooterSpeed = -25;

        conf.ConveyorMaxCurrent = 5;

        conf.ShootConveyorReversePower = -0.225;
        conf.ShootConveyorReverseTime = 0.4;

        conf.ShootConveyorPower = 1;
        conf.ShootConveyorTime = 0.35;

        conf.PreShootHoldTime = 0.25;
        conf.PostShootHoldTime = 0.75;

        conf.HingeMinPower = 0;
        conf.HingeMaxPower = 1;

        return conf;
    }
}
