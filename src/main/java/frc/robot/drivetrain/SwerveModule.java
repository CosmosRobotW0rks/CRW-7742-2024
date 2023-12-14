package frc.robot.drivetrain;

import java.sql.Time;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveModule {
    private CANSparkMax AngleSpark;
    private CANSparkMax DriveSpark;
    private RelativeEncoder AngleEncoder;
    private RelativeEncoder DriveEncoder;

    private SparkMaxPIDController angleController;
    private SparkMaxPIDController driveController;  

    private final double SPEED_CALIB_VALUE = 0.33 / 2; // software value / real value

    private double distanceDriven;

    private double anglePID_P = 0.5;
    private double anglePID_I = 0;
    private double anglePID_D = 0.05;

    private double drivePID_P = 0.01;
    private double drivePID_I = 0;
    private double drivePID_D = 0.01;

    Time lastUpdaTime;

    public SwerveModule(int AngleCANID, int DriveCANID) {
        AngleSpark = new CANSparkMax(AngleCANID, CANSparkMaxLowLevel.MotorType.kBrushless);
        DriveSpark = new CANSparkMax(DriveCANID, CANSparkMaxLowLevel.MotorType.kBrushless);

        AngleEncoder = AngleSpark.getEncoder();
        AngleEncoder.setPositionConversionFactor(2.0 * Math.PI / (18 / 1));

        DriveEncoder = DriveSpark.getEncoder();

        angleController = AngleSpark.getPIDController();
        angleController.setP(anglePID_P);
        angleController.setI(anglePID_I);
        angleController.setD(anglePID_D);
        angleController.setOutputRange(-0.5, 0.5);

        // AngleSpark.setSmartCurrentLimit(limit)

        driveController = DriveSpark.getPIDController();
        driveController.setP(drivePID_P);
        driveController.setI(drivePID_I);
        driveController.setD(drivePID_D);
    }

    double TargetAngle = 0;
    boolean inverted = false;

    public void Drive(double Speed) {
        driveController.setReference(Speed * 250.0 * SPEED_CALIB_VALUE * (inverted ? -1 : 1), ControlType.kVelocity);
    }

    public double GetAngle() {
        return AngleEncoder.getPosition();
    }

    public double GetShortestRoute(double a1, double a2) {
        // Clamp currentAngle
        double a1_clamped = a1 % (2.0 * Math.PI);
        a1_clamped += a1_clamped < 0 ? 2.0 * Math.PI : 0;

        // Find shortest route
        double nt = a2 + a1 - a1_clamped;
        if (a2 - a1_clamped > Math.PI)
            nt -= 2.0 * Math.PI;
        if (a2 - a1_clamped < -Math.PI)
            nt += 2.0 * Math.PI;

        return nt;
    }

    public void SetTarget(double angle, boolean forceForward) {
        double currentAngle = AngleEncoder.getPosition();
        double currentAngleClamped = currentAngle % (2.0 * Math.PI);
        currentAngleClamped += currentAngleClamped < 0 ? 2.0 * Math.PI : 0;

        double t = Math.toRadians(angle);
        double t1 = GetShortestRoute(currentAngle, t);
        double t2 = GetShortestRoute(currentAngle, t > Math.PI ? t - Math.PI : t + Math.PI);

        double diff1 = Math.abs(t1 - currentAngle);
        double diff2 = Math.abs(t2 - currentAngle);

        double newTarget;

        if (!forceForward && diff1 > diff2) {
            TargetAngle = t > Math.PI ? t - Math.PI : t + Math.PI;
            newTarget = t2;
            inverted = false;
        } else {
            TargetAngle = t;
            newTarget = t1;
            inverted = true;
        }

        angleController.setReference(newTarget, ControlType.kPosition);
    }

    public void SetTarget(double angle) {
        SetTarget(angle, false);
    }

    public double GetSpeed() {
        return DriveEncoder.getVelocity() / 250.0 / SPEED_CALIB_VALUE;
    }

    public void Update(double deltaTimeSeconds) {
        distanceDriven += GetSpeed() * deltaTimeSeconds;
    }

    public SwerveModulePosition GetPosition() {
        return new SwerveModulePosition(distanceDriven, Rotation2d.fromRadians(GetAngle()));
    }

    public SwerveModuleState GetState() {
        return new SwerveModuleState(GetSpeed(), Rotation2d.fromRadians(GetAngle()));
    }
}