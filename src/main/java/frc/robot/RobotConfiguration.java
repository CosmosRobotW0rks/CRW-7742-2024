package frc.robot;

import frc.robot.control.JoystickConfiguration;
import frc.robot.note_manipulation.NoteSystemConfiguration;

public class RobotConfiguration {
    public static final JoystickConfiguration MainJoystick() {
        JoystickConfiguration conf = new JoystickConfiguration();
        conf.ForwardAxis = 1;
        conf.RightAxis = 0;
        conf.RotationAxis = 4;

        conf.ForwardCoefficient = -1;
        conf.RightCoefficient = 1;
        conf.RotationCoefficient = 0.15;

        conf.ThrottleAxis = 3;
        conf.ThrottleCoefficient = 5;

        conf.BrakeButton = 6;

        conf.IntakeOperationThrottle = 2;
        conf.ModeSwitchButton = 5;

        conf.HingeManualControlAxis = 2;
        
        conf.Deadzone = 0.05;

        return conf;
    }

    public static final NoteSystemConfiguration GetNoteSystemConfiguration(){
        NoteSystemConfiguration conf = new NoteSystemConfiguration();
        conf.IntakeMinPower = 1.0;
        conf.IntakeMaxPower = -1.0;

        conf.ShooterMinRPM = 0.0;
        conf.ShooterMaxRPM = 5600.0;

        conf.ConveyorMinPower = 0.25;
        conf.ConveyorMaxPower = -0.25;
        conf.ConveyorReversePower = -0.3;
        conf.ConveyorReverseTime = 0.2;
        conf.ConveyorPushbackPower = 0.15;
        conf.ConveyorPushbackTime = 0.2;
        conf.InputOperationShooterSpeed = -250;

        conf.ConveyorMaxCurrent = 5;

        conf.ShootConveyorReversePower = -0.15;
        conf.ShootConveyorReverseTime = 0.1;

        conf.ShootConveyorPower = 1;
        conf.ShootConveyorTime = 0.25;

        conf.PostShootHoldTime = 0.75;

        conf.HingeMinPower = 0;
        conf.HingeMaxPower = 1;

        return conf;
    }
}
