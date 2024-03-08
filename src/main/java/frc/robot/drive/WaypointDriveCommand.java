package frc.robot.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.drivetrain.SwerveDrivetrain;

public class WaypointDriveCommand extends Command {

    private final WaypointDriver Driver;
    private final SwerveDrivetrain Drivetrain;
    private final boolean stay;
    private final double x_s;
    private final double y_s;
    private final double precision;
    private final double p_gain;

    private Pose2d Target;

    public WaypointDriveCommand(RobotContainer c, Pose2d target, boolean stay, double trans_speed, double precision,
            double p_gain) {
        Driver = c.auto_driver;
        Drivetrain = c.drivetrain;
        this.stay = stay;

        this.x_s = trans_speed;
        this.y_s = trans_speed;

        this.precision = precision;
        this.p_gain = p_gain;

        addRequirements(Driver);
        addRequirements(Drivetrain);

        Target = target;
    }

    public WaypointDriveCommand(RobotContainer c, Pose2d target, boolean stay, double x_s, double y_s, double precision,
            double p_gain) {
        Driver = c.auto_driver;
        Drivetrain = c.drivetrain;
        this.stay = stay;

        this.x_s = x_s;
        this.y_s = y_s;

        this.precision = precision;
        this.p_gain = p_gain;

        addRequirements(Driver);
        addRequirements(Drivetrain);

        Target = target;
    }

    @Override
    public void initialize() {
        Driver.x_speed = x_s;
        Driver.y_speed = y_s;
        Driver.precision = precision;
        Driver.trans_p_gain = p_gain;
        Driver.Goto(Target);
    }

    @Override
    public void end(boolean interrupted) {
        Driver.Disengage();
    }

    @Override
    public boolean isFinished() {
        if (stay)
            return false;

        return Driver.AtTarget;
    }

    public void SetPose(Pose2d target) {
        Target = target;
    }
}
