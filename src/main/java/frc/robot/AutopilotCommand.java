package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.AutopilotDriver;

public class AutopilotCommand extends CommandBase{

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
        Driver.tX = true;
        Driver.tY = true;
        Driver.rZ = true;
        
        Driver.Goto(Target);
    }

    @Override
    public void end(boolean interrupted) {
        Driver.Goto(null);
        Drivetrain.SetSpeed(new Translation2d(0, 0), 0);
    }

    @Override
    public boolean isFinished() {
        return Driver.AtTarget;
    }

}
