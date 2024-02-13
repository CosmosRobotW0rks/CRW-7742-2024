package frc.robot.note_manipulation.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.control.JoystickRequester;

public class HingeJoystickControl extends Command {
    private Hinge hinge;
    private JoystickRequester req;
    private int axis;

    public double min_power = 0;
    public double max_power = 0.25;

    public HingeJoystickControl(Hinge hinge, int axis, JoystickRequester req){
        this.hinge = hinge;
        this.axis = axis;
        this.req = req;

        addRequirements(hinge);
    }

    @Override
    public void execute(){
        double coeff = max_power - min_power;
        double power = req.GetAxis(axis) * coeff + min_power;

        hinge.SetOutput(power);
    }

    @Override
    public void end(boolean interrupted){
        hinge.SetOutput(0);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
