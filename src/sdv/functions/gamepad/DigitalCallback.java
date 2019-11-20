// DigitalCallback.java

/*
This software is part of the Aplu GamepadInput package.
It is Open Source Free Software, so you may
- run the code for any purpose
- study how the code works and adapt it to your needs
- integrate all or parts of the code in your own programs
- redistribute copies of the code
- improve the code and release your improvements to the public
However the use of the code is entirely your responsibility.
 */


package sdv.functions.gamepad;

interface DigitalCallback {
    void invoke(boolean pressed);

    class F0 implements DigitalCallback {
        private GamepadListener listener;

        public F0(GamepadListener listener) {
            this.listener = listener;
        }

        public void invoke(boolean pressed) {
            listener.start(pressed);
        }
    }

    class F1 implements DigitalCallback {
        private GamepadListener listener;

        public F1(GamepadListener listener) {
            this.listener = listener;
        }

        public void invoke(boolean pressed) {
            listener.back(pressed);
        }
    }

    class F2 implements DigitalCallback {
        private GamepadListener listener;

        public F2(GamepadListener listener) {
            this.listener = listener;
        }

        public void invoke(boolean pressed) {
            listener.leftThumb(pressed);
        }
    }

    class F3 implements DigitalCallback {
        private GamepadListener listener;

        public F3(GamepadListener listener) {
            this.listener = listener;
        }

        public void invoke(boolean pressed) {
            listener.rightThumb(pressed);
        }
    }

    class F4 implements DigitalCallback {
        private GamepadListener listener;

        public F4(GamepadListener listener) {
            this.listener = listener;
        }

        public void invoke(boolean pressed) {
            listener.leftShoulder(pressed);
        }
    }

    class F5 implements DigitalCallback {
        private GamepadListener listener;

        public F5(GamepadListener listener) {
            this.listener = listener;
        }

        public void invoke(boolean pressed) {
            listener.rightShoulder(pressed);
        }
    }

    class F6 implements DigitalCallback {
        private GamepadListener listener;

        public F6(GamepadListener listener) {
            this.listener = listener;
        }

        public void invoke(boolean pressed) {
            listener.buttonA(pressed);
        }
    }

    class F7 implements DigitalCallback {
        private GamepadListener listener;

        public F7(GamepadListener listener) {
            this.listener = listener;
        }

        public void invoke(boolean pressed) {
            listener.buttonB(pressed);
        }
    }

    class F8 implements DigitalCallback {
        private GamepadListener listener;

        public F8(GamepadListener listener) {
            this.listener = listener;
        }

        public void invoke(boolean pressed) {
            listener.buttonX(pressed);
        }
    }

    class F9 implements DigitalCallback {
        private GamepadListener listener;

        public F9(GamepadListener listener) {
            this.listener = listener;
        }

        public void invoke(boolean pressed) {
            listener.buttonY(pressed);
        }
    }
}
