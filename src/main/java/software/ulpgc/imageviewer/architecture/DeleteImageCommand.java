package software.ulpgc.imageviewer.architecture;

import java.io.File;

public class DeleteImageCommand implements Command {
    private final ImagePresenter imagePresenter;
    private final ImageProvider imageProvider;

    public DeleteImageCommand(ImagePresenter imagePresenter, ImageProvider imageProvider) {
        this.imagePresenter = imagePresenter;
        this.imageProvider = imageProvider;
    }

    @Override
    public void execute() {
        File file = new File(String.valueOf(imagePresenter.image().getPath()));
        Image image = imagePresenter.image().next();
        if (imageProvider.imagesCount() > 1 && file.delete()) {
            if (imageProvider.imagesCount() + 1 > 1) imagePresenter.show(image);
            imageProvider.popImage(image.previous().id());
            System.out.println("Deleted " + file.getAbsolutePath());
        }
    }
}
