package fr.marketplace;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class MultiImageView extends ImageView {

    private List<URI> urls;
    private CopyOnWriteArrayList<Image> images;
    private Image loadingImage;
    private int index;
    private Consumer<Image> consumer;

    public MultiImageView() {
    }

    public void init(List<URI> urls, Image loadingImage) {
        setImage(loadingImage);
        this.index = 0;
        this.urls = List.copyOf(urls);
        this.images = new CopyOnWriteArrayList<>(new Image[this.urls.size()]);
        this.loadingImage = loadingImage;
    }

    public void fetchAndGet() {
        for (int i = 0; i < urls.size(); i++) {
            final URI uri = urls.get(i);
            int finalI = i;
            Resource.fetchImageAsync(uri)
                    .thenAccept(image -> {
                        images.set(finalI, image);
                        if (finalI == getIndex()) {
                            Platform.runLater(() -> {
                                setImage(image);
                            });
                        }
                        consumer.accept(image);
                    });
        }
    }

    public void setOnLoadImage(Consumer<Image> consumer) {
        this.consumer = consumer;
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return urls.size();
    }

    public void nextImage() {
        if (urls.isEmpty()) return;
        index++;
        if (index >= getSize()) {
            index = 0;
        }

        setImage(index);
    }

    public void previousImage() {
        if (urls.isEmpty()) return;
        index--;
        if (index < 0) {
            index = getSize() - 1;
        }

        setImage(index);
    }

    public void setImage(int index) {
        final Image image = images.get(index);
        if (image == null) {
            setImage(loadingImage);
        } else {
            setImage(image);
        }
    }
}
