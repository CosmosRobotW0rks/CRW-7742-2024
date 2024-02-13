package frc.robot.note_manipulation.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.RollingAverage;
import frc.robot.power.Power;

public class Conveyor extends SubsystemBase {
    private VictorSPX r_ctl = new VictorSPX(31);
    private VictorSPX l_ctl = new VictorSPX(32);

    private Power power;

    double output = 0;

    private int conveyor_l_pdp = 11;
    private int conveyor_r_pdp = 5;

    private RollingAverage CurrentAverage5s = new RollingAverage(5);
    private RollingAverage CurrentAverage0_5s = new RollingAverage(0.5);

    private boolean stalled = false;

    public void Init(Power power) {
        this.power = power;
    }

    public void SetPower(double power) {
        this.output = power;
    }

    public double GetCurrent() {
        double l_current = power.GetCurrent(conveyor_l_pdp);
        double r_current = power.GetCurrent(conveyor_r_pdp);

        return (l_current + r_current) / 2;
    }

    @Override
    public void periodic() {
        r_ctl.set(ControlMode.PercentOutput, -output);
        l_ctl.set(ControlMode.PercentOutput, output);

        double l_current = power.GetCurrent(conveyor_l_pdp);
        double r_current = power.GetCurrent(conveyor_r_pdp);

        CurrentAverage5s.AddSample(GetCurrent());
        CurrentAverage0_5s.AddSample(GetCurrent());

        double avg_short = CurrentAverage0_5s.GetAverage();
        double avg_long = CurrentAverage5s.GetAverage() * 1.2;

        SmartDashboard.putNumber("Left Conveyor Current", l_current);
        SmartDashboard.putNumber("Right Conveyor Current", r_current);
        SmartDashboard.putNumber("Conveyor Average Short", avg_short);
        SmartDashboard.putNumber("Conveyor Average Long", avg_long);
    }
}
