package sdv.communication.TCP;

import sdv.gui.controller.control.sys.ControlSys;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 12:19
 */
public class TCPGUIClient extends TCPClient {
    // Writer for socket comm.
    private PrintWriter writer;

    /**
     * Initializes with a new socket.
     */
    public TCPGUIClient(ControlSys sys, String ipAddress, int port) {
        super(sys, ipAddress, port, "TCPGUIClient");
    }

    @Override
    public void run() {
        doCloseSocket();
        if (doConnect())
            setPrintWriter();
    }

    /**
     * Setts the PrintWriter field.
     */
    private void setPrintWriter() {
        try {
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException e) {
          e.printStackTrace();
        }
    }

    /**
     * Writes a string trough the socket.
     *
     * @param str String to send trough socket.
     */
    public void doWriteString(String str) {
        if (null != this.writer) {
            this.writer.println(str);
        }
    }
}