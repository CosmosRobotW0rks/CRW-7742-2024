package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.control.JoystickRequester;
import frc.robot.note_manipulation.shooter.Conveyor;
import frc.robot.note_manipulation.shooter.Hinge;

public class HingeJoystickOverride extends Command {
    private Hinge hinge;
    private JoystickRequester req;
    private NoteSystemController c;
    private int axis;

    public double min_power = 0;
    public double max_power = 0.25;
    public double offset = 0.2;

    public HingeJoystickOverride(Hinge hinge, int axis, JoystickRequester req, NoteSystemController c) {
        this.hinge = hinge;

        this.req = req;
        this.c = c;
        this.axis = axis;
    }

    @Override
    public void execute() {
        double coeff = max_power - min_power;
        double power = req.GetAxis(axis) * coeff + min_power;

        if (Math.abs(power) > 10e-6)
            return;
            
        c.SetMode(NoteSystemController.Mode.MANUAL);
        hinge.SetOutput(power + offset);
    }

    @Override
    public void end(boolean interrupted) {
        hinge.SetOutput(0.0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
