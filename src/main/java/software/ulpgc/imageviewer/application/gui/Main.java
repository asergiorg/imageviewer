package software.ulpgc.imageviewer.application.gui;

import software.ulpgc.imageviewer.application.FileImageStore;
import software.ulpgc.imageviewer.architecture.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    private static File root;

    public static void main(String[] args) {
        root = new File("images");
        ImageStore store = new FileImageStore(root);
        ImageProvider imageProvider = ImageProvider.with(store.images(), root);
        SwingImageDisplay imageDisplay = new SwingImageDisplay();
        Desktop desktop = Desktop.create(imageDisplay);
        ImagePresenter imagePresenter = new ImagePresenter(imageDisplay, desktop, imageDisplay);
        imagePresenter.show(imageProvider.first(Main::readImage));
        desktop.put("⏵", new NextCommand(imagePresenter))
                .put("⏴", new PrevCommand(imagePresenter))
                .put("ⓘ", new InformationCommand(imagePresenter))
                .put("\uD83D\uDDD1", new DeleteImageCommand(imagePresenter, imageProvider))
                .setVisible(true);
    }

    private static byte[] readImage(String id) {
        try {
            return Files.readAllBytes(new File(root, id).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
