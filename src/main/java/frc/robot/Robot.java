// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.control.JoystickDriver;
import frc.robot.drivetrain.VelocityProvider;

public class Robot extends TimedRobot {
	public static RobotContainer c = new RobotContainer();

	@Override
	public void robotInit() {
		c.Setup();

		c.drivetrain.AddProvider(new JoystickDriver(new Joystick(0), RobotConfiguration.JoystickA()));
	}

	@Override
	public void robotPeriodic() {
		CommandScheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
	}

	@Override
	public void teleopPeriodic() {
	}

	@Override
	public void teleopExit() {
	}

	@Override
	public void autonomousInit(){
		//new EngageCommandGroup().schedule();
	}
}
