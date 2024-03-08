package frc.robot.commands;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotConfiguration;
import frc.robot.RobotContainer;
import frc.robot.apriltag.FieldPOIs;
import frc.robot.apriltag.LocalizeRobot;
import frc.robot.drive.LocalizeWaypoint;
import frc.robot.drive.WaypointDriveCommand;

public class SquareUp_ClosestSpeaker extends Command {
    private Command speaker_r;

    private RobotContainer c;

    Command current;

    public SquareUp_ClosestSpeaker(RobotContainer c) {
        this.c = c;
    }

    @Override
    public void initialize() {
        current = null;
    }

    @Override
    public void execute() {
        if (current != null)
            return;

        Pose2d nearest_possible = c.drivetrain.GetLocalizedPose().nearest(new ArrayList<Pose2d>() {
            {
                add(FieldPOIs.Speaker_front);
                add(FieldPOIs.Speaker_left);
                add(FieldPOIs.Speaker_right);
            }
        });

        current = new WaypointDriveCommand(c, nearest_possible, false, 1, 0.2, 3);

        if (current != null)
            current.schedule();
    }

    @Override
    public void end(boolean interrupted) {
        if (current != null)
            current.cancel();
    }

    @Override
    public boolean isFinished() {
        if (current != null)
            return current.isFinished();

        return false;
    }
}
