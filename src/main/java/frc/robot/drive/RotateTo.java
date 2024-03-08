package frc.robot.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.drivetrain.SwerveDrivetrain;

public class RotateTo extends Command {

    private final WaypointDriver Driver;
    private final SwerveDrivetrain Drivetrain;
    private final double angle;

    public RotateTo(RobotContainer c, double angle) {
        Driver = c.auto_driver;
        Drivetrain = c.drivetrain;
        this.angle = angle;

        addRequirements(Driver);
        addRequirements(Drivetrain);
    }

    @Override
    public void initialize() {
        Driver.Goto(new Pose2d(new Translation2d(), new Rotation2d()));
        Driver.vp.SetEnabledAxes(false, false, true);
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
        Driver.Disengage();
    }

    @Override
    public boolean isFinished() {
        return Driver.RotatedToTarget;
    }

}
