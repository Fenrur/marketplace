package fr.marketplace.controller;

import fr.marketplace.bi.Email;
import fr.marketplace.bi.User;
import fr.marketplace.bi.Username;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.UUID;

public class UserMarketplaceController implements Initializable {
    public TableColumn<MutableUser, UUID> userIdTableColumn;
    public TableColumn<MutableUser, Email> emailNameTableColumn;
    public TableColumn<MutableUser, Username> usernameTableColumn;
    public TableColumn<MutableUser, ComboBox<User.Type>> userTypeColumn;
    public TableColumn<MutableUser, CheckBox> isSubscriberColumn;
    public TableColumn<MutableUser, CheckBox> isDisableColumn;
    public TableColumn<MutableUser, ZonedDateTime> createdAtColumn;
    public TableView<MutableUser> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (ClientApplication.loggedUser.isDisable() || ClientApplication.loggedUser.type() != User.Type.MARKETPLACE) return;

        tableView.setItems(FXCollections.observableArrayList(
                ClientApplication
                        .marketPlaceRepository
                        .userRepository()
                        .getAllUsers()
                        .stream()
                        .map(MutableUser::from)
                        .toList()
        ));

        userIdTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        emailNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        usernameTableColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createAt"));
        isDisableColumn.setCellValueFactory(param -> {
            final CheckBox checkBox = new CheckBox();
            checkBox.setSelected(param.getValue().isDisable());
            checkBox.setFocusTraversable(false);
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                param.getValue().setDisable(newValue);

                final User user = param.getValue().build();

                ClientApplication
                        .marketPlaceRepository
                        .userRepository()
                        .put(user);
            });
            return new SimpleObjectProperty<>(checkBox);
        });

        isSubscriberColumn.setCellValueFactory(param -> {
            final CheckBox checkBox = new CheckBox();
            checkBox.setSelected(param.getValue().isSubscriber());
            checkBox.setFocusTraversable(false);
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                param.getValue().setDisable(newValue);

                final User user = param.getValue().build();

                ClientApplication
                        .marketPlaceRepository
                        .userRepository()
                        .put(user);
            });
            return new SimpleObjectProperty<>(checkBox);
        });

        userTypeColumn.setCellValueFactory(param -> {
            final ComboBox<User.Type> typeComboBox = new ComboBox<>(
                FXCollections.observableArrayList(User.Type.values())
            );

            typeComboBox.setValue(param.getValue().getType());
            typeComboBox.setFocusTraversable(false);

            typeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                param.getValue().setType(newValue);

                final User user = param.getValue().build();

                ClientApplication
                        .marketPlaceRepository
                        .userRepository()
                        .put(user);
            });

            return new SimpleObjectProperty<>(typeComboBox);
        });
    }

    public void onNewClicked(MouseEvent mouseEvent) {
        if (ClientApplication.loggedUser.isDisable() || ClientApplication.loggedUser.type() != User.Type.MARKETPLACE) return;

        final MutableUser mutableUser = new MutableUser();
        mutableUser.setId(UUID.randomUUID());
        ClientApplication.showNewStageFromFXML("user_editor_controller.fxml", true, "MarketPlace - Edit Product", param -> new UserEditorController(mutableUser, this));
    }


    public void onEditClicked(MouseEvent mouseEvent) {
        if (ClientApplication.loggedUser.isDisable() || ClientApplication.loggedUser.type() != User.Type.MARKETPLACE) return;

        final MutableUser mutableUser = tableView.getSelectionModel().getSelectedItem();
        if (mutableUser != null) {
            ClientApplication.showNewStageFromFXML("user_editor_controller.fxml", true, "MarketPlace - Edit User", param -> new UserEditorController(mutableUser, this));
        }
    }

    public void onBackClicked(MouseEvent mouseEvent) {
        ClientApplication.changeSceneFromFXML("select_view_type_marketplace.fxml", false, "MarketPlace - Select");
    }
}
