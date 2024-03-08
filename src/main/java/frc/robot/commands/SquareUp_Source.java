package frc.robot.commands;

import java.util.Optional;

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

public class SquareUp_Source extends SequentialCommandGroup {
    WaypointDriveCommand go_to;
    public SquareUp_Source(RobotContainer c, SquareUp.Position position) {
        // Command go_to_pre = new WaypointDriveCommand(c, new Pose2d(new
        // Translation2d(0.5, 0), new Rotation2d()), false,
        // RobotConfiguration.CoarseXSpeed, RobotConfiguration.CoarseYSpeed,
        // RobotConfiguration.CoarseLocalizationPrecision, 2);

        Optional<Alliance> alliance = DriverStation.getAlliance();
        if (!alliance.isPresent())
            return;

        Pose2d pose = null;
        SourcePOIs source = null;

        if (alliance.get() == Alliance.Red)
            source = FieldPOIs.RedSource;
        else if (alliance.get() == Alliance.Blue)
            source = FieldPOIs.BlueSource;

        if (source == null)
            return;

        switch (position) {
            case NEAR_OR_LEFT:
                pose = source.near;
                break;
            case MIDDLE_OR_FRONT:
                pose = source.middle;
                break;
            case FAR_OR_RIGHT:
                pose = source.far;
                break;
        }

        if (pose == null)
            return;

        Command localize = new LocalizeRobot(c.apriltag);
        go_to = new WaypointDriveCommand(c, pose, false, 4,
                0.2, 3);

        addCommands(localize, go_to);
    }

    public void SetPosition(SquareUp.Position position) {
        Optional<Alliance> alliance = DriverStation.getAlliance();
        if (!alliance.isPresent())
            return;

        Pose2d pose = null;
        SourcePOIs source = null;

        if (alliance.get() == Alliance.Red)
            source = FieldPOIs.RedSource;
        else if (alliance.get() == Alliance.Blue)
            source = FieldPOIs.BlueSource;

        if (source == null)
            return;

        switch (position) {
            case NEAR_OR_LEFT:
                pose = source.near;
                break;
            case MIDDLE_OR_FRONT:
                pose = source.middle;
                break;
            case FAR_OR_RIGHT:
                pose = source.far;
                break;
        }

        if (pose == null)
            return;
        if(go_to != null)
            go_to.SetPose(pose);
    }
}
