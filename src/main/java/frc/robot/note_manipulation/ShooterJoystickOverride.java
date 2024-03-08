package frc.robot.note_manipulation;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.control.JoystickRequester;
import frc.robot.note_manipulation.shooter.Shooter;

public class ShooterJoystickOverride extends Command {
    private Shooter shooter;
    private JoystickRequester req;
    private NoteSystemController c;
    private int axis;

    public double min_rpm = 0;
    public double max_rpm = 5600;

    public ShooterJoystickOverride(Shooter shooter, int axis, JoystickRequester req, NoteSystemController c) {
        this.shooter = shooter;

        this.req = req;
        this.c = c;
        this.axis = axis;
    }

    @Override
    public void execute() {
        double coeff = max_rpm - min_rpm;
        double rpm = req.GetAxis(axis) * coeff + min_rpm;

        if (Math.abs(rpm) > 10e-6)
            return;

        shooter.SetShooter(rpm);
        c.SetMode(NoteSystemController.Mode.MANUAL);
    }

    @Override
    public void end(boolean interrupted) {
        shooter.SetShooter(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
