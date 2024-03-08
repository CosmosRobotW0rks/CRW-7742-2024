package frc.robot.apriltag;

import edu.wpi.first.wpilibj2.command.Command;

public class SetConstantLocalization extends Command {
    AprilTagUDPReceiver r;
    boolean constant;
    public SetConstantLocalization(AprilTagUDPReceiver r, boolean on){
        this.r = r;
        this.constant = on;
    }

    @Override
    public void initialize() {
        r.SetConstant(constant);
    }

    @Override
    public boolean isFinished() {
        return true;        
    }
}
