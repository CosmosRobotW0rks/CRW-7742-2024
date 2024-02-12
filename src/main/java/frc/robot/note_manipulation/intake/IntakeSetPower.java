package frc.robot.note_manipulation.intake;

import edu.wpi.first.wpilibj2.command.Command;

public class IntakeSetPower extends Command {
    private Intake intake;
    private double power;
    private boolean hold;

    public IntakeSetPower(Intake intake, double power, boolean hold) {
        this.intake = intake;
        this.power = power;
        this.hold = hold;

        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.SetPower(power);
    }

    @Override
    public void end(boolean interrupted) {
        if(this.hold)
            intake.SetPower(0);
    }

    @Override
    public boolean isFinished() {
        return !this.hold;
    }
}
