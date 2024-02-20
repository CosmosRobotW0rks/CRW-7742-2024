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
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveDrivetrain extends SubsystemBase {
    private final double WIDTH = 11.2; // Inches? TODO Add dimensions
    private final double HEIGHT = 10.5; //

    private SwerveModule TL = new SwerveModule(1, 3);
    private SwerveModule TR = new SwerveModule(2, 5);
    private SwerveModule BL = new SwerveModule(8, 7);
    private SwerveModule BR = new SwerveModule(4, 6);
    public AHRS gyro = new AHRS(SPI.Port.kMXP);

    public boolean Enabled = true;

    private ArrayList<VelocityProvider> velocity_providers = new ArrayList<>();

    private SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
            new Translation2d(WIDTH, HEIGHT),
            new Translation2d(WIDTH, -HEIGHT),
            new Translation2d(-WIDTH, HEIGHT),
            new Translation2d(-WIDTH, -HEIGHT));

    private Field2d odom_display = new Field2d();

    public void Setup() {
        UpdateModules();
        odometry = new SwerveDriveOdometry(kinematics, gyroAngle, positions);

        SmartDashboard.putData("Odom", odom_display);
    }

    public void AddProvider(VelocityProvider p) {
        this.velocity_providers.add(p);
    }

    void Drive() {
        Translation3d combined = new Translation3d();
        Translation3d combined_non_field_oriented = new Translation3d();

        for (VelocityProvider p : velocity_providers) {
            if (p.field_oriented)
                combined = new Translation3d(
                        combined.getX() + (p.GetEnabledAxes()[0] ? p.GetVelocity().getX() : 0),
                        combined.getY() + (p.GetEnabledAxes()[1] ? p.GetVelocity().getY() : 0),
                        combined.getZ() + (p.GetEnabledAxes()[2] ? p.GetVelocity().getZ() : 0));
            else
                combined_non_field_oriented = new Translation3d(
                        combined.getX() + (p.GetEnabledAxes()[0] ? p.GetVelocity().getX() : 0),
                        combined.getY() + (p.GetEnabledAxes()[1] ? p.GetVelocity().getY() : 0),
                        combined.getZ() + (p.GetEnabledAxes()[2] ? p.GetVelocity().getZ() : 0));
        }

        ChassisSpeeds fieldOrientedXYSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(-combined.getX(),
                combined.getY(),
                combined.getZ(),
                gyroAngle); // Gyro is upside down?

        ChassisSpeeds total = new ChassisSpeeds(
                fieldOrientedXYSpeeds.vxMetersPerSecond + combined_non_field_oriented.getX(),
                fieldOrientedXYSpeeds.vyMetersPerSecond + combined_non_field_oriented.getY(),
                fieldOrientedXYSpeeds.omegaRadiansPerSecond - combined_non_field_oriented.getZ());

        SwerveModuleState[] states = kinematics.toSwerveModuleStates(total);
        // Set angles
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

    public Transform2d OdometryOffset = new Transform2d();

    void Odometry() {
        gyroAngle = Rotation2d.fromDegrees(-gyro.getFusedHeading());
        odometry.update(gyroAngle, positions);
        OdometryOutPose = odometry.getPoseMeters();
        OdometryOutPose = new Pose2d(
                new Translation2d(OdometryOutPose.getTranslation().getX(), -OdometryOutPose.getTranslation().getY()),
                OdometryOutPose.getRotation());

    }

    public void SetOdomPose(Pose2d pose, Rotation2d rot) {
        //pose = new Pose2d(pose.getTranslation(), rot); // We do NOT want to reset the gyro angle, the NavX is more than
                                                       // enough!
        odometry.resetPosition(rot, positions, pose);
    }

    public Pose2d GetLocalizedPose() {
        Pose2d pose = OdometryOutPose.transformBy(OdometryOffset);
        SmartDashboard.putString("Robot loc", "x: " + pose.getX() + ", y: " + pose.getY());
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
        double x_formatted = Math.abs(OdometryOutPose.getX()) > 0.001 ? Math.floor(OdometryOutPose.getX() * 1000) / 1000
                : 0;
        double Y_formatted = Math.abs(OdometryOutPose.getY()) > 0.001 ? Math.floor(OdometryOutPose.getY() * 1000) / 1000
                : 0;

        SmartDashboard.putString("Odom Pose", ("x: " + x_formatted + ", y: " + Y_formatted));

                SmartDashboard.putString("Odom Localized", ("x: " + GetLocalizedPose().getX() + ", y: " + GetLocalizedPose().getY()));

        SmartDashboard.putNumber("Gyro angle", gyroAngle.getDegrees());
        odom_display.setRobotPose(GetLocalizedPose());
    }

    @Override
    public void periodic() {
        UpdateModules();
        Odometry();
        Drive();
        Display();
    }
}
