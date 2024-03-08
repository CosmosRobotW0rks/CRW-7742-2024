package frc.robot.drivetrain;

import java.util.ArrayList;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.drive.RotateTo;

public class SwerveDrivetrain extends SubsystemBase {
    private final double WIDTH = 11.2; // Inches? TODO Add dimensions
    private final double HEIGHT = 10.5; //

    private SwerveModule TL = new SwerveModule(1, 3);
    private SwerveModule TR = new SwerveModule(2, 5);
    private SwerveModule BL = new SwerveModule(8, 7);
    private SwerveModule BR = new SwerveModule(4, 6);
    public AHRS gyro = new AHRS(SPI.Port.kMXP);

    public boolean Enabled = true;

    boolean homed = false;
    private ArrayList<VelocityProvider> velocity_providers = new ArrayList<>();

    double x_spd = 0;
    double y_spd = 0;
    double th_spd = 0;

    double max_x_accel = 15;
    double max_y_accel = 15;
    double max_th_accel = 1;

    double old_time;

    private SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
            new Translation2d(WIDTH, HEIGHT),
            new Translation2d(WIDTH, -HEIGHT),
            new Translation2d(-WIDTH, HEIGHT),
            new Translation2d(-WIDTH, -HEIGHT));

    private Field2d odom_display = new Field2d();

    public void Setup() {
        UpdateModules();
        odometry = new SwerveDriveOdometry(kinematics, gyroAngle, positions);

        odom_display.setRobotPose(new Pose2d(new Translation2d(), new Rotation2d()));
        SmartDashboard.putData("Field", odom_display);
        SmartDashboard.putData("Home", new SwerveSetDegrees(this, 0, 0, 0, 0));
        SmartDashboard.putData("Cross", new SwerveSetDegrees(this, 45, -45, -45, 45));
    }

    public void AddProvider(VelocityProvider p) {
        this.velocity_providers.add(p);
    }

    void Drive() {
        Translation3d combined = new Translation3d();

        for (VelocityProvider p : velocity_providers) {
            combined = new Translation3d(
                    combined.getX() + (p.GetEnabledAxes()[0] ? p.GetVelocity().getX() : 0),
                    combined.getY() + (p.GetEnabledAxes()[1] ? p.GetVelocity().getY() : 0),
                    combined.getZ() + (p.GetEnabledAxes()[2] ? p.GetVelocity().getZ() : 0));
        }
        double new_time = Timer.getFPGATimestamp();
        double delta = new_time - old_time;
        old_time = new_time;

        if (x_spd < combined.getX()) {
            x_spd = x_spd + delta * max_x_accel;
            if (x_spd > combined.getX())
                x_spd = combined.getX();
        }

        if (x_spd > combined.getX()) {
            x_spd = x_spd - delta * max_x_accel;
            if (x_spd < combined.getX())
                x_spd = combined.getX();
        }

        if (y_spd < combined.getY()) {
            y_spd = y_spd + delta * max_y_accel;
            if (y_spd > combined.getY())
                y_spd = combined.getY();
        }

        if (y_spd > combined.getY()) {
            y_spd = y_spd - delta * max_y_accel;
            if (y_spd < combined.getY())
                y_spd = combined.getY();
        }

        ChassisSpeeds fieldOrientedXYSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(-x_spd,
                -y_spd,
                -combined.getZ(),
                gyroAngle); // Gyro is upside down?

        SwerveModuleState[] states = kinematics.toSwerveModuleStates(fieldOrientedXYSpeeds);

        // Set speeds
        TL.Drive(states[0].speedMetersPerSecond);
        TR.Drive(states[1].speedMetersPerSecond);
        BL.Drive(states[2].speedMetersPerSecond);
        BR.Drive(states[3].speedMetersPerSecond);

        if (Math.abs(fieldOrientedXYSpeeds.vxMetersPerSecond) + Math.abs(fieldOrientedXYSpeeds.vyMetersPerSecond)
                + Math.abs(fieldOrientedXYSpeeds.omegaRadiansPerSecond) < 1e-4)
            return;
        // Set angles
        TL.SetTarget(states[0].angle.getDegrees());
        TR.SetTarget(states[1].angle.getDegrees());
        BL.SetTarget(states[2].angle.getDegrees());
        BR.SetTarget(states[3].angle.getDegrees());
    }

    public void SetTargetAbsolute(double tl, double tr, double bl, double br) {
        TL.SetTarget(tl, true);
        TR.SetTarget(tr, true);
        BL.SetTarget(bl, true);
        BR.SetTarget(br, true);
        homed = true;
    }

    public Rotation2d gyroAngle = new Rotation2d();
    SwerveModulePosition[] positions = new SwerveModulePosition[4];
    SwerveModuleState[] states = new SwerveModuleState[4];
    SwerveDriveOdometry odometry;
    public Pose2d OdometryOutPose;

    public Transform2d OdometryOffset = new Transform2d();

    void Odometry() {
        gyroAngle = Rotation2d.fromDegrees(-gyro.getFusedHeading());
        odometry.update(gyroAngle, positions);
        OdometryOutPose = odometry.getPoseMeters();
        OdometryOutPose = new Pose2d(
                new Translation2d(OdometryOutPose.getTranslation().getX(), OdometryOutPose.getTranslation().getY()),
                OdometryOutPose.getRotation());

    }

    public void SetOdomPose(Pose2d pose) {
        // pose = new Pose2d(pose.getTranslation(), rot); // We do NOT want to reset the
        // gyro angle, the NavX is more than
        // enough!
        odometry.resetPosition(gyroAngle, positions, pose);
    }

    public Pose2d GetLocalizedPose() {
        Pose2d pose = OdometryOutPose;
        return pose;
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
        SmartDashboard.putString("Odom Localized",
                ("x: " + GetLocalizedPose().getX() + ", y: " + GetLocalizedPose().getY()));

        SmartDashboard.putNumber("Gyro angle", gyroAngle.getDegrees());
        odom_display.setRobotPose(new Pose2d(OdometryOutPose.getX(), OdometryOutPose.getY() + 5.548, gyroAngle));
    }

    @Override
    public void periodic() {
        UpdateModules();
        Odometry();
        Drive();
        Display();
    }
}
