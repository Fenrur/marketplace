package fr.marketplace.controller;

import fr.marketplace.MultiImageView;
import fr.marketplace.Resource;
import fr.marketplace.Utils;
import fr.marketplace.bi.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CustomerMarketplaceController implements Initializable {

    public final ShoppingCart shoppingCart;
    public TableView<Data> tableView;
    public TableColumn<Data, ImageView> imageTableColumn;
    public TableColumn<Data, String> productNameTableColumn;
    public TableColumn<Data, PrettyMonetaryAmount> priceTableColumn;
    public TableColumn<Data, Username> sellerNameTableColumn;
    public TableColumn<Data, LocalDate> estimatedDeliveryTableColumn;
    public Button buyButton;
    public Spinner<Integer> amountProductSpinner;
    public Button showBasketButton;

    public Stage searchStage;
    private SearchCustomerMarketplaceController searchCustomerMarketplaceController;

    public CustomerMarketplaceController() {
        this.shoppingCart = new ShoppingCart();
    }

    private static List<Data> generateDataForTableView(Set<Product> allProducts) {
        return allProducts
                .stream()
                .filter(Product::productIsAvailable)
                .mapMulti((BiConsumer<Product, Consumer<Data>>) (product, dataConsumer) -> {
                    final Set<URI> images = product.images();

                    final ImageView imageView;
                    if (images.isEmpty()) {
                        imageView = new ImageView(Resource.LOADING_IMAGE);
                    } else {
                        MultiImageView multiImageView = new MultiImageView();
                        multiImageView.init(List.of(Utils.first(images)), Resource.LOADING_IMAGE);
                        multiImageView.fetchAndGet();
                        imageView = multiImageView;
                        imageView.setPreserveRatio(true);
                        imageView.setFitHeight(100);

//                                        imageView = new ImageView(Resource.LOADING_IMAGE);
                    }

                    final Optional<UUID> op = ClientApplication
                            .marketPlaceRepository
                            .productRepository()
                            .getUserIdByProductId(product.id());

                    if (op.isEmpty()) {
                        return;
                    }
                    final UUID userId = op.get();

                    final Optional<User> op1 = ClientApplication
                            .marketPlaceRepository
                            .userRepository()
                            .get(userId);

                    if (op1.isEmpty()) {
                        return;
                    }

                    final User user = op1.get();

                    final Object priceComponent;
                    final PrettyMonetaryAmount price;
                    if (user.isSubscriber()) {
                        final PrettyMonetaryAmount discountPrice = new PrettyMonetaryAmount(product.price().multiply(0.9d));
                        final PrettyMonetaryAmount realPrice = new PrettyMonetaryAmount(product.price());

                        HBox hBox = new HBox();

                        final Text text = new Text(realPrice.toString());
                        text.setStrikethrough(true);
                        text.setFill(Color.web("#D73A4A"));
                        hBox.getChildren().add(text);


                        final Label label2 = new Label(discountPrice.toString());
                        label2.setTextFill(Color.web("#00CA83"));
                        hBox.getChildren().add(label2);
                        label2.setPadding(new Insets(0, 0, 0, 5));

                        priceComponent = hBox;
                        price = discountPrice;
                    } else {
                        final PrettyMonetaryAmount p = new PrettyMonetaryAmount(product.price());
                        priceComponent = new Label(p.toString());
                        price = p;
                    }

                    final LocalDate estimatedDeliveryDate = LocalDate.now().plusDays(1 + new Random().nextInt(7));
                    final Data data = new Data(product.id(), imageView, product.name(), priceComponent, user.username(), estimatedDeliveryDate, price);
                    dataConsumer.accept(data);
                })
                .collect(Collectors.toList());
    }

    public void onShowProductClicked(MouseEvent mouseEvent) {
        final Data selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        final Optional<Product> op = ClientApplication
                .marketPlaceRepository
                .productRepository()
                .get(selectedItem.productId);

        if (op.isEmpty()) return;
        final Product product = op.get();

        ClientApplication.showNewStageFromFXML("product_viewer_controller.fxml", true, "MarketPlace - Product Viewer", param -> new ProductViewerController(product.images(), product.name(), new PrettyMonetaryAmount(product.price()), product.description()), StageStyle.DECORATED);
    }

    public void onBuyClicked(MouseEvent mouseEvent) {
        final Data selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        shoppingCart.add(selectedItem.getProductId(), selectedItem.getPrice().monetaryAmount(), amountProductSpinner.getValue());
        updateBasketButton();
    }

    public void updateBasketButton() {
        final List<ShoppingProduct> shoppingProducts = shoppingCart.getShoppingProducts();
        final int sum = shoppingProducts
                .stream()
                .mapToInt(ShoppingProduct::amount)
                .sum();

        showBasketButton.setText("show basket (" + sum + ")");

        System.out.println(shoppingProducts);
    }

    public void onShowBasketClicked(MouseEvent mouseEvent) {
    }

    public void onBackClicked(MouseEvent mouseEvent) {
        searchStage.close();
        ClientApplication.changeSceneFromFXML("select_view_type_marketplace.fxml", false, "MarketPlace - Select");
    }

    public ProductSearch generateProductSearch() {
        final List<Search> searches = new ArrayList<>();

        final String productNameText = searchCustomerMarketplaceController.productNameTextField.getText();
        if (!productNameText.isBlank() && !productNameText.isEmpty()) {
            searches.add(new Search.ByName(productNameText));
        }

        try {
            final double min = Double.parseDouble(searchCustomerMarketplaceController.minPriceTextField.getText());
            if (min > 0) {
                searches.add(new Search.ByPriceMin(min));
            }
        } catch (NumberFormatException ignored) {

        }

        try {
            final double max = Double.parseDouble(searchCustomerMarketplaceController.maxTextField.getText());
            if (max > 0) {
                searches.add(new Search.ByPriceMax(max));
            }
        } catch (NumberFormatException ignored) {

        }

        return new ProductSearch(searches);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.searchCustomerMarketplaceController = new SearchCustomerMarketplaceController();

        searchStage = ClientApplication.showNewStageFromFXML("search_customer_marketplace_controller.fxml", true, "MarketPlace - Advanced Search", param -> searchCustomerMarketplaceController, StageStyle.UTILITY);
        searchStage.hide();
        searchStage.setOnCloseRequest(event -> {
            searchStage.hide();
            event.consume();
        });
        searchCustomerMarketplaceController.productNameTextField.textProperty().addListener((observable, oldValue, newValue) -> onSearchControllerAreUpdated());
        searchCustomerMarketplaceController.maxTextField.textProperty().addListener((observable, oldValue, newValue) -> onSearchControllerAreUpdated());
        searchCustomerMarketplaceController.minPriceTextField.textProperty().addListener((observable, oldValue, newValue) -> onSearchControllerAreUpdated());
        searchCustomerMarketplaceController.beforeDeliveryDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> onSearchControllerAreUpdated());

        amountProductSpinner.setEditable(true);
        amountProductSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));

        if (ClientApplication.loggedUser.type() != User.Type.CUSTOMER) {
            buyButton.setDisable(true);
            amountProductSpinner.setDisable(true);
            showBasketButton.setDisable(true);
        }

        final Set<Product> allProducts = ClientApplication
                .marketPlaceRepository
                .productRepository()
                .getAllProducts();
        tableView.setItems(FXCollections.observableArrayList(generateDataForTableView(allProducts)));

        productNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        sellerNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("sellerName"));
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<>("priceComponent"));
        imageTableColumn.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        estimatedDeliveryTableColumn.setCellValueFactory(new PropertyValueFactory<>("estimatedDelivery"));
    }

    private void onSearchControllerAreUpdated() {
        Platform.runLater(() -> {
            tableView.getItems().clear();

            final Set<Product> products = ClientApplication
                    .marketPlaceRepository
                    .productRepository()
                    .search(generateProductSearch());

            final List<Data> data = generateDataForTableView(products);

            final LocalDate value = searchCustomerMarketplaceController.beforeDeliveryDatePicker.getValue();
            if (value != null) {
                data.removeIf(data1 -> data1.estimatedDelivery.isAfter(value));
            }

            tableView.getItems().addAll(
                    data
            );
        });
    }

    public void onSearchByClicked(MouseEvent mouseEvent) {
        if (searchStage.isShowing()) {
            searchStage.hide();
        } else {
            searchStage.show();
            searchStage.toFront();
        }
    }

    public static class Data {
        private PrettyMonetaryAmount price;
        private UUID productId;
        private ImageView imageView;
        private String productName;
        private Object priceComponent;
        private Username sellerName;
        private LocalDate estimatedDelivery;

        public Data(UUID productId, ImageView imageView, String productName, Object priceComponent, Username sellerName, LocalDate estimatedDelivery, PrettyMonetaryAmount price) {
            this.productId = productId;
            this.imageView = imageView;
            this.productName = productName;
            this.priceComponent = priceComponent;
            this.sellerName = sellerName;
            this.estimatedDelivery = estimatedDelivery;
            this.price = price;
        }

        public Data() {
        }

        public UUID getProductId() {
            return productId;
        }

        public Data setProductId(UUID productId) {
            this.productId = productId;
            return this;
        }

        public PrettyMonetaryAmount getPrice() {
            return price;
        }

        public Data setPrice(PrettyMonetaryAmount price) {
            this.price = price;
            return this;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public Data setImageView(ImageView imageView) {
            this.imageView = imageView;
            return this;
        }

        public String getProductName() {
            return productName;
        }

        public Data setProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public Object getPriceComponent() {
            return priceComponent;
        }

        public Data setPriceComponent(Object priceComponent) {
            this.priceComponent = priceComponent;
            return this;
        }

        public Username getSellerName() {
            return sellerName;
        }

        public Data setSellerName(Username sellerName) {
            this.sellerName = sellerName;
            return this;
        }

        public LocalDate getEstimatedDelivery() {
            return estimatedDelivery;
        }

        public Data setEstimatedDelivery(LocalDate estimatedDelivery) {
            this.estimatedDelivery = estimatedDelivery;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Data data = (Data) o;

            return getProductId() != null ? getProductId().equals(data.getProductId()) : data.getProductId() == null;
        }

        @Override
        public int hashCode() {
            return getProductId() != null ? getProductId().hashCode() : 0;
        }
    }
}
