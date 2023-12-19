package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutopilotCommand extends CommandBase {

    private final AutopilotDriver Driver;
    private final frc.robot.drivetrain.SwerveDrivetrain Drivetrain;
    private final Pose2d Target;

    public AutopilotCommand(Pose2d target) {
        Driver = Robot.c.driver;
        Drivetrain = Robot.c.drivetrain;

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
