package frc.robot;

import frc.robot.control.JoystickConfiguration;

public class RobotConfiguration {
    public static JoystickConfiguration JoystickA(){
        JoystickConfiguration conf = new JoystickConfiguration();
        conf.ForwardAxis = 1;
        conf.RightAxis = 0;
        conf.RotationAxis = 4;

        conf.ForwardCoefficient = -1;
        conf.RightCoefficient = 1;
        conf.RotationCoefficient = 1 / 6;

        conf.ThrottleAxis = 3;
        conf.ThrottleCoefficient = 5;

        conf.BrakeButton = 6;

        return conf;
    }
}
