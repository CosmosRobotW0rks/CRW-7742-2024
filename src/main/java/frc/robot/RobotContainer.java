// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.MPU9250_ESP32.MPU9250_ESP32;
import frc.robot.control.JoystickDriver;
import frc.robot.drivetrain.SwerveDrivetrain;

//TODO Dynamic subsystem type registration and retrieval
public class RobotContainer {
	public SwerveDrivetrain drivetrain;
	public MPU9250_ESP32 imu;
	public AutopilotDriver driver;

	void Setup(){
		drivetrain = new SwerveDrivetrain();
		imu = new MPU9250_ESP32();
		driver = new AutopilotDriver();

		drivetrain.Setup();
		driver.Init();
	}
}
