// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.apriltag.AprilTagUDPReceiver;
import frc.robot.control.JoystickCommands;
import frc.robot.control.JoystickDriver;
import frc.robot.control.JoystickProvider;
import frc.robot.drive.WaypointDriver;
import frc.robot.drivetrain.SwerveDrivetrain;
import frc.robot.leds.LedUDPSender;
import frc.robot.note_manipulation.NoteSystemController;
import frc.robot.note_manipulation.intake.Intake;
import frc.robot.note_manipulation.shooter.Hinge;
import frc.robot.note_manipulation.shooter.Shooter;
import frc.robot.note_manipulation.shooter.Conveyor;
import frc.robot.power.Power;

public class RobotContainer {
	public SwerveDrivetrain drivetrain;
	public WaypointDriver auto_driver;
	public JoystickDriver main_joy_driver;
	public JoystickCommands commands;

	public AprilTagUDPReceiver apriltag;
	public LedUDPSender leds;

	public Power power;

	public Conveyor conveyor;
	public Shooter shooter;
	public Intake intake;
	public Hinge hinge;

	public NoteSystemController note_c;

	public JoystickProvider main;

	void Setup(Robot robot) {
		drivetrain = new SwerveDrivetrain();

		power = new Power();

		Joystick j = new Joystick(0);

		main = new JoystickProvider(j, 0.05);
		main_joy_driver = new JoystickDriver();

		commands = new JoystickCommands();

		apriltag = new AprilTagUDPReceiver(drivetrain);
		leds = new LedUDPSender(robot);

		conveyor = new Conveyor();
		shooter = new Shooter();
		intake = new Intake();
		hinge = new Hinge();

		note_c = new NoteSystemController();

		drivetrain.Setup();
		main_joy_driver.Init(robot, main);

		shooter.Init();
		conveyor.Init(power);

		note_c.Init(this, RobotConfiguration.MainJoystick(), RobotConfiguration.GetNoteSystemConfiguration());
		main_joy_driver.SetConfig(RobotConfiguration.MainJoystick());

		drivetrain.AddProvider(main_joy_driver);
		auto_driver = new WaypointDriver(drivetrain);

		auto_driver.Init();
		leds.Init();

		commands.Init(j, RobotConfiguration.MainJoystick(), this);
	}
}
