package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.apriltag.AprilTagUDPReceiver;
import frc.robot.drivetrain.SwerveDrivetrain;

public class AggregateAprilTagLocalize extends Command {
    AprilTagUDPReceiver rcv;
    SwerveDrivetrain dtr;

    double x_acc;
    double y_acc;
    int readings;

    public AggregateAprilTagLocalize(AprilTagUDPReceiver rcv, SwerveDrivetrain dtr) {
        this.rcv = rcv;
        this.dtr = dtr;
    }

    @Override
    public void initialize() {
        x_acc = 0;
        y_acc = 0;
        readings = 0;
    }

    @Override
    public void execute() {
        Pose3d pose = rcv.GetRobotPose();

        if (pose == null)
            return;

        Pose2d pose2d = pose.toPose2d();

        x_acc += pose2d.getX();
        y_acc += pose2d.getY();
        readings += 1;
    }

    @Override
    public void end(boolean interrupted) {
        if (readings == 0) {
            System.out.println("Cannot localize! No tags have been recognized!");
            return;
        }

        x_acc /= readings;
        y_acc /= readings;

        if (Double.isNaN(x_acc) || Double.isNaN(y_acc)) {
            System.out.println("Cannot localize! Pose value is NaN!");
            return;
        }

        dtr.SetOdomPose(new Pose2d(x_acc, y_acc, new Rotation2d()));
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
