package frc.robot.commands;

import java.util.ArrayList;
import java.util.Arrays;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.apriltag.AprilTagUDPReceiver;

public class SquareUp extends Command {
    private Command speaker;

    private RobotContainer c;

    Command current;

    public SquareUp(RobotContainer c) {
        speaker = new SquareUp_Speaker(c);

        this.c = c;
    }

    @Override
    public void execute() {
        current = null;
        ArrayList<Integer> ids = c.apriltag.GetDetectedIDs();

        for (int id : ids) {
            switch (id) {
                case 3:
                case 4:
                    current = speaker;
                    break;
            }
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

        return true;
    }
}
