package frc.robot.drive;

import java.net.DatagramSocket;
import java.net.SocketException;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.drivetrain.DirectVelocityProvider;

public class UDPDriver extends SubsystemBase {
    private DirectVelocityProvider vp = new DirectVelocityProvider();
    private DatagramSocket vel_sock;
    private DatagramSocket gyro_sock;

    public UDPDriver(RobotContainer c) throws SocketException {
        vel_sock = new DatagramSocket(11752);
        gyro_sock = new DatagramSocket(11753);

        Thread listener_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listen();
            }
        });

        Thread sender_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                send();
            }
        });

        listener_thread.start();
        sender_thread.start();
    }

    @Override
    public void periodic() {
    }

    private void listen() {
        while(true){

        }
    }

    private void send() {
        while(true){

        }
    }
}
