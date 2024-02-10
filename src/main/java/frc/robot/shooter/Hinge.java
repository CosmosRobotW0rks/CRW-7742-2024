package frc.robot.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.control.JoystickProvider;
import frc.robot.control.JoystickRequester;

public class Hinge extends SubsystemBase {
    private JoystickRequester joystick;

    private double upwards_hold_output = 0.2;
    private double downwards_hold_output = -0.1;

    private double upwards_output = 0.2;
    private double downwards_output = -0.5;

    private boolean hold = false;
    private boolean hold_btn_held = false;

    private VictorSPX motor = new VictorSPX(51);

    public void Init(JoystickProvider provider) {
        this.joystick = new JoystickRequester(provider);
    }

    @Override
    public void periodic() {
        double out = hold ? 0.2 : 0;
        if (joystick.GetDPad() == 0)
            out += hold ? upwards_hold_output : upwards_output;
        else if (joystick.GetDPad() == 180)
            out += hold ? downwards_hold_output : downwards_output;

        if (joystick.GetDPad() == 270) {
            if (!hold_btn_held)
                hold = !hold;
            hold_btn_held = true;
        } else
            hold_btn_held = false;

        motor.set(ControlMode.PercentOutput, -out);
    }
}
