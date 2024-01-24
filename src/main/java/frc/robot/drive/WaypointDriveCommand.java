package frc.robot.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.drivetrain.SwerveDrivetrain;

public class WaypointDriveCommand extends Command {

    private final WaypointDriver Driver;
    private final SwerveDrivetrain Drivetrain;
    private final Pose2d Target;

    public WaypointDriveCommand(RobotContainer c, Pose2d target) {
        Driver = c.auto_driver;
        Drivetrain = c.drivetrain;

        addRequirements(Driver);
        addRequirements(Drivetrain);

        Target = target;
    }

    @Override
    public void initialize() {
        Driver.Goto(Target);
    }

    @Override
    public void end(boolean interrupted) {
        Driver.Disengage();
    }

    @Override
    public boolean isFinished() {
        return Driver.AtTarget;
    }

}
