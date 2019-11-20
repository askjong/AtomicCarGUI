package sdv.communication.TCP;

import sdv.gui.controller.control.sys.ControlSys;

/**
 * Interface for communication the gui is sending "out".
 *
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 12:19
 */
public class TCPCommunication {
    // Socket form main communication.
    private TCPGUIClient tcpClient;
    // Server ip.
    private String ip;
    // Main port.
    private int port;

    /**
     * Creates new socket and defines the addresses.
     */
    public TCPCommunication(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * Sends strings to main controller class.
     *
     * @param str String to send.
     */
    public void sendCommand(String str) {
        this.tcpClient.doWriteString(str.toUpperCase().trim());
    }

    /**
     * Connects main communication class.
     */
    public void connectToTcpServer(ControlSys controlSys) {
        if (null != this.tcpClient) {
            this.tcpClient.doCloseSocket();
            this.tcpClient.interrupt();
        }
        this.tcpClient = new TCPGUIClient(controlSys, this.ip, this.port);
        this.tcpClient.setDaemon(true);
        this.tcpClient.start();
    }

    public void closeSocket() {
        if (null != this.tcpClient)
            this.tcpClient.doCloseSocket();
    }
}