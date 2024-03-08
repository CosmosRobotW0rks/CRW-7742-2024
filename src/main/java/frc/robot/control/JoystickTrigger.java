package frc.robot.control;

import java.util.Enumeration;
import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class JoystickTrigger {
    private class NeverTrigger extends Trigger {
        public NeverTrigger() {
            super(new BooleanSupplier() {
                @Override
                public boolean getAsBoolean() {
                    // TODO Auto-generated method stub
                    return false;
                }
            });
        }
    }

    public enum Source {
        NOT_BOUND,
        BUTTON,
        POV
    }

    private Source source;
    private int value;

    public JoystickTrigger() {
        source = Source.NOT_BOUND;
    }

    public JoystickTrigger(int value, Source source) {
        this.value = value;
        this.source = source;
    }

    public Trigger GetTrigger(Joystick joystick) {
        switch (source) {
            case NOT_BOUND:
                return new NeverTrigger();

            case BUTTON:
                return new JoystickButton(joystick, value);

            case POV:
                return new POVButton(joystick, value);
        }

        return new NeverTrigger();
    }
}
