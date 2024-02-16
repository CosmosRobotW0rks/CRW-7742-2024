package frc.robot.note_manipulation.shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class HingeGoto extends Command {
    private Hinge hinge;
    private double target;

    public HingeGoto(Hinge hinge, double target) {
        this.hinge = hinge;
        this.target = target;

        addRequirements(hinge);
    }

    public void SetTarget(double target) {
        this.target = target;
    }

    @Override
    public void initialize() {
        hinge.SetTarget(target);
    }

    @Override
    public boolean isFinished() {
        return hinge.AtTarget();
    }
}
