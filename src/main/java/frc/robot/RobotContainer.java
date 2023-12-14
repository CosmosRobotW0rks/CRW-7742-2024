// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.MPU9250_ESP32.MPU9250_ESP32;
import frc.robot.arm.ArmControl;
import frc.robot.control.JoystickDriver;
import frc.robot.drivetrain.SwerveDrivetrain;

public class RobotContainer {
	public SwerveDrivetrain drivetrain;
	public JoystickDriver joystick;
	public ArmControl arm;
	public MPU9250_ESP32 imu;
	public AutopilotDriver driver;

	void Setup(){
		drivetrain = new SwerveDrivetrain();
		joystick = new JoystickDriver();
		arm = new ArmControl();
		imu = new MPU9250_ESP32();
		driver = new AutopilotDriver();

		drivetrain.Setup();
		driver.Init();
	}
}
