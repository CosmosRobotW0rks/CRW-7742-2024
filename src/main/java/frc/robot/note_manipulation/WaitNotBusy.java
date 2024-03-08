package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;

public class WaitNotBusy extends Command {
    private final NoteSystemController c;
    public WaitNotBusy(NoteSystemController c){
        this.c = c;
    }

    @Override
    public boolean isFinished() {
        return !c.IsBusy();
    }
}