package frc.robot.drivetrain;

import edu.wpi.first.math.kinematics.*; //TODO Odometry
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.ArrayList;

import frc.robot.Robot;
import frc.robot.RobotContainer;

//TODO Add velocity provider
public class SwerveDrivetrain extends SubsystemBase implements AutoCloseable {
    private final double WIDTH = 11.2; // Inches? TODO Add dimensions
    private final double HEIGHT = 10.5; //

    private SwerveModule TL = new SwerveModule(2, 1);
    private SwerveModule TR = new SwerveModule(4, 6);
    private SwerveModule BL = new SwerveModule(7, 5);
    private SwerveModule BR = new SwerveModule(8, 3);
    // public AHRS gyro = new AHRS(SerialPort.Port.kUSB1);

    public boolean Enabled = true;

    private RobotContainer rbt;

    private Translation3d current_velocity;
    private ArrayList<VelocityProvider> velocity_providers = new ArrayList<>();

    private SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
            new Translation2d(WIDTH, HEIGHT),
            new Translation2d(WIDTH, -HEIGHT),
            new Translation2d(-WIDTH, HEIGHT),
            new Translation2d(-WIDTH, -HEIGHT));

    public SwerveDrivetrain() {
        rbt = Robot.c;
    }

    public void Setup() {
        // gyro.calibrate();
        UpdateModules();
        odometry = new SwerveDriveOdometry(kinematics, gyroAngle, positions);
    }

    public void AddProvider(VelocityProvider p) {
        this.velocity_providers.add(p);
    }

    public Translation3d GetCurrentVelocity(){
        return current_velocity;
    }

    void Drive() {
        Translation3d combined = new Translation3d();
        for (VelocityProvider p : velocity_providers)
            combined = new Translation3d(
                    combined.getX() + (p.GetEnabledAxes()[0] ? p.GetVelocity().getX() : 0),
                    combined.getY() + (p.GetEnabledAxes()[1] ? p.GetVelocity().getY() : 0),
                    combined.getZ() + (p.GetEnabledAxes()[2] ? p.GetVelocity().getZ() : 0));

        current_velocity = combined;

        ChassisSpeeds fieldOrientedXYSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(-combined.getX(),
                combined.getY(),
                combined.getZ(),
                Rotation2d.fromDegrees(-rbt.imu.degrees/*-gyro.getFusedHeading()*/)); // Gyro is upside down? TODO
                                                                                      // Invert gyro properly

        SwerveModuleState[] states = kinematics.toSwerveModuleStates(fieldOrientedXYSpeeds);
        // Set angles
        // TODO This could be refactored
        TL.SetTarget(states[0].angle.getDegrees());
        TR.SetTarget(states[1].angle.getDegrees());
        BL.SetTarget(states[2].angle.getDegrees());
        BR.SetTarget(states[3].angle.getDegrees());

        // Set speeds
        TL.Drive(states[0].speedMetersPerSecond);
        TR.Drive(states[1].speedMetersPerSecond);
        BL.Drive(states[2].speedMetersPerSecond);
        BR.Drive(states[3].speedMetersPerSecond);
    }

    public void Home() {
        TL.SetTarget(0, true);
        TR.SetTarget(0, true);
        BL.SetTarget(0, true);
        BR.SetTarget(0, true);

        Drive();
    }

    public Rotation2d gyroAngle = new Rotation2d();
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    SwerveModuleState[] states = new SwerveModuleState[4];
    SwerveDriveOdometry odometry;
    public Pose2d OdometryOutPose;

    void Odometry() {
        gyroAngle = Rotation2d.fromDegrees(0/*-gyro.getFusedHeading()*/);
        odometry.update(gyroAngle, positions);
        OdometryOutPose = odometry.getPoseMeters();
        OdometryOutPose = new Pose2d(
                new Translation2d(OdometryOutPose.getTranslation().getX(), -OdometryOutPose.getTranslation().getY()),
                OdometryOutPose.getRotation());
    }

    public void SetOdomPose(Pose2d pose, Rotation2d rot) {
        odometry.resetPosition(rot, positions, pose);
    }

    void UpdateModules() {
        TL.Update(0.02);
        TR.Update(0.02);
        BL.Update(0.02);
        BR.Update(0.02);

        positions[0] = TL.GetPosition();
        positions[1] = TR.GetPosition();
        positions[2] = BL.GetPosition();
        positions[3] = BR.GetPosition();

        states[0] = TL.GetState();
        states[1] = TR.GetState();
        states[2] = BL.GetState();
        states[3] = BR.GetState();
    }

    void Display() {
        double x_formatted = Math.abs(OdometryOutPose.getX()) > 0.001 ? Math.floor(OdometryOutPose.getX() * 1000) / 1000
                : 0;
        double Y_formatted = Math.abs(OdometryOutPose.getY()) > 0.001 ? Math.floor(OdometryOutPose.getY() * 1000) / 1000
                : 0;

        SmartDashboard.putString("Odom Pose", ("x: " + x_formatted + ", y: " + Y_formatted));
        SmartDashboard.putNumber("Gyro angle", gyroAngle.getDegrees());
    }

    @Override
    public void periodic() {
        UpdateModules();
        Odometry();
        Drive();
        Display();
    }

    @Override
    public void close() throws Exception {
        TL.close();
        TR.close();
        BL.close();
        BR.close();

        velocity_providers.clear();
    }
}
