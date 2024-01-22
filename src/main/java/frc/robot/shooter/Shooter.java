package frc.robot.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.control.JoystickProvider;
import frc.robot.control.JoystickRequester;

public class Shooter extends SubsystemBase{
    private JoystickRequester joystick;

    private CANSparkMax left = new CANSparkMax(35, MotorType.kBrushless);
    private CANSparkMax right = new CANSparkMax(34, MotorType.kBrushless);

    private VictorSPX aux_r = new VictorSPX(37);
    private VictorSPX aux_l = new VictorSPX(38);

    private RelativeEncoder left_encoder;
    private RelativeEncoder right_encoder;

    SparkMaxPIDController left_controller;
    SparkMaxPIDController right_controller;
    //private CANSparkMax right = new CANSparkMax(AngleCANID, CANSparkMaxLowLevel.MotorType.kBrushless);

    public void Init(JoystickProvider provider){
        this.joystick = new JoystickRequester(provider);

        left_encoder = left.getEncoder();
        right_encoder = right.getEncoder();

        left_controller = left.getPIDController();
        left_controller.setP(0.0005);
        left_controller.setI(0.0);
        left_controller.setD(0.000);
        left_controller.setOutputRange(-1, 1);

        right_controller = right.getPIDController();
        right_controller.setP(0.0005);
        right_controller.setI(0.0);
        right_controller.setD(0.000);
        right_controller.setOutputRange(-1, 1);
    }

    @Override
    public void periodic() {
        double rpm = joystick.GetAxis(2) * 1;

        double aux_out = joystick.GetButton(5) ? 1 : 0;

        left_controller.setReference(-rpm, ControlType.kDutyCycle);
        right_controller.setReference(rpm, ControlType.kDutyCycle);

        aux_r.set(ControlMode.PercentOutput, -aux_out);
        aux_l.set(ControlMode.PercentOutput, aux_out);
    }
}
