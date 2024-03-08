package frc.robot.note_manipulation.shooter;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private CANSparkMax left = new CANSparkMax(39, MotorType.kBrushless);
    private CANSparkMax right = new CANSparkMax(36, MotorType.kBrushless);

    private RelativeEncoder left_encoder;
    private RelativeEncoder right_encoder;

    double rpm = 0;
    double conveyor = 0;

    SparkPIDController left_controller;
    SparkPIDController right_controller;
    // private CANSparkMax right = new CANSparkMax(AngleCANID,
    // CANSparkMaxLowLevel.MotorType.kBrushless);

    public void Init() {
        left_encoder = left.getEncoder();
        right_encoder = right.getEncoder();

        left_controller = left.getPIDController();
        left_controller.setP(0.25e-5);
        left_controller.setI(3e-7);
        left_controller.setD(0.0);
        left_controller.setOutputRange(-1, 1);

        right_controller = right.getPIDController();
        right_controller.setP(0.25e-5);
        right_controller.setI(3e-7);
        right_controller.setD(0.0);
        right_controller.setOutputRange(-1, 1);
    }

    public void SetShooter(double rpm) {
        this.rpm = rpm;
    }

    public boolean AtTargetRPM() {
        double l_current = left_encoder.getVelocity();
        double r_current = right_encoder.getVelocity();

        double l_diff = Math.abs(l_current - rpm);
        double r_diff = Math.abs(-r_current - rpm);

        return l_diff + r_diff < 250;
    }

    public double GetRPM() {
        double l_current = left_encoder.getVelocity();
        double r_current = -right_encoder.getVelocity();

        return (l_current + r_current) / 2;
    }

    @Override
    public void periodic() {
        left_controller.setReference(rpm, ControlType.kVelocity);
        right_controller.setReference(-rpm, ControlType.kVelocity);
    }
}
