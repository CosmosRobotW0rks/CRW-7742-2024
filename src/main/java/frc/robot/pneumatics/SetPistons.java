package frc.robot.pneumatics;

import edu.wpi.first.wpilibj2.command.Command;

public class SetPistons extends Command {
    private Pneumatics pneumatics;
    private boolean state_l;
    private boolean state_r;
    private boolean toggle;

    private boolean has_toggled = false;

    public SetPistons(Pneumatics pneumatics) {
        this.pneumatics = pneumatics;
        this.toggle = true;
    }

    public SetPistons(Pneumatics pneumatics, boolean state) {
        this.pneumatics = pneumatics;
        this.state_l = state;
        this.state_r = state;
    }

    public SetPistons(Pneumatics pneumatics, boolean state_l, boolean state_r) {
        this.pneumatics = pneumatics;
        this.state_l = state_l;
        this.state_r = state_r;
    }

    @Override
    public void initialize() {
        if (this.toggle)
            pneumatics.SetPistons();
        else
            pneumatics.SetPistons(state_l, state_r);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
