package frc.robot.drive;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.RollingAverage;
import frc.robot.RobotContainer;
import frc.robot.drivetrain.DirectVelocityProvider;
import frc.robot.drivetrain.SwerveDrivetrain;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WaypointDriver extends SubsystemBase {
    public SwerveDrivetrain drivetrain;
    private PIDController xPositionController = new PIDController(3, 0, 0.08);
    private PIDController yPositionController = new PIDController(3, 0, 0.08);
    // private PIDController zRotController = new PIDController(0.25, 0, 0.025);

    private Pose2d TargetPose;
    public boolean AtTarget = false;
    public boolean RotatedToTarget = false;

    public double x_speed = 0.5;
    public double y_speed = 0.5;
    public double rot_speed = 0.03125;
    public double precision = 0.15;
    public double trans_p_gain = 3;

    public DirectVelocityProvider vp = new DirectVelocityProvider();

    public WaypointDriver(SwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    public void Init() {
        drivetrain.AddProvider(vp);
    }

    @Override
    public void periodic() {
        if (TargetPose != null)
            DriveToWaypoint();
        else
            vp.SetVelocity(new Translation3d(0, 0, 0));

        SmartDashboard.putBoolean("Autopilot Active", TargetPose != null);
    }

    public void Goto(Pose2d target) {
        TargetPose = target;
        AtTarget = false;

        Engage();
    }

    public void DriveToWaypoint() {
        xPositionController.setP(trans_p_gain);
        yPositionController.setP(trans_p_gain);

        Pose2d currentPose = drivetrain.GetLocalizedPose();

        double xPwr = xPositionController.calculate(currentPose.getX(), TargetPose.getX());
        double yPwr = yPositionController.calculate(currentPose.getY(), TargetPose.getY());

        xPwr = Math.copySign(Math.min(Math.abs(xPwr), x_speed), xPwr);
        yPwr = Math.copySign(Math.min(Math.abs(yPwr), y_speed), yPwr);

        double xDiff = currentPose.getX() - TargetPose.getX();
        double yDiff = currentPose.getY() - TargetPose.getY();

        double diff = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

        double currentAngle = drivetrain.gyroAngle.getRadians();
        double targetAngle = TargetPose.getRotation().getRadians();

        double rot = currentAngle - targetAngle;// -zRotController.calculate(currentAngle, targetAngle);
        rot = rot % (2 * Math.PI);
        rot = (rot > Math.PI) ? (rot - 2 * Math.PI) : rot;
        rot = (rot < -Math.PI) ? (rot + 2 * Math.PI) : rot;

        double rot_diff = rot;
        rot = Math.abs(rot) >= 0.0125 ? rot : 0;
        rot = rot * 0.125;
        rot = Math.copySign(Math.min(Math.abs(rot), rot_speed), rot);

        RotatedToTarget = Math.abs(rot_diff) <= 0.03125;

        if (diff > precision && Math.abs(rot_diff) <= 0.15)
            vp.SetVelocity(new Translation3d(xPwr, yPwr, -rot));
        else if (rot != 0)
            vp.SetVelocity(new Translation3d(0, 0, -rot));
        else {
            AtTarget = true;
            vp.SetVelocity(new Translation3d(0, 0, 0));
        }
    }

    public boolean Engage() {
        if (TargetPose != null) {
            vp.SetActive(true);
            return true;
        }
        return false;
    }

    public void Disengage() {
        vp.SetActive(false);
        TargetPose = null;
    }
}
