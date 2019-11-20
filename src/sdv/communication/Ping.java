package sdv.communication;

import javafx.scene.control.Label;
import sdv.functions.updater.LabelUpdater;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

/**
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 12:19
 */
public class Ping extends Thread {
    private String ipAdress;
    private LabelUpdater labelUpdater;
    // True to stop reading, false to continue.
    private boolean stop;

    public Ping(String ipAddress, Label label) {
        this.ipAdress = ipAddress;
        this.labelUpdater = new LabelUpdater(label);
    }


    public void run() {
        while (!this.stop) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long responseTime = getResponseTime();

            this.labelUpdater.updateLabel("Server response responseTime: " + responseTime + " ms");
        }
        this.labelUpdater.doClear();
    }

    /**
     * @return
     */
    public long getResponseTime() {
        long responseTime = 0;
        try {
            Date start = new Date();

            InetAddress inet = InetAddress.getByName(this.ipAdress);
            if (inet.isReachable(5000)) {
                Date stop = new Date();
                responseTime = (stop.getTime() - start.getTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseTime;
    }

    /**
     * Changes flag to stop loop and close thread.
     */
    public void doStop() {
        this.stop = true;
    }
}