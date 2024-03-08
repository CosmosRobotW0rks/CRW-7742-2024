// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.apriltag.FieldPOIs;
import frc.robot.apriltag.LocalizeRobot;
import frc.robot.apriltag.SetConstantLocalization;
import frc.robot.commands.AggregateAprilTagLocalize;
import frc.robot.commands.Autonomous;
import frc.robot.commands.GoAndShoot;
import frc.robot.commands.LoadNote;
import frc.robot.drive.LocalizeWaypoint;
import frc.robot.drive.RotateTo;
import frc.robot.drive.WaypointDriveCommand;
import frc.robot.note_manipulation.NoteSystemController;
import frc.robot.note_manipulation.SetMode;
import frc.robot.note_manipulation.Shoot;
import frc.robot.note_manipulation.WaitNotBusy;
import frc.robot.pneumatics.SetCompressor;

public class Robot extends TimedRobot {
	public RobotContainer c = new RobotContainer();

	@Override
	public void robotInit() {
		c.Setup(this);

		SmartDashboard.putData("Rotate to home", new RotateTo(c, 0));
	}

	@Override
	public void robotPeriodic() {
		try {
			CommandScheduler.getInstance().run();
		} catch (Exception e) {
			System.out.println(e);
			System.out.println(e.getStackTrace());

		}
	}

	@Override
	public void teleopInit() {
		new SetConstantLocalization(c.apriltag, true).schedule();
		new SetCompressor(c.pneumatics, true);
	}

	@Override
	public void teleopPeriodic() {
	}

	@Override
	public void teleopExit() {
		new SetConstantLocalization(c.apriltag, false).schedule();
	}

	@Override
	public void autonomousInit() {
		// new LocalizeRobot(c.apriltag).andThen(new LoadNote(c, new Pose2d(1, 1.4,
		// Rotation2d.fromDegrees(45)), new Pose2d(1.8, 1.4,
		// Rotation2d.fromDegrees(45)))).andThen(new GoAndShoot(c, 3500)).schedule();
		// new GoAndShoot(c, 3000).schedule();
		// new EngageCommandGroup().schedule();
		try {
			new SetConstantLocalization(c.apriltag, false).andThen(Autonomous.NoteSelf(c)).andThen(Autonomous.NoteCenterline(c)).schedule();// .withTimeout(5)).andThen(Autonomous.NoteRight(c)).schedule();
		} catch (Exception e) {
			System.out.println(e.toString());
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());

		}
	}
}
