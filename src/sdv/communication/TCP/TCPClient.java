package sdv.communication.TCP;

import sdv.functions.ClassStatus;
import sdv.gui.controller.control.sys.ControlSys;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

/**
 * Abstract class for a TCP client socket connection.
 *
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 12:19
 */
public abstract class TCPClient extends Thread {
    // Property change support, reports change in STATUS status.
    private PropertyChangeSupport pcs;
    // Objects status.
    private String status;
    // Classes tcp socket.
    protected Socket socket;
    // Sockets ip.
    private String ip;
    // Sockets port.
    private int port;
    // Objects string id, used for printing errors and fire events.
    protected String objName;


    /**
     * Initializes TCP client fields.
     *
     * @param sys  Listener for connection status events.
     * @param ip   Server ip.
     * @param port TCP servers port.
     */
    protected TCPClient(ControlSys sys, String ip, int port, String objName) {
        this.pcs = new PropertyChangeSupport(this);
        this.pcs.addPropertyChangeListener(sys);
        this.status = ClassStatus.isDisconnected();
        this.ip = ip;
        this.port = port;
        this.objName = objName;
    }

    /**
     * Connects the socket to a remote location.
     */
    public boolean doConnect() {
        boolean result = false;

        // Updates classes status.
        this.pcs.firePropertyChange(this.objName, this.status, ClassStatus.isLoading());
        this.status = ClassStatus.isLoading();


        // Connects the socket to remote host.
        try {
            // Connects port.
            this.socket = new Socket(this.ip, this.port);

            // Fires connection status change.
            this.pcs.firePropertyChange(this.objName, this.status, ClassStatus.isConnected());
            this.status = ClassStatus.isConnected();
            result = true;

        } catch (IOException e) {
            e.printStackTrace();

            // Fires connection status change.
            this.pcs.firePropertyChange(this.objName, this.status, ClassStatus.isDisconnected());
            this.status = ClassStatus.isDisconnected();
            result = false;
        }
        return result;
    }

    /**
     * Closes the socket if it is not null.
     */
    public void doCloseSocket() {
        if (null != this.socket) {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Fires an event, the socket is closed.
            this.pcs.firePropertyChange(this.objName, this.status, ClassStatus.isDisconnected());
            this.status = ClassStatus.isDisconnected();
        }
    }

    public boolean isConnected() {
        try {
            this.socket.setSoTimeout(100);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return this.socket.isConnected();
    }
}