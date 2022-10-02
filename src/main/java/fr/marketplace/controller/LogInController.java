package fr.marketplace.controller;

import fr.marketplace.bi.Email;
import fr.marketplace.bi.HashedPassword;
import fr.marketplace.bi.User;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LogInController {
    public TextField emailTextField;
    public PasswordField passwordTextField;

    public void onLoginButtonClicked(MouseEvent mouseEvent) {
        final Email loginEmail = Email.of(emailTextField.getText());
        final HashedPassword loginHashedPassword = HashedPassword.hash(passwordTextField.getText());

        ClientApplication
                .marketPlaceRepository
                .userRepository()
                .stream()
                .filter(user -> user.email().equals(loginEmail) && user.hashedPassword().equals(loginHashedPassword))
                .findFirst()
                .ifPresentOrElse(user -> {
                    ClientApplication.loggedUser = user;

                    if (user.isDisable()) {
                        new Alert(Alert.AlertType.WARNING, "You user account are disable", ButtonType.OK).showAndWait();
                        return;
                    }

                    if (user.type() == User.Type.CUSTOMER) {
                        ClientApplication.changeSceneFromFXML("customer_marketplace_controller.fxml", true, "MarketPlace");
                    } else {
                        ClientApplication.changeSceneFromFXML("select_view_type_marketplace.fxml", true, "MarketPlace - Select");
                    }
                }, () -> new Alert(Alert.AlertType.WARNING, "Account does not exist", ButtonType.OK).showAndWait());
    }

    public void onSigninButtonClicked(MouseEvent mouseEvent) {
        ClientApplication.changeSceneFromFXML("signin_controller.fxml", false, "MarketPlace - SignIn");
    }
}
