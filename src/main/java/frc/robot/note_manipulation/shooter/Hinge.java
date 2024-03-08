package frc.robot.note_manipulation.shooter;

import java.util.Date;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.RollingAverage;

public class Hinge extends SubsystemBase {
    private final double POSITION_TOLERANCE = 0.1;
    private VictorSPX motor = new VictorSPX(51);
    private Encoder encoder = new Encoder(0, 1);

    private final double RADIANS_PER_PULSE = (2 * Math.PI) / ((226233 / 3179) * 7 * 4);

    private PIDController angle_pid = new PIDController(2, 1.6, 0);

    public double MinRadians = 0;
    public double MaxRadians = 3;

    boolean const_out = false;
    double desired_output = 0;

    boolean angle_control = false;
    double target_radians = 0;
    double max_spd = 0.8;
    double target_angle_spd = 0;

    double last_time = 0;

    public Hinge() {
        encoder.setReverseDirection(true);
        encoder.setDistancePerPulse(RADIANS_PER_PULSE);

        angle_pid.setIntegratorRange(0, 0.75);

        last_time = ((double) new Date().getTime()) / 1000.0;

        SmartDashboard.putNumber("Target Angle", 0);
    }

    public void SetOutput(double power) {
        desired_output = power;
        const_out = true;
        angle_control = false;
    }

    public void SetTarget(double radians) {
        target_radians = radians;
        const_out = false;
        angle_control = true;
    }

    public double GetTarget(){
        return target_radians;
    }

    public void Home() {
    }

    public double GetPosition() {
        return encoder.getDistance();
    }

    public boolean AtTarget(){
        return Math.abs(target_radians - GetPosition()) < POSITION_TOLERANCE;
    }

    void UpdatePID() {
        double new_time = ((double) new Date().getTime()) / 1000.0;
        double delta = new_time - last_time;
        last_time = new_time;

        double delta_pos = delta * max_spd;

        if (Math.abs(target_angle_spd - target_radians) < delta_pos)
            target_angle_spd = target_radians;
        else if (target_angle_spd > target_radians)
            target_angle_spd = target_angle_spd - delta_pos;
        else if (target_angle_spd < target_radians)
            target_angle_spd = target_angle_spd + delta_pos;

        if (target_angle_spd <= 0.05 && encoder.getDistance() <= 0.05) {
            motor.set(ControlMode.PercentOutput, 0);
            return;
        }

        double out = angle_pid.calculate(encoder.getDistance(), target_angle_spd);
        SmartDashboard.putNumber("Hinge Output", out);

        if (encoder.getDistance() < 1.5 && out < -0.4)
            out = -0.4;

        motor.set(ControlMode.PercentOutput, -out);
    }

    @Override
    public void periodic() {
        target_radians = Math.min(Math.max(target_radians, MinRadians), MaxRadians);

        if (const_out)
            motor.set(ControlMode.PercentOutput, -desired_output);
        else if (angle_control)
            UpdatePID();
        else
            motor.set(ControlMode.PercentOutput, 0);

        SmartDashboard.putData("Encoder", encoder);
        SmartDashboard.putNumber("Position", encoder.getDistance());

        angle_control = true;
        //target_radians = SmartDashboard.getNumber("Target Angle", 0);
    }
}
