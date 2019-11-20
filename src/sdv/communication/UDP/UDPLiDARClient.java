package sdv.communication.UDP;

import javafx.scene.chart.XYChart;
import sdv.gui.controller.control.sys.ControlSys;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Reads a DatagramSocket and assembles the data.
 *
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 12:19
 */
public class UDPLiDARClient extends UDPClient {

    /**
     * Creates a socket with ip and port to doReconnect to.
     *
     * @param ipAddress Ip for socket to doReconnect to.
     * @param port      Port to doReconnect to.
     */
    public UDPLiDARClient(InetAddress ipAddress, int port, ControlSys sys) {
        super(sys, ipAddress, port, "UDPLiDARClient", 1440);
    }


    /**
     * Gets data and returns it as a BufferedImage.
     *
     * @return BufferedImage.
     */
    public XYChart.Series<Number, Number> getSeries() {
        return doAssembleData(doReadSocket());
    }


    /**
     * Takes the byte array in and returns a series with rectangular coordinates.
     *
     * @param data the received data from the server
     * @return Image.
     */
    private XYChart.Series<Number, Number> doAssembleData(byte[] data) {
        float[] lidarData = byteToFloat(data);
        return polToRec(lidarData);
    }


    /**
     *
     * @param lidarData
     * @return
     */
    private XYChart.Series<Number,Number> polToRec(float[] lidarData) {
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        for (int i = 0; i < 360; i++) {
            double x = -lidarData[i] * Math.sin(Math.toRadians(i));
            double y = lidarData[i] * Math.cos(Math.toRadians(i));

            series.getData().add(new XYChart.Data<>(x, y));
        }
        return series;
    }

    /**
     * Converts a byte[] to a float[]
     *
     * @param input byte[] of the data you want to convert.
     * @return float[] that has been converted.
     */
    private static float[] byteToFloat(byte[] input) {
        float[] ret = new float[input.length / 4];
        for (int x = 0; x < input.length; x += 4) {
            ret[x / 4] = ByteBuffer.wrap(input, x, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        }
        return ret;
    }
}
