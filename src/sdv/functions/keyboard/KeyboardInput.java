package sdv.functions.keyboard;

import javafx.scene.input.KeyEvent;


/**
 * Handles arrow key inputs from user, to manually control car. Can only use on key at the time,
 * pressed key has to be released before next key can be pressed.
 *
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 12:19
 */
public class KeyboardInput {
    private int[] oldDirection = new int[2];

    private static final String forwards = "W";
    private static final String backwards = "S";
    private static final String right = "D";
    private static final String left = "A";

    private boolean setForward;
    private boolean setBackward;
    private boolean setLeft;
    private boolean setRight;

    private int speed = 50;

    /**
     * Initializes the class.
     */
    public KeyboardInput() {
    }

    /**
     * Builds and returns string containing event type and what key is used.
     *
     * @param event Key event.
     */
    public int[] doHandleKeyEvent(KeyEvent event) {
        // Code of button event.
        String keyEvent = event.getEventType().toString().toUpperCase();
        // Event type(press of release).
        String keyName = event.getCode().toString().toUpperCase();
        // String to be returned.

        int[] goDirection = new int[2];


// Activate a key if no other key is active.
        if (keyEvent.equals("KEY_PRESSED")) {
            switch (keyName) {
                case forwards:
                    goDirection[0] = this.speed;
                    goDirection[1] = this.speed;
                    this.setForward = true;
                    break;
                case backwards:
                    goDirection[0] = -this.speed;
                    goDirection[1] = -this.speed;
                    this.setBackward = true;
                    break;
                case right:
                    goDirection[0] = this.speed;
                    goDirection[1] = -this.speed;
                    this.setRight = true;
                    break;
                case left:
                    goDirection[0] = -this.speed;
                    goDirection[1] = this.speed;
                    this.setLeft = true;
                    break;
            }
        }
        if (keyEvent.equals("KEY_RELEASED")) {
            switch (keyName) {
                case forwards:
                    this.setForward = false;
                    break;
                case backwards:
                    this.setBackward = false;
                    break;
                case right:
                    this.setRight = false;
                    break;
                case left:
                    this.setLeft = false;
                    break;
            }
        }

        if (this.setForward && this.setRight) {
            goDirection[0] = this.speed;
            goDirection[1] = this.speed / 4;

        } else if (this.setForward && this.setLeft) {
            goDirection[0] = this.speed / 4;
            goDirection[1] = this.speed;

        } else if (this.setBackward && this.setRight) {
            goDirection[0] = -this.speed;
            goDirection[1] = -this.speed / 4;

        } else if (this.setBackward && this.setLeft) {
            goDirection[0] = -this.speed / 4;
            goDirection[1] = -this.speed;
        }

        if (!this.setForward && !this.setBackward && !this.setRight && !this.setLeft) {
            goDirection[0] = 0;
            goDirection[1] = 0;
        }

        int[] direction;
        if (this.oldDirection == goDirection) {
            direction = null;
        } else {
            direction = goDirection;
            this.oldDirection = goDirection;
        }

        return direction;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}