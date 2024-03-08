package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.control.JoystickRequester;
import frc.robot.note_manipulation.shooter.Conveyor;

public class ConveyorJoystickOverride extends Command {
    private Conveyor conveyor;
    private JoystickRequester req;
    private NoteSystemController c;
    private int axis;

    public double min_power = 0;
    public double max_power = 0.25;

    public ConveyorJoystickOverride(Conveyor shooter, int axis, JoystickRequester req, NoteSystemController c) {
        this.conveyor = shooter;

        this.req = req;
        this.c = c;
        this.axis = axis;
    }

    @Override
    public void execute() {
        double coeff = max_power - min_power;
        double conveyor_power = req.GetAxis(axis) * coeff + min_power;

        if (Math.abs(conveyor_power) > 10e-6)
            return;

        conveyor.SetPower(conveyor_power);
        c.SetMode(NoteSystemController.Mode.MANUAL);
    }

    @Override
    public void end(boolean interrupted) {
        conveyor.SetPower(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
