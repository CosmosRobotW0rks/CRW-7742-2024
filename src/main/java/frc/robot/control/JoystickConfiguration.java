package frc.robot.control;

public class JoystickConfiguration {
    // Axes / buttons
    public int ForwardAxis = -1;
    public int RightAxis = -1;
    public int RotationAxis = -1;

    public double ForwardCoefficient = -1;
    public double RightCoefficient = -1;
    public double RotationCoefficient = -1;

    public int ThrottleAxis = -1;
    public double ThrottleCoefficient = -1;

    public int BrakeButton = -1;

    public int LoadingThrottle = -1;

    public int HingeManualControlAxis = -1;
    public int ShooterManualControlAxis = -1;
    public int ConveyorManualControlAxis = -1;

    // Commands
    public JoystickTrigger IntakeModeTrigger = new JoystickTrigger();
    public JoystickTrigger ManualModeTrigger = new JoystickTrigger();
    public JoystickTrigger HPModeTrigger = new JoystickTrigger();
    public JoystickTrigger ShootModeTrigger = new JoystickTrigger();
    public JoystickTrigger AmpModeTrigger = new JoystickTrigger();

    public JoystickTrigger CompressorToggleTrigger = new JoystickTrigger();
    public JoystickTrigger CompressorEnableTrigger = new JoystickTrigger();
    public JoystickTrigger CompressorDisableTrigger = new JoystickTrigger();

    public JoystickTrigger ClimbToggleTrigger = new JoystickTrigger();

    public JoystickTrigger ClimbLeftToggleTrigger = new JoystickTrigger();
    public JoystickTrigger ClimbRightToggleTrigger = new JoystickTrigger();

    public JoystickTrigger ClibmUpTrigger = new JoystickTrigger();
    public JoystickTrigger ClibmDownTrigger = new JoystickTrigger();

    public JoystickTrigger RaiseHingeTrigger = new JoystickTrigger();
    public JoystickTrigger LowerHingeTrigger = new JoystickTrigger();

    public JoystickTrigger ActionTrigger = new JoystickTrigger();

    public JoystickTrigger AlignTrigger = new JoystickTrigger();
    public JoystickTrigger RotateToHomeTrigger = new JoystickTrigger();

    public JoystickTrigger SetNearLeft = new JoystickTrigger();
    public JoystickTrigger SetMiddleCenter = new JoystickTrigger();
    public JoystickTrigger SetFarRight = new JoystickTrigger();

    // Hardware
    public double Deadzone;
}
