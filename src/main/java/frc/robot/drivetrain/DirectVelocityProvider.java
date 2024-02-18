package frc.robot.drivetrain;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.Timer;

public class DirectVelocityProvider extends VelocityProvider {
    Translation3d vel = new Translation3d(0, 0, 0);
    double last_set_time;

    public void SetVelocity(Translation3d velocity) {
        vel = velocity;
        last_set_time = Timer.getFPGATimestamp();
    }

    public Translation3d GetVelocity() {
        if(Timer.getFPGATimestamp() - last_set_time > 0.25)
            vel = new Translation3d(0, 0, 0);

        return vel;
    }
}