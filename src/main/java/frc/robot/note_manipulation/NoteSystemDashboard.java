package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class NoteSystemDashboard extends SubsystemBase {
    private NoteSystemConfiguration conf;

    public void Init(NoteSystemConfiguration conf) {
        this.conf = conf;

        SmartDashboard.putNumber("Intake Minimum", conf.IntakeMinPower);
        SmartDashboard.putNumber("Intake Maximum", conf.IntakeMinPower);

        SmartDashboard.putNumber("Shooter Minimum", conf.IntakeMinPower);
        SmartDashboard.putNumber("Shooter Minimum", conf.IntakeMinPower);
        
        SmartDashboard.putNumber("Conveyor Minimum", conf.IntakeMinPower);
        SmartDashboard.putNumber("Conveyor Maximum", conf.IntakeMinPower);
        SmartDashboard.putNumber("Conveyor Reverse", conf.IntakeMinPower);
        
        SmartDashboard.putNumber("Conveyor Max Current", conf.IntakeMinPower);
    }

    @Override
    public void periodic(){
        conf.IntakeMinPower = SmartDashboard.getNumber("Intake Minimum", conf.IntakeMinPower);
        conf.IntakeMaxPower = SmartDashboard.getNumber("Intake Maximum", conf.IntakeMinPower);

        conf.ShooterMinRPM = SmartDashboard.getNumber("Shooter Minimum", conf.IntakeMinPower);
        conf.ShooterMaxRPM = SmartDashboard.getNumber("Shooter Minimum", conf.IntakeMinPower);
        
        conf.ConveyorMinPower = SmartDashboard.getNumber("Conveyor Minimum", conf.IntakeMinPower);
        conf.ConveyorMaxPower = SmartDashboard.getNumber("Conveyor Maximum", conf.IntakeMinPower);
        conf.ConveyorReversePower = SmartDashboard.getNumber("Conveyor Reverse", conf.IntakeMinPower);
        
        conf.ConveyorMaxCurrent = SmartDashboard.getNumber("Conveyor Max Current", conf.IntakeMinPower);
    }
}
