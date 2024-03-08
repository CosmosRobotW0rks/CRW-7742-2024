package frc.robot.pneumatics;

import edu.wpi.first.wpilibj2.command.Command;

public class SetCompressor extends Command {
    private Pneumatics pneumatics;
    private boolean state;
    private boolean toggle;

    public SetCompressor(Pneumatics pneumatics) {
        this.pneumatics = pneumatics;
        this.toggle = true;
    }

    public SetCompressor(Pneumatics pneumatics, boolean state) {
        this.pneumatics = pneumatics;
        this.state = state;
    }

    @Override
    public void initialize() {
        if (this.toggle)
            pneumatics.ToggleCompressor();
        else
            pneumatics.SetCompressor(state);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
