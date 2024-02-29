package frc.lib;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPReceiver {
    private DatagramSocket sock;
    private double read_rate;

    private byte[] buffer = new byte[4096];
    private DatagramPacket last_packet;

    private boolean timed_out = true;

    public UDPReceiver(int port, double read_rate, double timeout) throws SocketException {
        sock = new DatagramSocket(port);
        sock.setSoTimeout((int) (timeout * 1000));
        this.read_rate = read_rate;
        last_packet = new DatagramPacket(buffer, buffer.length);

        Thread listener_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                listen();
            }
        });

        listener_thread.start();
    }

    public String GetLastData() {
        return new String(last_packet.getData(), 0, last_packet.getLength());
    }

    public boolean HasTimedOut(){
        return timed_out;
    }

    private void listen() {
        try {
            while (true) {
                try {
                    sock.receive(last_packet);
                    timed_out = false;
                    Thread.sleep((long) ((1.0 / read_rate) * 1000));
                } catch (java.net.SocketTimeoutException e) {
                    timed_out = true;
                    // System.out.println("Timed out waiting for data!");
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Listener shutting down...");
        } catch (IOException e) {
            System.out.println("Listener thread encountered unexpected error!");
            e.printStackTrace();
        }
    }
}
