package fr.marketplace.controller;

import fr.marketplace.Json;
import fr.marketplace.Resource;
import fr.marketplace.Utils;
import fr.marketplace.bi.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.nio.file.Path;

public class ClientApplication extends Application {

    public static MarketPlaceRepository marketPlaceRepository;
    public static Stage stage;
    public static User loggedUser;

    public static void main(String[] args) {
        launch(args);
    }

    public static void changeSceneFromFXML(String name, boolean resizable, String title) {
        final Parent parent = Resource.loadFXML(name);
        final Scene scene = new Scene(parent, -1, -1, true, SceneAntialiasing.BALANCED);
        stage.setScene(scene);
        stage.setResizable(resizable);
        stage.setTitle(title);
        Platform.runLater(() -> {
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        });
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        final Json json = new Json();

        final ProductRepository productRepository = ProductRepository.fromFile(json, Path.of("/Users/livio/Desktop/aa/products.json"));
        final UserRepository userRepository = UserRepository.fromFile(json, Path.of("/Users/livio/Desktop/aa/users.json"));
        final OrderRepository orderRepository = OrderRepository.fromFile(json, Path.of("/Users/livio/Desktop/aa/orders.json"));
        final StockProductRepository stockProductRepository = StockProductRepository.fromFile(json, Path.of("/Users/livio/Desktop/aa/stock_products.json"));
        final DeliveryServiceRepository deliveryServiceRepository = DeliveryServiceRepository.fromFile(json, Path.of("/Users/livio/Desktop/aa/delivery_services.json"));

        marketPlaceRepository = new MarketPlaceRepository(productRepository, orderRepository, userRepository, stockProductRepository, deliveryServiceRepository);

        loggedUser = Utils.first(userRepository);

        changeSceneFromFXML("select_view_type_marketplace.fxml", false, "MarketPlace - Select");
        primaryStage.show();
    }
}