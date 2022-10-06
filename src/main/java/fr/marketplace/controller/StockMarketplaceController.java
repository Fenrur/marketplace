package fr.marketplace.controller;

import fr.marketplace.bi.Product;
import fr.marketplace.bi.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StockMarketplaceController implements Initializable {
    public TableView<MutableStock> tableView;
    public TableColumn<MutableStock, UUID> productIdTableColumn;
    public TableColumn<MutableStock, String> productNameTableColumn;
    public TableColumn<MutableStock, Spinner<Integer>> actualStockTableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (ClientApplication.loggedUser.isDisable() || ClientApplication.loggedUser.type() != User.Type.MARKETPLACE)
            return;

        final List<MutableStock> mutableStocks = ClientApplication
                .marketPlaceRepository
                .productRepository()
                .getAllProductByUserId(
                        ClientApplication
                                .loggedUser.id()
                )
                .stream()
                .mapMulti((BiConsumer<Product, Consumer<MutableStock>>) (product, consumer) -> {
                    final int amount = ClientApplication
                            .marketPlaceRepository
                            .stockProductRepository()
                            .getAmount(product.id());

                    final MutableStock mutableStock = new MutableStock(
                            product.id(),
                            product.name(),
                            amount
                    );

                    consumer.accept(mutableStock);
                })
                .toList();

        tableView.setItems(FXCollections.observableArrayList(mutableStocks));
        productIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        actualStockTableColumn.setCellValueFactory(param -> {
            final Spinner<Integer> spinner = new Spinner<>(0, Integer.MAX_VALUE, param.getValue().getActualStock());
            spinner.setEditable(true);

            spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue < 0) {
                    param.getValue().setActualStock(oldValue);
                } else {
                    param.getValue().setActualStock(newValue);
                    ClientApplication
                            .marketPlaceRepository
                            .stockProductRepository()
                            .setAmount(param.getValue().getProductId(), newValue);
                }
            });

            return new SimpleObjectProperty<>(spinner);
        });
    }

    public void onBackClicked(MouseEvent mouseEvent) {
        ClientApplication.changeSceneFromFXML("select_view_type_marketplace.fxml", false, "MarketPlace - Select");
    }

    public void onShowProductClicked(MouseEvent mouseEvent) {
        if (ClientApplication.loggedUser.isDisable() || (ClientApplication.loggedUser.type() != User.Type.MARKETPLACE && ClientApplication.loggedUser.type() != User.Type.SELLER))
            return;

        final MutableStock mutableStock = tableView.getSelectionModel().getSelectedItem();
        if (mutableStock == null) return;

        final Optional<Product> op = ClientApplication
                .marketPlaceRepository
                .productRepository()
                .get(mutableStock.getProductId());

        if (op.isEmpty()) return;

        final Product product = op.get();
        ClientApplication.showNewStageFromFXML("product_viewer_controller.fxml", true, "MarketPlace - Product Viewer", param -> new ProductViewerController(product.images(), product.name(), new PrettyMonetaryAmount(product.price()), product.description()), StageStyle.DECORATED);
    }
}
