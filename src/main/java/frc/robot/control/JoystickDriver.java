package frc.robot.control;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.drivetrain.SwerveDrivetrain;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class JoystickDriver extends SubsystemBase {
    private Joystick joystick = new Joystick(0);
    //private Joystick joystick_2 = new Joystick(1);
    private SwerveDrivetrain drivetrain;
    private RobotContainer r;

    public JoystickDriver() {
        r = Robot.c;
        drivetrain = r.drivetrain;
        // new JoystickButton(joystick, 1).whenPressed(new ShootCommand(shooter, 27,
        // 8.5));
        // new JoystickButton(joystick, 6).whenPressed(lookCMD);
    }

    @Override
    public void periodic() {
        double xSpeed = -joystick.getRawAxis(1) / 1;// + -joystick_2.getRawAxis(1) / 2;
        double ySpeed = joystick.getRawAxis(0) / 1;// + joystick_2.getRawAxis(0) / 2;
        double rot = joystick.getRawAxis(4) / 6; //+;//  + joystick_2.getRawAxis(2) / 12;

        /*if (joystick_2.getRawButton(4)) {
            xSpeed = -joystick_2.getRawAxis(1);
            ySpeed = joystick_2.getRawAxis(0);
            rot = joystick_2.getRawAxis(4) / 6;
        }*/

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

        r.arm.shoulderPower = (joystick.getRawAxis(2) + 0) / 2 + (joystick.getRawButton(5) ? -0.25 : 0); // (joystick.getRawButton(2)
                                                                                                   // ? -1 : 0) +
                                                                                                   // (joystick.getRawButton(4)
                                                                                                   // ? 1 : 0);

        //r.arm.gripperOpenPercent = (joystick_2.getRawAxis(4) + 1) / 2 - (joystick_2.getRawButton(6) ? 0.7 : 0);
    }
}