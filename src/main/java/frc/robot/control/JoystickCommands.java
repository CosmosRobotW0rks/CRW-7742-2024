package frc.robot.control;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.RobotConfiguration;
import frc.robot.RobotContainer;
import frc.robot.commands.SquareUp;

public class JoystickCommands {
    private Joystick joystick;
    private JoystickConfiguration conf;
    private RobotContainer c;

    public JoystickCommands() {

    }

    public void Init(Joystick joystick, JoystickConfiguration conf, RobotContainer c) {
        this.joystick = joystick;
        this.conf = conf;
        this.c = c;
        
        new JoystickButton(joystick, conf.AlignButton).whileTrue(new SquareUp(c));
    }
}
