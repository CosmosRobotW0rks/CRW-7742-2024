package frc.robot.note_manipulation.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

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

    private RollingAverage CurrentAverageLong = new RollingAverage(2);
    private RollingAverage CurrentAverageShort = new RollingAverage(0.15);

    private RollingAverage StallAverage = new RollingAverage(0.75);

    public boolean Stalled = false;

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

        if (output != 0) {
            CurrentAverageLong.AddSample(GetCurrent() / Math.abs(output));
            CurrentAverageShort.AddSample(GetCurrent() / Math.abs(output));
        }

        double avg_short = CurrentAverageShort.GetAverage();
        double avg_long = CurrentAverageLong.GetAverage();

        StallAverage.AddSample((avg_short > avg_long * 1.2) ? 1 : 0);

        Stalled = StallAverage.GetAverage() > 0.75;

        SmartDashboard.putNumber("Left Conveyor Current", l_current);
        SmartDashboard.putNumber("Right Conveyor Current", r_current);
        SmartDashboard.putNumber("Conveyor Average Short", avg_short);
        SmartDashboard.putNumber("Conveyor Average Long", avg_long);
    }
}
