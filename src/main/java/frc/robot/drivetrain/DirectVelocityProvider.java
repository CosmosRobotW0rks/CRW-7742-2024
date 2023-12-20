package frc.robot.drivetrain;

import edu.wpi.first.math.geometry.Translation3d;

public class DirectVelocityProvider extends VelocityProvider {
    Translation3d vel = new Translation3d(0, 0, 0);

    public void SetVelocity(Translation3d velocity) {
        vel = velocity;
    }

    public Translation3d GetVelocity() {
        return vel;
    }
}