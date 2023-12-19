package frc.robot.drivetrain;

import edu.wpi.first.math.geometry.Translation3d;

public abstract class VelocityProvider {
    public abstract Translation3d GetVelocity();

    boolean x_enabled = false;
    boolean y_enabled = false;
    boolean rot_enabled = false;

    public boolean[] GetEnabledAxes() {
        return new boolean[] { x_enabled, y_enabled, rot_enabled };
    }

    public void SetEnabledAxes(boolean x_enabled, boolean y_enabled, boolean rot_enabled) {
        this.x_enabled = x_enabled;
        this.y_enabled = y_enabled;
        this.rot_enabled = rot_enabled;
    }

    public void SetActive(boolean enabled) {
        x_enabled = enabled;
        y_enabled = enabled;
        rot_enabled = enabled;
    }
}
