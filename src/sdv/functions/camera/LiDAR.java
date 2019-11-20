package sdv.functions.camera;

import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import sdv.communication.UDP.UDPLiDARClient;
import sdv.functions.updater.ScatterUpdater;
import sdv.gui.controller.control.sys.ControlSys;

import java.net.InetAddress;

/**
 * Gets Image from UDP based UdpScatterChartClientOld and sends it to ImageUpdater to be displayed.
 *
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 12:19
 */
public class LiDAR extends Thread {
    // Read from datagram socket.
    private UDPLiDARClient udpLiDARClient;
    // Handles the image.
    private ScatterUpdater scatterUpdater;
    // True to stop reading, false to continue.
    private boolean stop;

    /**
     * Connects to a UDP cam server with ip address and port, and setups imageViewer.
     *
     * @param scatterChart Image view to display image.
     * @param ipAddress    Ip address for server to read from.
     * @param port         Port for server to doReconnect to.
     */
    public LiDAR(ScatterChart<Number, Number> scatterChart, InetAddress ipAddress, int port, ControlSys sys) {
        this.udpLiDARClient = new UDPLiDARClient(ipAddress, port, sys);
        this.scatterUpdater = new ScatterUpdater(scatterChart);
        this.stop = false;
    }

    /**
     * Loop where picture is read from DatagramSocket and displayed to ImageView.
     */
    public void run() {
        while (!this.stop) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Gets series.
            XYChart.Series<Number, Number> series = this.udpLiDARClient.getSeries();
            // Draw's the image.
            this.scatterUpdater.updateScatter(series);
        }
        // Close socket before thread is closed.
        this.udpLiDARClient.doCloseSocket();
    }


    /**
     * Changes flag to stop loop and close thread.
     */
    public void doStop() {
        this.stop = true;
    }
}