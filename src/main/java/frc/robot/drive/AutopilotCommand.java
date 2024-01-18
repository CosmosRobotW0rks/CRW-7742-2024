package frc.robot.drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class AutopilotCommand extends CommandBase {

    private final AutopilotDriver Driver;
    private final frc.robot.drivetrain.SwerveDrivetrain Drivetrain;
    private final Pose2d Target;

    public AutopilotCommand(RobotContainer c, Pose2d target) {
        Driver = c.driver;
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