package sdv.communication.UDP;

import javafx.scene.chart.ScatterChart;
import javafx.scene.image.ImageView;
import sdv.functions.camera.LiDAR;
import sdv.functions.camera.WebCam;
import sdv.gui.controller.control.sys.ControlSys;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Interface class between camera stream and LiDAR.
 *
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 12:19
 */
public class UDPCommunication {
    // Server ip.
    private String serverIp;
    // Servers port.
    private int serverWebCamPort;
    private int serverLiDARPort;
    // Reads from web-cam server.
    private WebCam webCam;
    // Reads from LiDAR server.
    private LiDAR liDAR;


    /**
     * Initial values.
     */
    public UDPCommunication(String serverIp, int serverWebCamPort, int serverLiDARPort) {
        this.serverIp = serverIp;

        this.serverWebCamPort = serverWebCamPort;
        this.serverLiDARPort = serverLiDARPort;

        this.webCam = null;
        this.liDAR = null;
    }

    /**
     * Initialises the web cam stream and draws images to ImageViewer.
     *
     * @param imageViewer Gui's image viewer, to display video feed.
     */
    public void doStartWebCam(ImageView imageViewer, ControlSys sys) {
        doStopWebCam();

        this.webCam = new WebCam(imageViewer, getInetAddress(this.serverIp), this.serverWebCamPort, sys);
        this.webCam.setDaemon(true);
        this.webCam.start();
    }

    /**
     * Initialises the LiDAR stream and draws images to ScatterChart.
     *
     * @param scatterChart Gui's scatter chart, to display LiDAR feed.
     */
    public void doStartLiDAR(ScatterChart<Number, Number> scatterChart, ControlSys sys) {
        doStopLiDAR();

        this.liDAR = new LiDAR(scatterChart, getInetAddress(this.serverIp), this.serverLiDARPort, sys);
        this.liDAR.setDaemon(true);
        this.liDAR.start();
    }


    /**
     * Stops the webcam server.
     */
    public void doStopWebCam() {
        if (null != this.webCam)
            this.webCam.doStop();
    }

    /**
     * Stops the LiDAR server.
     */
    public void doStopLiDAR() {
        if (null != this.liDAR) {
            this.liDAR.doStop();
        }
    }

    /**
     * @return InetAddress, null if not found.
     */
    private InetAddress getInetAddress(String ip) {
        // Ip for InetAddress server.
        InetAddress ipAddress = null;
        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }
}