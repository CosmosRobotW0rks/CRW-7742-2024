package frc.robot.MPU9250_ESP32;

import java.util.Date;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class CalibrateCommand extends CommandBase {
    boolean calibrate_waited = false;
    long prev_sent_ms = 0;
    @Override
    public void initialize() {
        Robot.c.imu.Calibrate();
        Robot.c.imu.state = "calibrating";

        calibrate_waited = false;
        prev_sent_ms = new Date().getTime();
    }

    @Override
    public void execute(){
        if(Robot.c.imu.state.contains("calib_wait"))
            calibrate_waited = true;

        long current_ms = new Date().getTime();
        if(!calibrate_waited && Robot.c.imu.state.contains("ok") && current_ms - prev_sent_ms > 250){
            prev_sent_ms = current_ms;
            Robot.c.imu.Calibrate();
        }
    }

    @Override
    public boolean isFinished() {
        return Robot.c.imu.state.contains("ok") && calibrate_waited;
    }

    @Override
    public boolean runsWhenDisabled(){
        return true;
    }
}
