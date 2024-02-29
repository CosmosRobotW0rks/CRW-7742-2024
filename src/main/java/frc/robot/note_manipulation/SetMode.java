package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;

public class SetMode extends Command {
    private final NoteSystemController.Mode mode;
    private final NoteSystemController c;
    public SetMode(NoteSystemController c, NoteSystemController.Mode mode){
        this.c = c;
        this.mode = mode;
    }

    @Override
    public void execute(){
        c.SetMode(mode);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
