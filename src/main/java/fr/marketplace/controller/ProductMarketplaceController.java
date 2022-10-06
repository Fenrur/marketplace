package fr.marketplace.controller;

import fr.marketplace.bi.Product;
import fr.marketplace.bi.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

import javax.money.MonetaryAmount;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

public class ProductMarketplaceController implements Initializable {
    public TableColumn<MutableProduct, UUID> idTableColumn;
    public TableColumn<MutableProduct, String> nameTableColumn;
    public TableColumn<MutableProduct, String> sellerNameTableColumn;
    public TableColumn<MutableProduct, PrettyMonetaryAmount> priceTableColumn;
    public TableColumn<MutableProduct, CheckBox> isAvailableTableColumn;
    public TableColumn<MutableProduct, CheckBox> isAvailableByMarketplaceTableColumn;
    public TableView<MutableProduct> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (ClientApplication.loggedUser.isDisable() || (ClientApplication.loggedUser.type() != User.Type.MARKETPLACE && ClientApplication.loggedUser.type() != User.Type.SELLER))
            return;

        tableView.setItems(FXCollections.observableArrayList(
                ClientApplication
                        .marketPlaceRepository
                        .productRepository()
                        .getAllProducts()
                        .stream()
                        .filter(product -> {
                            if (ClientApplication.loggedUser.type() == User.Type.MARKETPLACE) return true;
                            else {
                                final Optional<UUID> userIdByProductId = ClientApplication
                                        .marketPlaceRepository
                                        .productRepository()
                                        .getUserIdByProductId(product.id());

                                if (userIdByProductId.isEmpty()) return false;
                                return userIdByProductId.get().equals(ClientApplication.loggedUser.id());
                            }
                        })
                        .map(product -> MutableProduct.from(product, ClientApplication.marketPlaceRepository.userRepository(), ClientApplication.marketPlaceRepository.productRepository()))
                        .toList()
        ));

        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        sellerNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("sellerName"));
        priceTableColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(new PrettyMonetaryAmount(param.getValue().getPrice())));
        isAvailableTableColumn.setCellValueFactory(param -> {
            final CheckBox checkBox = new CheckBox();
            checkBox.setSelected(param.getValue().isAvailable());
            checkBox.setFocusTraversable(false);
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                param.getValue().setAvailable(newValue);

                final Product product = param.getValue().build();

                ClientApplication
                        .marketPlaceRepository
                        .productRepository()
                        .put(
                                ClientApplication.loggedUser.id(),
                                product
                        );

            });
            return new SimpleObjectProperty<>(checkBox);
        });
        isAvailableByMarketplaceTableColumn.setCellValueFactory(param -> {
            final CheckBox checkBox = new CheckBox();
            checkBox.setSelected(param.getValue().isAvailableByMarketplace());
            checkBox.setFocusTraversable(false);
            checkBox.setDisable(ClientApplication.loggedUser.type() != User.Type.MARKETPLACE);

            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                param.getValue().setAvailable(newValue);

                final Product product = param.getValue().build();

                ClientApplication
                        .marketPlaceRepository
                        .productRepository()
                        .put(
                                ClientApplication.loggedUser.id(),
                                product
                        );

            });
            return new SimpleObjectProperty<>(checkBox);
        });
    }

    public void onNewProductClicked(MouseEvent mouseEvent) {
        if (ClientApplication.loggedUser.isDisable() || (ClientApplication.loggedUser.type() != User.Type.MARKETPLACE && ClientApplication.loggedUser.type() != User.Type.SELLER))
            return;

        final MutableProduct mutableProduct = new MutableProduct();
        mutableProduct.setId(UUID.randomUUID());
        ClientApplication.showNewStageFromFXML("product_editor_controller.fxml", true, "MarketPlace - New Product", param -> new ProductEditorController(mutableProduct, this), StageStyle.DECORATED);
    }

    public void onEditClicked(MouseEvent mouseEvent) {
        if (ClientApplication.loggedUser.isDisable() || (ClientApplication.loggedUser.type() != User.Type.MARKETPLACE && ClientApplication.loggedUser.type() != User.Type.SELLER))
            return;

        final MutableProduct mutableProduct = tableView.getSelectionModel().getSelectedItem();
        if (mutableProduct != null) {
            ClientApplication.showNewStageFromFXML("product_editor_controller.fxml", true, "MarketPlace - Edit Product", param -> new ProductEditorController(mutableProduct, this), StageStyle.DECORATED);
        }
    }

    public void onShowProductClicked(MouseEvent mouseEvent) {
        if (ClientApplication.loggedUser.isDisable() || (ClientApplication.loggedUser.type() != User.Type.MARKETPLACE && ClientApplication.loggedUser.type() != User.Type.SELLER))
            return;

        final MutableProduct mutableProduct = tableView.getSelectionModel().getSelectedItem();
        if (mutableProduct == null) return;
        final String name = mutableProduct.getName();
        final MonetaryAmount price = mutableProduct.getPrice();
        final Set<URI> uris = Set.copyOf(mutableProduct.getImages());
        final String description = mutableProduct.getDescription();
        ClientApplication.showNewStageFromFXML(
                "product_viewer_controller.fxml",
                true,
                "MarketPlace - Product Viewer",
                param -> new ProductViewerController(uris, name, new PrettyMonetaryAmount(price), description),
                StageStyle.DECORATED
        );
    }

    public void onBackClicked(MouseEvent mouseEvent) {
        ClientApplication.changeSceneFromFXML(
                "select_view_type_marketplace.fxml",
                false,
                "MarketPlace - Select"
        );
    }
}
