package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class ShootModeConstantVelTargetMotion extends Command {
    private NoteSystemController c;
    double vel;

    private double prev_time;

    public ShootModeConstantVelTargetMotion(NoteSystemController c, double vel){
        this.c = c;
        this.vel = vel;
    }

    @Override
    public void initialize() {
        prev_time = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        double current_time = Timer.getFPGATimestamp();
        double delta = current_time - prev_time;
        prev_time = current_time;

        double current_angle = c.GetShootHingeTarget();
        c.SetShootHingeTarget(current_angle + (delta * vel));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
