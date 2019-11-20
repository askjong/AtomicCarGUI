package sdv.communication.UDP;

import sdv.gui.controller.control.sys.ControlSys;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Reads a DatagramSocket and assembles the data.
 *
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 12:19
 */
public class UDPWebCamClient extends UDPClient {

    /**
     * Creates a socket with ip and port to doReconnect to.
     *
     * @param ipAddress Ip for socket to doReconnect to.
     * @param port      Port to doReconnect to.
     */
    public UDPWebCamClient(InetAddress ipAddress, int port, ControlSys sys) {
        super(sys, ipAddress, port, "UDPWebCamClient", (int) Math.pow(2, 16));
    }


    /**
     * Gets data and returns it as a BufferedImage.
     *
     * @return BufferedImage.
     */
    public BufferedImage getImage() {
        return doAssembleData(doReadSocket());
    }


    /**
     * Takes the byte array and assembles it as a image.
     *
     * @param receivedData Byte array of image.
     * @return Image.
     * @throws IOException Cant read byteArray.
     */
    private BufferedImage doAssembleData(byte[] receivedData) {
        // Initialises the BufferedImage to be returned.
        BufferedImage img = null;

        // Gets raw data and converts it to buffered image.
        try {
            img = ImageIO.read(new ByteArrayInputStream(receivedData));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
}
