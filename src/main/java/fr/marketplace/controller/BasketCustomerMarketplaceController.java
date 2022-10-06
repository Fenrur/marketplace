package fr.marketplace.controller;

import fr.marketplace.bi.ShoppingProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class BasketCustomerMarketplaceController implements Initializable {
    public TableView<ShoppingCart> tableView;
    public TableColumn<CustomerMarketplaceController, Object> previewTableColumn;
    public TableColumn productNameTableColumn;
    public TableColumn estimatedDeliveryTableColumn;
    public TableColumn quantityTableColumn;
    public TableColumn uniPriceTableColumn;
    public TableColumn totalPriceTableColumn;
    public Label totalPrice;

    public CustomerMarketplaceController customerMarketplaceController;
    public ObservableList<ShoppingProduct> shoppingProducts;

    public BasketCustomerMarketplaceController(CustomerMarketplaceController customerMarketplaceController) {
        this.customerMarketplaceController = customerMarketplaceController;
        this.shoppingProducts = FXCollections.observableArrayList(customerMarketplaceController.shoppingCart.getShoppingProducts());
    }

    public BasketCustomerMarketplaceController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onRemoveProductsClicked(MouseEvent mouseEvent) {
//        final CustomerMarketplaceController.Data selectedItem = tableView.getSelectionModel().getSelectedItem();
//        if (selectedItem == null) return;

//        customerMarketplaceController.shoppingCart.remove(selectedItem.getProductId());
    }
}
