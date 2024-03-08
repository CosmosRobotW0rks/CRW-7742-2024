package frc.robot.control;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class JoystickProvider extends SubsystemBase {
    private Joystick joystick;
    private double deadzone;

    public ArrayList<JoystickRequester> requesters = new ArrayList<>();

    public JoystickProvider(Joystick joystick, double deadzone) {
        this.joystick = joystick;
        this.deadzone = deadzone;
    }

    public double GetAxisWithDeadzone(int axis) {
        if (axis == -1)
            return 0;
            
        double val = joystick.getRawAxis(axis);
        if (val > deadzone)
            return val - deadzone;
        else if (val < -deadzone)
            return val + deadzone;
        else
            return 0;
    }

    public boolean GetButton(int axis) {
        if (axis == -1)
            return false;

        return joystick.getRawButton(axis);
    }

    public int GetDPad() {
        return joystick.getPOV();
    }

    @Override
    public void periodic() {
        double combined_rumble = 0;
        for (JoystickRequester req : requesters) {
            combined_rumble += req.GetRumble();
        }

        joystick.setRumble(RumbleType.kBothRumble, combined_rumble);
    }
}
