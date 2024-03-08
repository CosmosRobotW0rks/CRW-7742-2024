package frc.robot.pneumatics;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.control.JoystickConfiguration;
import frc.robot.control.JoystickProvider;
import frc.robot.control.JoystickRequester;

public class Pneumatics extends SubsystemBase {
    private final Compressor compressor;
    private final Solenoid left;
    private final Solenoid right;

    private JoystickRequester req;
    private JoystickConfiguration conf;

    boolean left_up = false;
    boolean right_up = false;

    public Pneumatics() {
        compressor = new Compressor(8, PneumaticsModuleType.CTREPCM);
        left = new Solenoid(8, PneumaticsModuleType.CTREPCM, 0);
        right = new Solenoid(8, PneumaticsModuleType.CTREPCM, 1);
    }

    public void Init() {
        compressor.enableDigital();
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Compressor running?", compressor.isEnabled());
    }

    public void SetCompressor(boolean state) {
        if (state)
            compressor.enableDigital();
        else
            compressor.disable();
    }

    public void ToggleCompressor() {
        if (compressor.isEnabled())
            compressor.disable();
        else
            compressor.enableDigital();
    }

    public void SetPistons() {
        left_up = !left_up;
        right_up = !right_up;

        left.set(left_up);
        right.set(right_up);
    }

    public void SetPistons(boolean up) {
        left_up = up;
        right_up = up;

        left.set(left_up);
        right.set(right_up);
    }

    public void SetPistons(boolean left_up, boolean right_up) {
        this.left_up = left_up;
        this.right_up = right_up;

        left.set(left_up);
        right.set(right_up);
    }
}
