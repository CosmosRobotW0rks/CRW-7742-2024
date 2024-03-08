package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;

public class Shoot extends Command {
    private final NoteSystemController c;
    double rpm;
    double deg;

    public Shoot(NoteSystemController c, double rpm, double deg){
        this.c = c;
        this.rpm = rpm;
    }

    @Override
    public void initialize(){
        c.SetShootRPMTarget(rpm);
        c.SetShootHingeTarget(deg);
        c.Shoot();
        System.out.println("Shooting!");
    }

    @Override
    public boolean isFinished() {
        return !c.IsBusy();
    }
}
