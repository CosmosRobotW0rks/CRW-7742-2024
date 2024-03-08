package frc.robot.apriltag;

import edu.wpi.first.wpilibj2.command.Command;

public class LocalizeRobot extends Command {
    AprilTagUDPReceiver r;
    public LocalizeRobot(AprilTagUDPReceiver r){
        this.r = r;
    }

    @Override
    public void initialize() {
        r.LocalizeRobot();
    }

    @Override
    public boolean isFinished() {
        return true;        
    }
}
