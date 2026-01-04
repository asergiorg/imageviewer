package software.ulpgc.imageviewer.architecture;

import java.nio.file.Path;
import java.util.Date;

public interface Image {
    String id();
    byte[] bitmap();
    Image next();
    Image previous();
    Date lastModified();
    Long size();
    Path getPath();
}
