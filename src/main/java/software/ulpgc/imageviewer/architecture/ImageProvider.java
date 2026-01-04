package software.ulpgc.imageviewer.architecture;

import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageProvider {
    private final List<String> images;
    private final File root;

    private ImageProvider(Stream<String> images, File root) {
        this.images = images.collect(Collectors.toList());
        this.root = root;
    }

    public static ImageProvider with(Stream<String> images, File root) {
        return new ImageProvider(images, root);
    }

    public Image first(Function<String, byte[]> loader) {
        return load(0, loader);
    }

    private Image load(int index, Function<String, byte[]> loader) {
        return new Image() {
            private byte[] bitmap;

            @Override
            public String id() {
                return images.get(index);
            }

            @Override
            public byte[] bitmap() {
                if (bitmap == null) bitmap = loader.apply(images.get(index));
                return bitmap;
            }

            @Override
            public Image next() {
                return index == images.size() - 1 ? load(0, loader) : load((index + 1) % images.size(), loader);
            }

            @Override
            public Image previous() {
                return index == 0 ? load((images.size() - 1) % images.size(), loader) : load(index - 1, loader);
            }

            @Override
            public Date lastModified() {
                return new Date(getFile().lastModified());
            }

            private File getFile() {
                return new File(ImageProvider.this.root, id());
            }

            @Override
            public Long size() {
                return getFile().length() / 1024;
            }

            @Override
            public Path getPath() {
                return getFile().toPath();
            }
        };
    }

    public void popImage(String id) {
        images.remove(id);
    }

    public int imagesCount() {
        return images.size();
    }
}
