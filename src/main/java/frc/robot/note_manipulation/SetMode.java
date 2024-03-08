package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;

public class SetMode extends Command {
    private final NoteSystemController.Mode mode;
    private final NoteSystemController c;

    double rpm = -1;
    double deg = -1;

    public SetMode(NoteSystemController c, NoteSystemController.Mode mode) {
        this.c = c;
        this.mode = mode;
    }

    public SetMode(NoteSystemController c, NoteSystemController.Mode mode, double rpm, double hinge) {
        this.c = c;
        this.mode = mode;

        this.rpm = rpm;
        this.deg = hinge;
    }

    @Override
    public void initialize() {
        c.SetMode(mode);
        System.out.println("Setting mode RPM:" + rpm);

        if (this.rpm != -1) {
            c.SetShootRPMTarget(rpm);
            c.SetShootHingeTarget(deg);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
