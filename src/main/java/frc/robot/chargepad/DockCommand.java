package frc.robot.chargepad;

import java.util.Date;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class DockCommand extends CommandBase{
    long dockedMillis;

    @Override
    public void initialize(){
        addRequirements(Robot.c.drivetrain);
    }

    @Override
    public void execute(){
        Robot.c.drivetrain.SetSpeed(new Translation2d(2, 0), 0);

        if(Math.abs(Robot.c.imu.tilt_degrees) < 5)
            dockedMillis = new Date().getTime();
    }

    @Override
    public boolean isFinished(){
        return (Math.abs(Robot.c.imu.tilt_degrees) > 5) && (new Date().getTime() - dockedMillis > 250);
    }
}
