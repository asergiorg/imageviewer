package software.ulpgc.imageviewer.architecture;

import software.ulpgc.imageviewer.application.gui.SwingImageDisplay;
import software.ulpgc.imageviewer.architecture.ImageDisplay.Paint;

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
            swing.onFirstPaint(() -> { toolBar.showImageSize(metrics.getCanvasWidth(), metrics.getCanvasHeight()); });
        }
        this.display.on((ImageDisplay.Shift) offset -> display.paint(
                new Paint(image.bitmap(), offset),
                new Paint(
                        offset < 0 ? image.next().bitmap() : image.previous().bitmap(),
                        offset < 0 ? display.width() + offset : offset - display.width())
        ));
        this.display.on((ImageDisplay.Released) offset -> {
            if (Math.abs(offset) * 2 > display.width()) image = offset < 0 ? image.next() : image.previous();
            //System.out.println(image.id());
            display.paint(new Paint(image.bitmap(), 0));
            toolBar.showImageName(image.id());
            toolBar.showImageMemorySize(image.size());
            toolBar.showImageSize(metrics.getCanvasWidth(), metrics.getCanvasHeight());
        });
    }

    public void show(Image image) {
        this.image = image;
        this.display.paint(new Paint(image.bitmap(), 0));
        this.toolBar.showImageName(image().id());
        this.toolBar.showImageMemorySize(image.size());
        toolBar.showImageSize(metrics.getCanvasWidth(), metrics.getCanvasHeight());
    }

    public Image image() {
        return image;
    }

}
