package frc.robot.note_manipulation.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.control.JoystickRequester;

public class HingeJoystickControl extends Command {
    private Hinge hinge;
    private JoystickRequester req;
    private int axis;

    public double min_spd = -0.5;
    public double max_spd = 0.5;

    public HingeJoystickControl(Hinge hinge, int axis, JoystickRequester req) {
        this.hinge = hinge;
        this.axis = axis;
        this.req = req;

        addRequirements(hinge);
    }

    @Override
    public void execute() {
        double coeff = max_spd - min_spd;
        double offset = req.GetAxis(axis) * coeff + min_spd;
        double target = hinge.GetPosition() + offset;
        
        hinge.SetTarget(target);
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
