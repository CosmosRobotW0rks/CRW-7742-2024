package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;

public class DoAction extends Command {
    private final NoteSystemController c;

    public DoAction(NoteSystemController c) {
        this.c = c;
    }

    @Override
    public void initialize() {
        c.DoAction();
    }

    @Override
    public boolean isFinished() {
        return !c.IsBusy();
    }
}
