package sdv.communication.UDP;

import sdv.functions.ClassStatus;
import sdv.gui.controller.control.sys.ControlSys;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


/**
 * @author Andreas
 * @version 1.0
 * @since 16.11.2019, 23:41
 */
public abstract class UDPClient {
    // Property change support, reports change in STATUS status.
    private PropertyChangeSupport pcs;
    // Datagram socket.
    private DatagramSocket socket;
    // Classes starting status.
    private String status;
    // Sockets ip.
    private InetAddress ipAddress;
    // Sockets port.
    private int port;
    // The packet size.
    private int packetSize;
    // Objects string id, used for printing errors and fire events.
    protected String objName;


    public UDPClient(ControlSys sys, InetAddress ipAddress, int port, String objName, int packetSize) {
        this.pcs = new PropertyChangeSupport(this);
        this.pcs.addPropertyChangeListener(sys);
        this.status = ClassStatus.isDisconnected();
        this.ipAddress = ipAddress;
        this.port = port;
        this.objName = objName;
        this.packetSize = packetSize;
        doSetupSocket(ipAddress, port);
    }

    /**
     * Creates a socket and connects it.
     *
     * @param ipAddress Ip address for socket to doReconnect to.
     * @param port      Port nr for socket to doReconnect to.
     */
    public void doSetupSocket(InetAddress ipAddress, int port) {
        this.pcs.firePropertyChange(this.objName, this.status, ClassStatus.isLoading());
        this.status = ClassStatus.isLoading();
        doCloseSocket();

        try {
            this.socket = new DatagramSocket(port);
            this.pcs.firePropertyChange(this.objName, this.status, ClassStatus.isConnected());
            this.status = ClassStatus.isConnected();

        } catch (SocketException e) {
            e.printStackTrace();
            this.pcs.firePropertyChange(this.objName, this.status, ClassStatus.isDisconnected());
            this.status = ClassStatus.isDisconnected();
        }

        this.socket.connect(ipAddress, port);
        sendMessage("Start", ipAddress, port);
    }

    /**
     * Reads from dDatagramSocket, returns the data from the packet.
     *
     * @return Data from DatagramSocket.
     */
    public byte[] doReadSocket() {
        byte[] receivedData = new byte[this.packetSize];
        DatagramPacket packet = new DatagramPacket(receivedData, receivedData.length);

        try {
            this.socket.receive(packet);

        } catch (IOException e) {
           e.printStackTrace();
            this.pcs.firePropertyChange(this.objName.toUpperCase() + ":STATUS", this.status, ClassStatus.isDisconnected());
            this.status = ClassStatus.isDisconnected();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return packet.getData();
    }




    /**
     * Sends a message to the UDP server
     *
     * @param msg The message you want to send.
     * @param ipAddress Servers IP address.
     * @param port Servers port number.
     */
    public void sendMessage(String msg, InetAddress ipAddress, int port) {
        try {
            byte[] buf = msg.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, ipAddress, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Closes the socket.
     */
    public void doCloseSocket() {
        if (null != this.socket) {
            this.socket.close();
        }
        this.pcs.firePropertyChange(this.objName, this.status, ClassStatus.isDisconnected());
        this.status = ClassStatus.isDisconnected();
    }
}
