package sdv.functions;

/**
 * System statuses
 *
 * @author Andreas
 * @version 1.0
 * @since 15.11.2019, 00:32
 */
public class ClassStatus {
    private static final String DISCONNECTED = "DISCONNECTED";
    private static final String LOADING = "LOADING";
    private static final String CONNECTED = "CONNECTED";

    /**
     * Gets DISCONNECTED from status
     *
     * @return value of DISCONNECTED
     */
    public static String isDisconnected() {
        return DISCONNECTED;
    }

    /**
     * Gets LOADING from status
     *
     * @return value of LOADING
     */
    public static String isLoading() {
        return LOADING;
    }

    /**
     * Gets CONNECTED from status
     *
     * @return value of CONNECTED
     */
    public static String isConnected() {
        return CONNECTED;
    }
}
