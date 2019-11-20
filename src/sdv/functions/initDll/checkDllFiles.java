package sdv.functions.initDll;

import java.io.*;

/**
 * @author Andreas
 * @version 1.0
 * @since 11.11.2019, 09:41
 */
public class checkDllFiles {

    public static void check() {
        try {
            File xboxcontrollerDestinationPath = new File("\\\\C:\\Windows\\xboxcontroller.dll");
            File xboxcontroller64DestinationPath = new File("\\\\C:\\Windows\\xboxcontroller64.dll");

            File xboxcontroller = new File("resources/dll/xboxcontroller.dll");
            File xboxcontroller64 = new File("resources/dll/xboxcontroller64.dll");


            if (!xboxcontrollerDestinationPath.exists()) {
                copyFiles(xboxcontroller, xboxcontrollerDestinationPath);
                System.out.println("xboxcontroller.dll added");
            }
            if (!xboxcontroller64DestinationPath.exists()) {
                copyFiles(xboxcontroller64, xboxcontroller64DestinationPath);
                System.out.println("xboxcontroller64.dll added");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("You have to add the DLL files manually");
        }
    }


    private static void copyFiles(File src, File dest) throws Exception {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dest); // buffer size 1K
            byte[] buf = new byte[(int) Math.pow(2, 17)];
            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) {
                os.write(buf, 0, bytesRead);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}