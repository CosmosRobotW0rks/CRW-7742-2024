package frc.robot;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.drivetrain.DirectVelocityProvider;
import frc.robot.drivetrain.SwerveDrivetrain;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutopilotDriver extends SubsystemBase {
    public static SwerveDrivetrain drivetrain;
    private PIDController xPositionController = new PIDController(6, 0, 0.8);
    private PIDController yPositionController = new PIDController(6, 0, 0.8);
    private PIDController zRotController = new PIDController(0.25, 0, 0.025);

    private Pose2d TargetPose;
    public boolean AtTarget = false;

    public boolean tX = true;
    public boolean tY = true;
    public boolean rZ = true;


    public DirectVelocityProvider vp = new DirectVelocityProvider();

    public void Init() {
        drivetrain = Robot.c.drivetrain;
        drivetrain.AddProvider(vp);
    }

    @Override
    public void periodic() {
        if (TargetPose != null)
            DriveToWaypoint();

        SmartDashboard.putBoolean("Autopilot Active", TargetPose != null);
    }

    public void Goto(Pose2d target) {
        TargetPose = target;
        AtTarget = false;
    }

    public void DriveToWaypoint() {
        Pose2d currentPose = drivetrain.OdometryOutPose;

        double xPwr = -xPositionController.calculate(currentPose.getX(), TargetPose.getX());
        double yPwr = yPositionController.calculate(currentPose.getY(), TargetPose.getY());

        xPwr = Math.copySign(Math.min(Math.abs(xPwr), 0.2), xPwr);
        yPwr = Math.copySign(Math.min(Math.abs(yPwr), 0.2), yPwr);

        double xDiff = currentPose.getX() - TargetPose.getX();
        double yDiff = currentPose.getY() - TargetPose.getY();
        double diff = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

        //double currentAngle = drivetrain.gyroAngle.getRadians();
        //double targetAngle = TargetPose.getRotation().getRadians();

        double rot = 0;//-zRotController.calculate(currentAngle, targetAngle);
        rot = Math.copySign(Math.min(Math.abs(rot), 0.05), rot);
        rot = Math.abs(rot) > 0.025 ? rot : 0;

        xPwr = tX ? xPwr : 0;
        yPwr = tY ? xPwr : 0;
        rot = rZ ? rot : 0;

        if (diff > 0.025 && rot <= 0.45)
            vp.SetVelocity(new Translation3d(xPwr, yPwr, rot));
        else if (rot != 0)
            vp.SetVelocity(new Translation3d(0, 0, rot));
        else{
            AtTarget = true;
            vp.SetVelocity(new Translation3d(0, 0, 0));
        }
    }
}
