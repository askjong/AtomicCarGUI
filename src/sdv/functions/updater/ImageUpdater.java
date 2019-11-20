package sdv.functions.updater;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

/**
 * Takes a BufferdImage element and puts it to a JavaFx ImageView element.
 *
 * @author Andreas
 * @version 1.0
 * @since 17.11.2019, 12:19
 */
public class ImageUpdater {
    // ImageView to send image to.
    private ImageView imageView;

    /**
     * Setts the ImageViewer.
     *
     * @param imageView ImageViewer, displays image.
     */
    public ImageUpdater(ImageView imageView) {
        this.imageView = imageView;
    }

    /**
     * Takes a buffered image and draws it to the ImageView.
     *
     * @param img Image to show user.
     */
    public void updateImage(BufferedImage img) {
        this.imageView.setImage(doChangeImage(img));
    }

    /**
     * Takes a buffered image and returns it as an image.
     *
     * @param img BufferedImage, becomes Image.
     * @return Image.
     */
    private Image doChangeImage(BufferedImage img) {
        return SwingFXUtils.toFXImage(img, null);
    }

    /**
     * Clears the ImageView.
     */
    public void doClear() {
        this.imageView.setImage(null);
    }
}