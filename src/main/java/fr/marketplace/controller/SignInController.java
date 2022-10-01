package fr.marketplace.controller;

import fr.marketplace.bi.Email;
import fr.marketplace.bi.HashedPassword;
import fr.marketplace.bi.User;
import fr.marketplace.bi.Username;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.time.ZonedDateTime;
import java.util.UUID;

public class SignInController {

    public TextField emailTextField;
    public PasswordField passwordTextField;
    public TextField usernameTextField;

    public void onConfirmButtonClicked(MouseEvent mouseEvent) {
        try {
            final Email email = Email.of(emailTextField.getText());
            final Username username = Username.of(usernameTextField.getText());
            final HashedPassword hashedPassword = HashedPassword.hash(passwordTextField.getText());

            final User newUser = new User(
                    UUID.randomUUID(),
                    email,
                    username,
                    hashedPassword,
                    User.Type.CUSTOMER,
                    false,
                    ZonedDateTime.now()
            );

            ClientApplication
                    .marketPlaceRepository
                    .userRepository()
                    .put(newUser);

            ClientApplication.changeSceneFromFXML("login_controller.fxml", false, "MarketPlace - Login");
        } catch (HashedPassword.PasswordBadPatternException e) {
            new Alert(Alert.AlertType.ERROR, "Password is not valid\n• a digit must occur at least once\n• a lower case letter must occur at least once\n• an upper case letter must occur at least once\n• a special character must occur at least once\n• no whitespace allowed in the entire string\n• at least 8 characters", ButtonType.OK).showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}
