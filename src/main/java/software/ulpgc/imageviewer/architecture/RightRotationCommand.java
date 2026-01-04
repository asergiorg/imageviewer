package software.ulpgc.imageviewer.architecture;

import java.awt.*;

public class RightRotationCommand implements Command {
    private final ImagePresenter imagePresenter;

    public RightRotationCommand(ImagePresenter imagePresenter) {
        this.imagePresenter = imagePresenter;
    }

    @Override
    public void execute() {
        imagePresenter.show(imagePresenter.image().rotateRight());
    }
}
