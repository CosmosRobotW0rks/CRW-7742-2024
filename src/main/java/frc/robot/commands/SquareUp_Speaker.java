package frc.robot.commands;

import java.util.Optional;

import javax.swing.text.Position;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotConfiguration;
import frc.robot.RobotContainer;
import frc.robot.apriltag.FieldPOIs;
import frc.robot.apriltag.LocalizeRobot;
import frc.robot.apriltag.SourcePOIs;
import frc.robot.drive.LocalizeWaypoint;
import frc.robot.drive.WaypointDriveCommand;

public class SquareUp_Speaker extends SequentialCommandGroup {
    private WaypointDriveCommand go_to;
    public SquareUp_Speaker(RobotContainer c, SquareUp.Position position) {
        Pose2d pose = null;

        switch (position) {
            case NEAR_OR_LEFT:
                pose = FieldPOIs.Speaker_left;
                break;
            case MIDDLE_OR_FRONT:
                pose = FieldPOIs.Speaker_front;
                break;
            case FAR_OR_RIGHT:
                pose = FieldPOIs.Speaker_right;
                break;
        }

        if (pose == null)
            return;

        Command localize = new LocalizeRobot(c.apriltag);
        go_to = new WaypointDriveCommand(c, pose, false, 4,
                0.2, 2);

        addCommands(localize, go_to);
    }

    public void SetPosition(SquareUp.Position position){
                Pose2d pose = null;

        switch (position) {
            case NEAR_OR_LEFT:
                pose = FieldPOIs.Speaker_left;
                break;
            case MIDDLE_OR_FRONT:
                pose = FieldPOIs.Speaker_front;
                break;
            case FAR_OR_RIGHT:
                pose = FieldPOIs.Speaker_right;
                break;
        }

        if (pose == null)
            return;

        go_to.SetPose(pose);
    }
}
