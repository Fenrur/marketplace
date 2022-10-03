package fr.marketplace.controller;

import fr.marketplace.bi.Product;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.javamoney.moneta.FastMoney;

import java.net.URI;
import java.net.URL;
import java.util.Currency;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductEditorController implements Initializable {
    public TextField productNameTextField;
    public TextArea descriptionTextArea;
    public ComboBox<PrettyCurrency> currencyComboBox;
    public TextField priceTextField;
    public ListView<TextField> urlListView;
    public CheckBox isAvailableCheckBox;

    private MutableProduct mutableProduct;
    private ProductMarketplaceController productMarketplaceController;

    public ProductEditorController(MutableProduct mutableProduct, ProductMarketplaceController productMarketplaceController) {
        this.mutableProduct = mutableProduct;
        this.productMarketplaceController = productMarketplaceController;
    }

    public ProductEditorController() {
    }

    public void onAddUrlClicked(MouseEvent mouseEvent) {
        urlListView.getItems().add(new TextField());
    }

    public void onRemoveUrlClick(MouseEvent mouseEvent) {
        final Iterator<TextField> iterator = urlListView.getItems().iterator();
        while (iterator.hasNext()) {
            final TextField textField = iterator.next();
            if (textField.isFocused()) {
                iterator.remove();

                if (iterator.hasNext()) {
                    final TextField next = iterator.next();
                    next.requestFocus();
                } else if (!urlListView.getItems().isEmpty()) {
                    urlListView.getItems().get(urlListView.getItems().size() - 1).requestFocus();
                }

                return;
            }
        }
    }

    public void onIsAvailableCheckBoxChanged(boolean newValue) {

    }

    public void onConfirmClicked(MouseEvent mouseEvent) {
        try {
            final FastMoney money = FastMoney.of(Double.parseDouble(priceTextField.getText()), currencyComboBox.getValue().currency().getCurrencyCode());

            final Set<URI> images = urlListView
                    .getItems()
                    .stream()
                    .map(TextInputControl::getText)
                    .filter(url -> !url.isBlank())
                    .map(URI::create)
                    .collect(Collectors.toSet());

            final Product product = new Product(
                    mutableProduct.getId(),
                    productNameTextField.getText(),
                    descriptionTextArea.getText(),
                    money,
                    images,
                    isAvailableCheckBox.isSelected(),
                    true
            );

            mutableProduct.setFrom(product);

            if (mutableProduct.getSellerName() == null) {
                mutableProduct.setSellerName(ClientApplication.loggedUser.username().username());
            }

            ClientApplication
                    .marketPlaceRepository
                    .productRepository()
                    .put(ClientApplication.loggedUser.id(), mutableProduct.build());

            if (!productMarketplaceController.tableView.getItems().contains(mutableProduct)) {
                productMarketplaceController.tableView.getItems().add(mutableProduct);
            }

            productMarketplaceController.tableView.refresh();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Price is not a number", ButtonType.OK).showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isAvailableCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> onIsAvailableCheckBoxChanged(newValue));
        currencyComboBox.getItems().addAll(PrettyCurrency.getAvailableCurrencies());

        if (mutableProduct.getName() != null) {
            productNameTextField.setText(mutableProduct.getName());
        }

        if (mutableProduct.getDescription() != null) {
            descriptionTextArea.setText(mutableProduct.getDescription());
        }

        if (mutableProduct.getPrice() != null) {
            currencyComboBox.setValue(new PrettyCurrency(Currency.getInstance(mutableProduct.getPrice().getCurrency().getCurrencyCode())));
            priceTextField.setText(String.valueOf(mutableProduct.getPrice().getNumber().doubleValue()));
        } else {
            currencyComboBox.setValue(PrettyCurrency.defaultPrettyCurrency());
        }

        if (mutableProduct.getImages() != null) {
            for (URI image : mutableProduct.getImages()) {
                urlListView.getItems().add(new TextField(image.toString()));
            }
        }

        isAvailableCheckBox.setSelected(mutableProduct.isAvailable());

        urlListView.getItems().add(new TextField());
    }
}
