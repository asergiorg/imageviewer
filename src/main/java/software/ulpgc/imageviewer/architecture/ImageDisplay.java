package software.ulpgc.imageviewer.architecture;

public interface ImageDisplay {

    void on(Shift shift);
    void on(Released released);

    void paint(Paint... paints);

    int width();

    interface Shift {
        void offset(int value);
    }

    interface Released {
        void offset(int value);
    }

    record Paint(byte[] bitmap, int offset, int rotation) {
    }

}
