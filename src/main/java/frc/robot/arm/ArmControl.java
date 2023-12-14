package frc.robot.arm;

import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ArmControl extends SubsystemBase{
    private VictorSPX shoulderMotor = new VictorSPX(11);

    public double shoulderPower = 0;
    public double upperPower = 0;
    public double lowerPower = 0;

    private Servo servoA = new Servo(2);
    private Servo servoB = new Servo(1);

    public double gripperOpenPercent = 1;

    @Override
    public void periodic(){
        shoulderMotor.set(VictorSPXControlMode.PercentOutput, shoulderPower);

        servoA.setAngle(110 - gripperOpenPercent * 90);
        servoB.setAngle(80 + gripperOpenPercent * 90);
    }
}
