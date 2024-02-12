package frc.robot.note_manipulation.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Hinge extends SubsystemBase {
    private VictorSPX motor = new VictorSPX(51);

    double desired_output = 0;

    public void SetOutput(double power){
        desired_output = power;
    }

    @Override
    public void periodic() {
        motor.set(ControlMode.PercentOutput, -desired_output);
    }
}
