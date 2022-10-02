package fr.marketplace.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class UserEditorController implements Initializable {
    public TextField usernameTextField;
    public TextField emailTextField;
    public TextField hashedPasswordTextField;

    private MutableUser mutableUser;
    private UserMarketplaceController userMarketplaceController;

    public UserEditorController(MutableUser mutableUser, UserMarketplaceController userMarketplaceController) {
        this.mutableUser = mutableUser;
        this.userMarketplaceController = userMarketplaceController;
    }

    public UserEditorController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameTextField.setText(mutableUser.getUsername().username());
        emailTextField.setText(mutableUser.getEmail().email());
        hashedPasswordTextField.setText(mutableUser.getHashedPassword().hash());
    }

    public void onRevealClicked(MouseEvent mouseEvent) {
    }

    public void onConfirmClicked(MouseEvent mouseEvent) {
    }
}
