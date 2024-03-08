package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;

public class WaitForMode extends Command{
    private final NoteSystemController.Mode mode;
    private final NoteSystemController c;

    public WaitForMode(NoteSystemController c, NoteSystemController.Mode mode){
        this.c = c;
        this.mode = mode;
    }

    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return c.mode == mode;
    }
}
