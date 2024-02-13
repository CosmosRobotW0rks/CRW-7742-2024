package frc.robot.control;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.drivetrain.VelocityProvider;

public class JoystickDriver extends VelocityProvider {
    private JoystickConfiguration config;
    private JoystickRequester joystick;
    private Robot robot;

    public void Init(Robot robot, JoystickProvider provider){
        this.robot = robot;
        this.joystick = new JoystickRequester(provider);
    }

    public void SetConfig(JoystickConfiguration configuration){
        this.config = configuration;
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