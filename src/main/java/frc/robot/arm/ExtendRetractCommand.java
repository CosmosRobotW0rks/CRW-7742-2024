package frc.robot.arm;

import java.util.Date;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;

public class ExtendRetractCommand extends CommandBase{
    long start_ms;

    @Override
    public void initialize(){
        start_ms = new Date().getTime();

    }
}
