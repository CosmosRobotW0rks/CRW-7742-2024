package frc.robot.control;

import frc.robot.drivetrain.VelocityProvider;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Robot;

public class JoystickDriver extends VelocityProvider {
    private DriveJoystickConfiguration config;
    private Robot robot;
    private JoystickRequester joystick;

    public void SetConfig(DriveJoystickConfiguration configuration){
        this.config = configuration;
    }

    public void SetActiveProvider(JoystickProvider provider){
        this.joystick.SetActiveProvider(provider);
    }

    public Translation3d GetVelocity() {
        double xSpeed = joystick.GetAxis(config.ForwardAxis) * config.ForwardCoefficient;
        double ySpeed = joystick.GetAxis(config.RightAxis) * config.RightCoefficient;
        double rot = joystick.GetAxis(config.RotationAxis) * config.RotationCoefficient;

        SmartDashboard.putString("Input Speed: ", ("x: " + xSpeed + ", y: " + ySpeed + ", rot: " + rot));

        double speed_decrease = joystick.GetButton(config.BrakeButton) ? 0.25 : 1;
        double speed_increase = 1 + joystick.GetAxis(config.ThrottleAxis) * config.ThrottleCoefficient;
        double speed_normal = 0.8125;
        double speed = speed_increase * speed_normal * speed_decrease;

        joystick.SetRumble(speed - 1.5);

        xSpeed *= speed;
        ySpeed *= speed;
        rot *= speed_decrease;

        return new Translation3d(xSpeed, ySpeed, rot);
    }

    @Override
    public boolean[] GetEnabledAxes(){
        SetActive(robot.isTeleopEnabled());
        return super.GetEnabledAxes();
    }
}