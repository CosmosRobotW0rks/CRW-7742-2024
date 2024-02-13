package frc.robot.note_manipulation.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.control.JoystickRequester;

public class ConveyorJoystickControl extends Command {
    private Conveyor shooter;
    private JoystickRequester req;
    private int axis;

    public double min_power = 0;
    public double max_power = 0.25;

    public ConveyorJoystickControl(Conveyor shooter, int axis, JoystickRequester req){
        this.shooter = shooter;
        this.axis = axis;
        this.req = req;

        addRequirements(shooter);
    }

    @Override
    public void execute(){
        double coeff = max_power - min_power;
        double conveyor_power = req.GetAxis(axis) * coeff + min_power;

        shooter.SetPower(conveyor_power);
    }

    @Override
    public void end(boolean interrupted){
        shooter.SetPower(0);
    }

    @Override
    public boolean isFinished(){
        return false;
    }
}
