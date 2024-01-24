package frc.robot;

import frc.robot.control.DriveJoystickConfiguration;

public class RobotConfiguration {
    public static final DriveJoystickConfiguration MainJoystick() {
        DriveJoystickConfiguration conf = new DriveJoystickConfiguration();
        conf.ForwardAxis = 1;
        conf.RightAxis = 0;
        conf.RotationAxis = 4;

        conf.ForwardCoefficient = -1;
        conf.RightCoefficient = 1;
        conf.RotationCoefficient = 0.15;

        conf.ThrottleAxis = 3;
        conf.ThrottleCoefficient = 5;

        conf.BrakeButton = 6;
        
        conf.Deadzone = 0.05;

        return conf;
    }
}
