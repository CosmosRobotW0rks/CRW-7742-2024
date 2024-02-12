package frc.robot.note_manipulation.shooter;

import edu.wpi.first.wpilibj2.command.Command;

public class ShooterSetRPM extends Command {
    private Shooter shooter;
    private double RPM;

    public enum HoldMode {
        None,
        RPMTarget,
        Indefinite,
    }

    private HoldMode hold;

    public ShooterSetRPM(Shooter shooter, double RPM, HoldMode hold) {
        this.shooter = shooter;
        this.RPM = RPM;
        this.hold = hold;

        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        shooter.SetShooter(RPM);
    }

    @Override
    public void end(boolean interrupted) {
        if (this.hold == HoldMode.Indefinite)
            shooter.SetShooter(0);
    }

    @Override
    public boolean isFinished() {
        if(this.hold == HoldMode.None)
            return true;
        else if(this.hold == HoldMode.RPMTarget)
            return shooter.AtTargetRPM();
        return false;
    }
}
