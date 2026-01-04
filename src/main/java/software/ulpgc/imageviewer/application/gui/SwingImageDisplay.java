package software.ulpgc.imageviewer.application.gui;

import software.ulpgc.imageviewer.architecture.Canvas;
import software.ulpgc.imageviewer.architecture.ImageDisplay;
import software.ulpgc.imageviewer.architecture.ImageMetrics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SwingImageDisplay extends JPanel implements ImageDisplay, ImageMetrics {
    private Shift shift;
    private Released released;
    private Paint[] paints;
    private int width, height;
    private Runnable onFirstPaint;
    private boolean firstPaintDone = false;

    public void onFirstPaint(Runnable r) {
        this.onFirstPaint = r;
    }

    public SwingImageDisplay() {
        MouseAdapter mouseAdapter = new MouseAdapter();
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(mouseAdapter);
    }

    @Override
    public void paint(Paint... paints) {
        this.paints = paints;
        this.repaint();
    }

    @Override
    public int width() {
        return this.getWidth();
    }

    private final Map<Integer, BufferedImage> images = new HashMap<>();
    private BufferedImage toBufferedImage(byte[] bitmap) {
        return images.computeIfAbsent(Arrays.hashCode(bitmap), _ -> read(bitmap));
    }

    private BufferedImage read(byte[] bitmap) {
        try (InputStream is = new ByteArrayInputStream(bitmap)) {
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.GRAY);
        g2.fillRect(0,0,this.getWidth(), this.getHeight());
        for (Paint paint : paints) {
            BufferedImage bitmap = toBufferedImage(paint.bitmap());
            Canvas canvas = Canvas.ofSize(this.getWidth(), this.getHeight()).fit(bitmap.getWidth(), bitmap.getHeight());
            height = canvas.height();
            width = canvas.width();
            int x = (this.getWidth() - canvas.width()) / 2;
            int y = (this.getHeight() - canvas.height()) / 2;
            AffineTransform at = new AffineTransform();
            at.translate(x + paint.offset(), y);
            double cx = canvas.width() / 2.0;
            double cy = canvas.height() / 2.0;
            at.rotate(Math.toRadians(paint.rotation()), cx, cy);
            at.scale(canvas.width() / (double)bitmap.getWidth(), canvas.height() / (double)bitmap.getHeight());
            g2.drawImage(bitmap, at, null);
        }

        if (!firstPaintDone && onFirstPaint != null) {
            firstPaintDone = true;
            onFirstPaint.run();
        }
    }

    @Override
    public int getCanvasWidth() {
        return width;
    }

    @Override
    public int getCanvasHeight() {
        return height;
    }

    private class MouseAdapter implements MouseListener, MouseMotionListener {
        private int x;

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            x =  e.getX();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            SwingImageDisplay.this.released.offset(e.getX() - x);
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            SwingImageDisplay.this.shift.offset(e.getX() - x);
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    @Override
    public void on(Shift shift) {
        this.shift = shift;
    }

    @Override
    public void on(Released released) {
        this.released = released;
    }

}






















