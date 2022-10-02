package fr.marketplace.controller;

import fr.marketplace.Resource;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectViewTypeMarketplace implements Initializable {
    public HBox hbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        switch (ClientApplication.loggedUser.type()) {
            case MARKETPLACE -> {
                addSection("Marketplace (Just View)", "customer.svg", event -> {

                });
                addSection("Product Management", "product.svg", event -> {
                    ClientApplication.changeSceneFromFXML("product_marketplace_controller.fxml", true, "MarketPlace - Product Management");
                });
                addSection("Users Management", "users.svg", event -> {
                    ClientApplication.changeSceneFromFXML("user_marketplace_controller.fxml", true, "MarketPlace - Product Management");
                });
                addSection("Sales Management", "sales.svg", event -> {

                });
                addSection("Delivery Management", "delivery.svg", event -> {

                });
            }
            case SELLER -> {
                addSection("Marketplace (Just View)", "marketplace.svg", event -> {

                });
                addSection("Product Management", "product.svg", event -> {
                    ClientApplication.changeSceneFromFXML("product_marketplace_controller.fxml", true, "MarketPlace - Product Management");
                });
                addSection("Sales Management", "sales.svg", event -> {

                });
                addSection("Delivery Management", "delivery.svg", event -> {

                });
            }
            case DELIVERY -> {
                addSection("Delivery Section", "delivery.svg", event -> {

                });
            }
            case CUSTOMER -> {
                addSection("Customer Section", "customer.svg", event -> {

                });
            }
        }
    }

    public void addSection(String nameSection, String imageName, EventHandler<? super MouseEvent> onClick) {
        final VBox vBox = new VBox();
        HBox.setHgrow(vBox, Priority.ALWAYS);
        vBox.setAlignment(Pos.CENTER);

        final ImageView imageView = new ImageView(Resource.loadSVG(imageName));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(100);

        final Label deliveryLabel = new Label(nameSection);
        final Button access = new Button("access");
        access.setFocusTraversable(false);

        vBox.getChildren().add(imageView);
        vBox.getChildren().add(deliveryLabel);
        vBox.getChildren().add(access);

        VBox.setMargin(access, new Insets(10, 0, 0, 0));

        hbox.getChildren().add(vBox);

        access.setOnMouseClicked(onClick);
    }
}
