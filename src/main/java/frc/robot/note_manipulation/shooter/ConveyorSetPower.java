package frc.robot.note_manipulation.shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class ConveyorSetPower extends Command {
    private Conveyor conveyor;
    private double power;
    private boolean hold;

    public ConveyorSetPower(Conveyor conveyor, double power, boolean hold) {
        this.conveyor = conveyor;
        this.power = power;
        this.hold = hold;

        addRequirements(conveyor);
    }

    @Override
    public void initialize() {
        conveyor.SetPower(power);
    }

    @Override
    public void end(boolean interrupted) {
        if(this.hold)
            conveyor.SetPower(0);
    }

    @Override
    public boolean isFinished() {
        return !this.hold;
    }
}
