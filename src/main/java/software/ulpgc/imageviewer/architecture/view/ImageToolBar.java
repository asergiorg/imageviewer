package software.ulpgc.imageviewer.architecture.view;

public interface ImageToolBar {
    void showImageName(String name);
    void showImageSize(int width, int height);
    void showImageMemorySize(long size);
}
