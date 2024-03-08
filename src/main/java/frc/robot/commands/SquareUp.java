package frc.robot.commands;

import java.util.ArrayList;
import java.util.Arrays;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.apriltag.AprilTagUDPReceiver;
import frc.robot.apriltag.FieldPOIs;
import frc.robot.apriltag.LocalizeRobot;

public class SquareUp extends Command {
    private SquareUp_Speaker speaker;
    private SquareUp_Source source;

    private RobotContainer c;
    private FieldPOIs pois;

    public enum Position {
        NEAR_OR_LEFT,
        MIDDLE_OR_FRONT,
        FAR_OR_RIGHT,
    }

    Command current;

    public SquareUp(RobotContainer c) {
        speaker = new SquareUp_Speaker(c, Position.MIDDLE_OR_FRONT);
        source = new SquareUp_Source(c, Position.MIDDLE_OR_FRONT);

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

        ArrayList<Integer> ids = c.apriltag.GetDetectedIDs();

        for (int id : ids) {
            boolean found = false;
            switch (id) {
                case 3:
                case 4:
                    current = speaker;
                    found = true;
                    break;

                case 9:
                case 10:
                    current = source;
                    found = true;
                    break;
            }

            if (found)
                break;
        }

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

    public void SetPosition(Position position) {
        speaker.SetPosition(position);
        source.SetPosition(position);

        SmartDashboard.putString("SquareUp Mode", position.toString());
    }
}
