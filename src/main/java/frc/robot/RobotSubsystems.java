// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.lang.reflect.Type;
import java.util.ArrayList;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RobotSubsystems {
	ArrayList<MySubsystem> subsystems = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public MySubsystem GetSubsystem(String type) {
		for (MySubsystem subsystem : subsystems) {
			if (subsystem.getClass().getName() == type)
				return subsystem;
		}

		return null;
	}

	public void AddSubsystem(MySubsystem subsystem) {
		subsystems.add(subsystem);
	}

	public void Initialize() {
		for (MySubsystem subsystem : subsystems)
			subsystem.Initialize(this);
	}
}
