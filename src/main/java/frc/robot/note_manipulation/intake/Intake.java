package frc.robot.note_manipulation.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.control.JoystickProvider;
import frc.robot.control.JoystickRequester;

public class Intake extends SubsystemBase {
    //private boolean reverse;

    private double output = 1;

    private VictorSPX front = new VictorSPX(41);
    private VictorSPX back = new VictorSPX(42);

    public void SetPower(double power){
        this.output = power;
    }

    @Override
    public void periodic() {
        front.set(ControlMode.PercentOutput, output);
        back.set(ControlMode.PercentOutput, -output);
    }
}
