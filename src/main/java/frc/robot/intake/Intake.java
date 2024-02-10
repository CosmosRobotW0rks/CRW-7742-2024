package frc.robot.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.control.JoystickProvider;
import frc.robot.control.JoystickRequester;

public class Intake extends SubsystemBase {
    private JoystickRequester joystick;
    private boolean on;
    //private boolean reverse;

    private double output = 1;

    private VictorSPX front = new VictorSPX(41);
    private VictorSPX back = new VictorSPX(42);

    public void Init(JoystickProvider provider) {
        this.joystick = new JoystickRequester(provider);
    }

    @Override
    public void periodic() {
        //if (joystick.GetButton(4))
            on = false;
        if (joystick.GetButton(3))
            on = true;

        double percent = on ? output : 0;

        front.set(ControlMode.PercentOutput, percent);
        back.set(ControlMode.PercentOutput, -percent);
    }
}
