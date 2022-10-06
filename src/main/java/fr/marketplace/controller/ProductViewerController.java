package fr.marketplace.controller;

import fr.marketplace.MultiImageView;
import fr.marketplace.Resource;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextAlignment;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

public class ProductViewerController implements Initializable {
    private final String productName;
    private final PrettyMonetaryAmount price;
    private final String description;
    private final Set<URI> images;
    public MultiImageView imageView;
    public Label productNameLabel;
    public Label priceLabel;
    public Label indexLabel;
    public Label descriptionLabel;
    int index = 0;


    public ProductViewerController(Set<URI> images, String productName, PrettyMonetaryAmount price, String description) {
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.images = images;
    }

    private void updateIndexLabel() {
        indexLabel.setText((imageView.getIndex()) + 1 + " / " + imageView.getSize());
    }

    public void leftClicked(MouseEvent mouseEvent) {
        imageView.previousImage();
        updateIndexLabel();
    }

    public void rightClicked(MouseEvent mouseEvent) {
        imageView.nextImage();
        updateIndexLabel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productNameLabel.setText(productName);
        priceLabel.setText(price.toString());
        descriptionLabel.setText(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setTextAlignment(TextAlignment.CENTER);
        imageView.init(new ArrayList<>(this.images), Resource.LOADING_IMAGE);
        imageView.setOnLoadImage(image -> {
            Platform.runLater(() -> {
                updateIndexLabel();
            });
        });
        imageView.fetchAndGet();
    }
}
