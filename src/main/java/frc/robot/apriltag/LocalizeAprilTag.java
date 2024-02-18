package frc.robot.apriltag;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Quaternion;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.drivetrain.DirectVelocityProvider;
import frc.robot.drivetrain.SwerveDrivetrain;
import frc.robot.drivetrain.VelocityProvider;

public class LocalizeAprilTag extends Command {
    private VelocityProvider provider;
    private AprilTagUDPReceiver receiver;

    private boolean best_effort;
    private Translation2d tag_relative_target;
    private int id;

    public LocalizeAprilTag(AprilTagUDPReceiver receiver, SwerveDrivetrain drivetrain, int id,
            Translation2d tag_relative_target, boolean best_effort) {
        provider = new DirectVelocityProvider();
        drivetrain.AddProvider(provider);

        this.receiver = receiver;

        this.id = id;
        this.tag_relative_target = tag_relative_target;
        this.best_effort = best_effort;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        
    }

    @Override
    public boolean isFinished() {
        TagData tag = receiver.GetTag(id);

        if (tag == null && !best_effort)
            return true; // Can't find tag!

        return false;
    }
}
