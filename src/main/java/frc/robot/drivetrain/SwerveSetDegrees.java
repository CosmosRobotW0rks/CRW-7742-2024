package frc.robot.drivetrain;

import edu.wpi.first.wpilibj2.command.Command;

public class SwerveSetDegrees extends Command {
    SwerveDrivetrain drivetrain;

    double tl;
    double tr;
    double bl;
    double br;

    public SwerveSetDegrees(SwerveDrivetrain drivetrain, double tl, double tr, double bl, double br) {
        this.drivetrain = drivetrain;

        this.tl = tl;
        this.tr = tr;
        this.bl = bl;
        this.br = br;
    }

    @Override
    public void initialize() {
        drivetrain.SetTargetAbsolute(tl, tr, bl, br);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
