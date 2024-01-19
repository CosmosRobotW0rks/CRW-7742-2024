// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.MPU9250_ESP32.MPU9250_ESP32;
import frc.robot.control.JoystickDriver;
import frc.robot.drive.AutopilotDriver;
import frc.robot.drivetrain.SwerveDrivetrain;


public class RobotContainer {
	public SwerveDrivetrain drivetrain;
	public AutopilotDriver driver;

	public JoystickDriver joy_a;

	void Setup(Robot robot) {
		drivetrain = new SwerveDrivetrain(this);
		driver = new AutopilotDriver(this);

		drivetrain.Setup();
		driver.Init();

		joy_a = new JoystickDriver(robot, new Joystick(0), RobotConfiguration.JoystickA());
		drivetrain.AddProvider(joy_a);
	}
}
