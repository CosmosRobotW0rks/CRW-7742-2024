// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.MPU9250_ESP32.MPU9250_ESP32;
import frc.robot.control.JoystickDriver;
import frc.robot.control.JoystickProvider;
import frc.robot.drive.AutopilotDriver;
import frc.robot.drivetrain.SwerveDrivetrain;


public class RobotContainer {
	public SwerveDrivetrain drivetrain;
	public AutopilotDriver auto_driver;
	public JoystickDriver main_joy_driver;

	public JoystickProvider main;

	void Setup(Robot robot) {
		drivetrain = new SwerveDrivetrain(this);
		auto_driver = new AutopilotDriver(this);

		drivetrain.Setup();
		auto_driver.Init();

		main = new JoystickProvider(new Joystick(0), 0.05);

		main_joy_driver = new JoystickDriver();
		main_joy_driver.SetActiveProvider(main);
		main_joy_driver.SetConfig(RobotConfiguration.MainJoystick());

		drivetrain.AddProvider(main_joy_driver);

	}
}
