package frc.robot.control;

import frc.robot.drivetrain.VelocityProvider;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class JoystickDriver extends VelocityProvider {
    private Joystick joystick;
    private JoystickConfiguration conf;

    public VictorSPX spx = new VictorSPX(23);
    
    public JoystickDriver(Joystick joystick, JoystickConfiguration conf) {
        this.joystick = joystick;
        this.conf = conf;
    }

    public double GetAxisWithDeadzone(int axis){
        double val = joystick.getRawAxis(conf.ForwardAxis);
        if(val > conf.Deadzone)
            return val - conf.Deadzone;
        else if (val < -conf.Deadzone)
            return val + conf.Deadzone;
        else
            return 0;
    }

    public Translation3d GetVelocity() {
        double xSpeed = GetAxisWithDeadzone(conf.ForwardAxis) * conf.ForwardCoefficient;
        double ySpeed = GetAxisWithDeadzone(conf.RightAxis) * conf.RightCoefficient;
        double rot = GetAxisWithDeadzone(conf.RotationAxis) * conf.RotationCoefficient;

        SmartDashboard.putString("Input Speed: ", ("x: " + xSpeed + ", y: " + ySpeed + ", rot: " + rot));

        double speed_decrease = joystick.getRawButton(conf.BrakeButton) ? 0.25 : 1; // TODO Speed decrease button
        double speed_increase = 1 + joystick.getRawAxis(conf.ThrottleAxis) * conf.ThrottleCoefficient;
        double speed_normal = 0.8125;
        double speed = speed_increase * speed_normal * speed_decrease;

        spx.set(ControlMode.PercentOutput, joystick.getRawAxis(conf.ThrottleAxis));

        joystick.setRumble(RumbleType.kLeftRumble, speed - 1.5);

        xSpeed *= speed;
        ySpeed *= speed;
        rot *= speed_decrease;

        return new Translation3d(xSpeed, ySpeed, rot);
    }
}