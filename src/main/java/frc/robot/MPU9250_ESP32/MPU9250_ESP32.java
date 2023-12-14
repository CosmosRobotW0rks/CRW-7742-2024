package frc.robot.MPU9250_ESP32;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.SerialPort.WriteBufferMode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;;

public class MPU9250_ESP32 extends SubsystemBase {
    //private SerialPort p = new SerialPort(115200, Port.kUSB1);

    public String state;
    public double degrees;
    public double tilt_degrees;

    String r = "";

    public long lastDashboardUpdateMS;

    public CommandBase SetDegreesCommand(double deg) {
        return this.runOnce(() -> this.SetDegrees(deg)).ignoringDisable(true);
    }

    public CommandBase SetDegreesFromDashboardCommand() {
        return this.runOnce(() -> this.SetDegrees(SmartDashboard.getNumber("Gyro Current Degrees", degrees)))
                .ignoringDisable(true);
    }

    public CommandBase ResetCommand() {
        return this.runOnce(() -> this.Reset()).ignoringDisable(true);
    }

    public MPU9250_ESP32() {
        SetupDashboard();

        //p.setWriteBufferMode(WriteBufferMode.kFlushOnAccess);
    }

    public CalibrateCommand cCommand = new CalibrateCommand();

    public void SetupDashboard() {
        SmartDashboard.putNumber("Gyro Current Degrees", 0);

        SmartDashboard.putData("Set Degrees", SetDegreesFromDashboardCommand());
        SmartDashboard.putData("Set Degrees 0", SetDegreesCommand(0));
        SmartDashboard.putData("Set Degrees 90", SetDegreesCommand(90));
        SmartDashboard.putData("Set Degrees 180", SetDegreesCommand(180));
        SmartDashboard.putData("Set Degrees 270", SetDegreesCommand(270));

        SmartDashboard.putData("Calibrate", cCommand);
        SmartDashboard.putData("Reset", ResetCommand());
    }

    @Override
    public void periodic() {
        /*read();

        long current_ms = new Date().getTime();
        if (current_ms - lastDashboardUpdateMS > 100) {
            SmartDashboard.putNumber("Gyro tilt", tilt_degrees);
            SmartDashboard.putNumber("Gyro angle", degrees);
            SmartDashboard.putString("Gyro state", state);
            lastDashboardUpdateMS = current_ms;
        }*/
    }
    /*
    void read() {
        if (p.getBytesReceived() == 0)
            return;

        r += p.readString();

        if (!r.contains(";"))
            return;

        String[] lines = r.split(";");
        for (int i = 0; i < lines.length - 1; i++)
            processLine(lines[i]);

        r = lines[lines.length - 1];
    }*/

    void processLine(String line) {
        line = line.replaceAll("\\s", "").replaceAll("\n", "");

        String[] tokens = line.split(":");

        if (tokens.length != 2)
            return;

        String key = tokens[0];

        // System.out.println("Key: \"" + key + "\"");
        // System.out.println("Value: \"" + tokens[1] + "\"");
        //System.out.println("Line: \"" + line + "\"");

        if (key.contains("state"))
            state = tokens[1];

        if (key.contains("angle"))
            degrees = Double.parseDouble(tokens[1]);

        if (key.contains("tilt"))
            tilt_degrees = Double.parseDouble(tokens[1]);
    }

    public void SetDegrees(double degrees) {
        //p.writeString("set_angle " + degrees + " ;");
        //p.flush();
    }

    public void Calibrate() {
        //System.out.println("Calibrating gyro");
        //p.writeString("calib ;");
        //p.flush();
    }

    public void Reset() {
        //System.out.println("Resetting gyro");
        //p.writeString("reset ;");
        //p.flush();
    }
}
