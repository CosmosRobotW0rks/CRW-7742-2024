package frc.robot.leds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotContainer;

public class LedUDPSender extends SubsystemBase {
    private final Robot robot;
    private DatagramSocket sock;
    private ObjectMapper mapper = new ObjectMapper();

    private class LEDData {
        public boolean isEnabled;
        public boolean isAutonomous;

        public double shooterSpeed;
        public boolean shooterAtTarget;

        public boolean executingAction;
        public String mode;

        public boolean apriltagRunning;
    }

    public LedUDPSender(Robot robot) {
        this.robot = robot;
    }

    public void Init() {
        open();
    }

    public void open() {
        while (true) {
            try {
                try {
                    sock = new DatagramSocket(5802);
                    System.out.println("Trying to find host coprocessor...");
                    InetAddress addr = InetAddress.getByName("coprocessor");
                    System.out.println("Found host coprocessor: " + addr.toString() + "!");
                    sock.connect(addr, 5802);
                    return;
                } catch (SocketException e) {
                    System.out.println("Exception while opening LED sender port!");
                } catch (UnknownHostException e) {
                    System.out.println("Could not find host coprocessor!");
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    @Override
    public void periodic() {
        LEDData data = new LEDData();

        data.isEnabled = robot.isEnabled();
        data.isAutonomous = robot.isAutonomousEnabled();

        data.shooterSpeed = robot.c.shooter.GetRPM();
        data.shooterAtTarget = robot.c.shooter.AtTargetRPM();

        data.executingAction = robot.c.note_c.IsBusy();
        data.mode = robot.c.note_c.GetMode().toString();

        data.apriltagRunning = robot.c.apriltag.Connected();

        try {
            byte[] buf = mapper.writeValueAsBytes(data);

            DatagramPacket p = new DatagramPacket(buf, buf.length);
            sock.send(p);
        } catch (IOException e) {
        }
    }
}
