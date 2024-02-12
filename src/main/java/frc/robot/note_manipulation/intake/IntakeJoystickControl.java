package frc.robot.note_manipulation.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.control.JoystickConfiguration;
import frc.robot.control.JoystickRequester;

public class IntakeJoystickControl extends Command {
    private Intake intake;
    private JoystickRequester req;
    private int axis;

    public double min_power = 0;
    public double max_power = 0.25;

    public IntakeJoystickControl(Intake intake, int axis, JoystickRequester req){
        this.intake = intake;
        this.axis = axis;
        this.req = req;

        addRequirements(intake);
    }

    @Override
    public void execute(){
        double coeff = max_power - min_power;
        double power = req.GetAxis(axis) * coeff + min_power;

        intake.SetPower(power);
    }

    @Override
    public void end(boolean interrupted){
        intake.SetPower(0);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
