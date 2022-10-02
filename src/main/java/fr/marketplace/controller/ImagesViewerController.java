package fr.marketplace.controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class ImagesViewerController implements Initializable {
    private final String productName;
    private final String price;
    public ImageView imageView;
    private final List<Image> allImages = new CopyOnWriteArrayList<>();
    public Label productNameLabel;
    public Label priceLabel;
    public Label indexLabel;
    int index = 0;

    private final static HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public ImagesViewerController(Set<URI> images, String productName, String price) {
        this.productName = productName;
        this.price = price;
        for (URI url : images) {
            HTTP_CLIENT.sendAsync(
                            HttpRequest.newBuilder()
                                    .uri(url)
                                    .build(),
                            HttpResponse.BodyHandlers.ofInputStream()
                    )
                    .thenAccept(httpResponse -> {
                        if (httpResponse.statusCode() == 200) {

                            final var body = httpResponse.body();

                            Platform.runLater(() -> {
                                final Image image = new Image(body);
                                allImages.add(image);

                                if (imageView.getImage() == null) {
                                    updateImage();
                                }
                                updateIndexLabel();
                            });
                        }
                    });
        }
    }

    public void updateImage() {
        imageView.setImage(allImages.get(index));
    }

    public void updateIndexLabel() {
        indexLabel.setText((index + 1) + " / " + allImages.size());
    }

    public void leftClicked(MouseEvent mouseEvent) {
        if (allImages.isEmpty()) return;

        if (--index < 0) {
            index = allImages.size() - 1;
        }
        updateImage();
        updateIndexLabel();
    }

    public void rightClicked(MouseEvent mouseEvent) {
        if (allImages.isEmpty()) return;

        if (allImages.size() <= ++index) {
            index = 0;
        }
        updateImage();
        updateIndexLabel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productNameLabel.setText(productName);
        priceLabel.setText(price);
    }
}
