package software.ulpgc.imageviewer.architecture.control;

import software.ulpgc.imageviewer.architecture.presenter.ImagePresenter;

public class InformationCommand implements Command {
    private final ImagePresenter presenter;

    public InformationCommand(ImagePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        System.out.println("Fecha de última modificación: " + presenter.image().lastModified());
        System.out.println("Tamaño de la imagen (KB): " + presenter.image().size());
    }
}
