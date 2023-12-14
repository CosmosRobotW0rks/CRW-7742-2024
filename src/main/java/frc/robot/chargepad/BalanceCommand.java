package frc.robot.chargepad;

import java.util.Date;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class BalanceCommand extends CommandBase {
    //long tiltWaitStartMillis;

    @Override
    public void initialize(){
        addRequirements(Robot.c.drivetrain);
    }

    @Override
    public void execute(){
        double tilt = Robot.c.imu.tilt_degrees;
        double speed_coeff = Math.copySign(Math.min(Math.abs(tilt), 10), tilt);
        Robot.c.drivetrain.SetSpeed(new Translation2d(-0.04 * speed_coeff, 0), 0);

        //if(Math.abs(Robot.c.imu.tilt_degrees) > 5)
            //tiltWaitStartMillis = new Date().getTime();
    }

    @Override
    public boolean isFinished(){
        return false;//tiltWaitStartMillis > 5000;
    }
}
