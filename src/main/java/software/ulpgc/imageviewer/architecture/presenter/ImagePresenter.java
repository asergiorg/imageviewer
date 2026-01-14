package software.ulpgc.imageviewer.architecture.presenter;

import software.ulpgc.imageviewer.application.SwingImageDisplay;
import software.ulpgc.imageviewer.architecture.view.ImageDisplay;
import software.ulpgc.imageviewer.architecture.view.ImageDisplay.Paint;
import software.ulpgc.imageviewer.architecture.tasks.ImageMetrics;
import software.ulpgc.imageviewer.architecture.view.ImageToolBar;
import software.ulpgc.imageviewer.architecture.model.Image;

public class ImagePresenter {
    private final ImageDisplay display;
    private Image image;
    private final ImageToolBar toolBar;
    private final ImageMetrics metrics;

    public ImagePresenter(ImageDisplay display, ImageToolBar toolBar, ImageMetrics metrics) {
        this.display = display;
        this.toolBar = toolBar;
        this.metrics = metrics;
        if (display instanceof SwingImageDisplay swing) {
            swing.onFirstPaint(() -> toolBar.showImageSize(metrics.getCanvasWidth(), metrics.getCanvasHeight()));
        }
        this.display.on((ImageDisplay.Shift) offset -> display.paint(
                new Paint(image.bitmap(), offset, image.rotation()),
                new Paint(
                        offset < 0 ? image.next().bitmap() : image.previous().bitmap(),
                        offset < 0 ? display.width() + offset : offset - display.width(),
                        offset < 0 ? image.next().rotation() : image.previous().rotation())
        ));
        this.display.on((ImageDisplay.Released) offset -> {
            if (Math.abs(offset) * 2 > display.width()) image = offset < 0 ? image.next() : image.previous();
            display.paint(new Paint(image.bitmap(), 0, image.rotation()));
            refreshImageInformation(image);
        });
    }

    public void show(Image image) {
        this.image = image;
        this.display.paint(new Paint(image.bitmap(), 0, image.rotation()));
        refreshImageInformation(image);
    }

    private void refreshImageInformation(Image image) {
        toolBar.showImageName(image.id());
        toolBar.showImageMemorySize(image.size());
        if ((image.rotation() / 2) % 2 == 0) toolBar.showImageSize(metrics.getCanvasWidth(), metrics.getCanvasHeight());
        else toolBar.showImageSize(metrics.getCanvasHeight(), metrics.getCanvasWidth());
    }

    public Image image() {
        return image;
    }

}
