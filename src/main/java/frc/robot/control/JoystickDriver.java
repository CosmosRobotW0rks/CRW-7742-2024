package frc.robot.control;

import frc.robot.drivetrain.VelocityProvider;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.drivetrain.SwerveDrivetrain;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class JoystickDriver extends VelocityProvider {
    private Joystick joystick;
    private JoystickConfiguration conf;
    private Robot robot;

    public JoystickDriver(Robot robot, Joystick joystick, JoystickConfiguration conf) {
        this.joystick = joystick;
        this.conf = conf;
        this.robot = robot;
    }

    public Translation3d GetVelocity() {
        SetActive(robot.isTeleopEnabled());

        double xSpeed = joystick.getRawAxis(conf.ForwardAxis) * conf.ForwardCoefficient;
        double ySpeed = joystick.getRawAxis(conf.RightAxis) * conf.RightCoefficient;
        double rot = joystick.getRawAxis(conf.RotationAxis) * conf.RotationCoefficient;

        SmartDashboard.putString("Input Speed: ", ("x: " + xSpeed + ", y: " + ySpeed + ", rot: " + rot));

        xSpeed = Math.abs(xSpeed) > 0.2 ? xSpeed : 0;
        ySpeed = Math.abs(ySpeed) > 0.2 ? ySpeed : 0;
        rot = Math.abs(rot) > 0.03125 ? rot : 0;

        double speed_decrease = joystick.getRawButton(conf.BrakeButton) ? 0.25 : 1; // TODO Speed decrease button
        double speed_increase = 1 + joystick.getRawAxis(conf.ThrottleAxis) * conf.ThrottleCoefficient;
        double speed_normal = 0.8125;
        double speed = speed_increase * speed_normal * speed_decrease;

        joystick.setRumble(RumbleType.kLeftRumble, speed - 1.5);

        xSpeed *= speed;
        ySpeed *= speed;
        rot *= 0.5 * speed_decrease;

        return new Translation3d(xSpeed, ySpeed, rot);
    }
}