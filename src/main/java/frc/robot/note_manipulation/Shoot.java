package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;

public class Shoot extends Command {
    private final NoteSystemController c;
    public Shoot(NoteSystemController c){
        this.c = c;
    }

    @Override
    public void execute(){
        c.Shoot();
    }

    @Override
    public boolean isFinished() {
        return !c.IsBusy();
    }
}
