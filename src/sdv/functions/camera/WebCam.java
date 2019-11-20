package sdv.functions.camera;

import javafx.scene.image.ImageView;
import sdv.communication.UDP.UDPWebCamClient;
import sdv.functions.updater.ImageUpdater;
import sdv.gui.controller.control.sys.ControlSys;

import java.awt.image.BufferedImage;
import java.net.InetAddress;

/**
 * Gets Image from UDP based UdpWebCamClientOld and sends it to ImageUpdater to be displayed.
 *
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 12:19
 */
public class WebCam extends Thread {
    // Read from datagram socket.
    private UDPWebCamClient udpWebCamClient;
    // Handles the image.
    private ImageUpdater imageUpdater;
    // True to stop reading, false to continue.
    private boolean stop;

    /**
     * Connects to a UDP cam server with ip address and port, and setups imageViewer.
     *
     * @param imageView Image view to display image.
     * @param ipAddress Ip address for server to read from.
     * @param port      Port for server to doReconnect to.
     */
    public WebCam(ImageView imageView, InetAddress ipAddress, int port, ControlSys sys) {
        this.udpWebCamClient = new UDPWebCamClient(ipAddress, port, sys);
        this.imageUpdater = new ImageUpdater(imageView);
        this.stop = false;
    }

    /**
     * Loop where picture is read from DatagramSocket and displayed to ImageView.
     */
    public void run() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (!this.stop) {
            // Gets image.
            BufferedImage img = this.udpWebCamClient.getImage();
            // Draw's the image.
            if (img != null) {
                this.imageUpdater.updateImage(img);
            }
        }
        // Clears the imageView.
        this.imageUpdater.doClear();
        // Close socket before thread is closed.
        this.udpWebCamClient.doCloseSocket();
    }

    /**
     * Changes flag to stop loop and close thread.
     */
    public void doStop() {
        this.stop = true;
    }
}