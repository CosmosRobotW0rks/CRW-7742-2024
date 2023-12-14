package frc.robot.control;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.drivetrain.SwerveDrivetrain;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//TODO Multiple joystick support by registering independent joysticks
public class JoystickDriver extends SubsystemBase {
    private Joystick joystick = new Joystick(0);
    //private Joystick joystick_2 = new Joystick(1);
    private SwerveDrivetrain drivetrain;
    private RobotContainer r;

    public JoystickDriver() {
        r = Robot.c;
        drivetrain = r.drivetrain;
    }

    @Override
    public void periodic() {
        double xSpeed = -joystick.getRawAxis(1) / 1;
        double ySpeed = joystick.getRawAxis(0) / 1;
        double rot = joystick.getRawAxis(4) / 6;

        if (joystick.getRawButton(4)) {
            xSpeed = -joystick.getRawAxis(1);
            ySpeed = joystick.getRawAxis(0);
            rot = joystick.getRawAxis(4) / 6;
        }

        SmartDashboard.putString("Input Speed: ", ("x: " + xSpeed + ", y: " + ySpeed + ", rot: " + rot));

        xSpeed = Math.abs(xSpeed) > 0.2 ? xSpeed : 0;
        ySpeed = Math.abs(ySpeed) > 0.2 ? ySpeed : 0;
        rot = Math.abs(rot) > 0.03125 ? rot : 0;

        double speed_decrease = joystick.getRawButton(6) ? 0.25 : 1; // TODO Speed decrease button
        double speed_increase = (1 + joystick.getRawAxis(3) * 5);
        double speed_normal = 0.8125;
        double speed = speed_increase * speed_normal * speed_decrease;

        joystick.setRumble(RumbleType.kLeftRumble, speed - 1.5);

        xSpeed *= speed;
        ySpeed *= speed;
        rot *= 0.5 * speed_decrease;

        drivetrain.SetSpeed(new Translation2d(xSpeed, ySpeed), rot);
    }
}