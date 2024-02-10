package frc.robot.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.control.JoystickProvider;
import frc.robot.control.JoystickRequester;

public class Shooter extends SubsystemBase {
    private JoystickRequester joystick;

    private CANSparkMax left = new CANSparkMax(39, MotorType.kBrushless);
    private CANSparkMax right = new CANSparkMax(36, MotorType.kBrushless);

    private VictorSPX aux_r = new VictorSPX(31);
    private VictorSPX aux_l = new VictorSPX(32);

    private RelativeEncoder left_encoder;
    private RelativeEncoder right_encoder;

    SparkPIDController left_controller;
    SparkPIDController right_controller;
    // private CANSparkMax right = new CANSparkMax(AngleCANID,
    // CANSparkMaxLowLevel.MotorType.kBrushless);

    public void Init(JoystickProvider provider) {
        this.joystick = new JoystickRequester(provider);

        left_encoder = left.getEncoder();
        right_encoder = right.getEncoder();

        left_controller = left.getPIDController();
        left_controller.setP(1.5e-5);
        left_controller.setI(5e-7);
        left_controller.setD(0.0);
        left_controller.setOutputRange(-1, 1);

        right_controller = right.getPIDController();
        right_controller.setP(1.5e-5);
        right_controller.setI(5e-7);
        right_controller.setD(0.0);
        right_controller.setOutputRange(-1, 1);
    }

    @Override
    public void periodic() {
        double rpm = joystick.GetAxis(2) * 5000;

        double aux_out = joystick.GetButton(5) ? 0.5 : 0;

        if (joystick.GetButton(1))
            aux_out = -0.25;

        if (rpm > 500) {
            left_controller.setReference(rpm, ControlType.kVelocity);
            right_controller.setReference(-rpm, ControlType.kVelocity);
        } else {
            left_controller.setReference(0, ControlType.kDutyCycle);
            right_controller.setReference(0, ControlType.kDutyCycle);
        }

        aux_r.set(ControlMode.PercentOutput, -aux_out);
        aux_l.set(ControlMode.PercentOutput, aux_out);
    }
}
