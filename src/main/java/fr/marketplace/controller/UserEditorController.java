package fr.marketplace.controller;

import fr.marketplace.bi.Email;
import fr.marketplace.bi.HashedPassword;
import fr.marketplace.bi.User;
import fr.marketplace.bi.Username;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.RandomStringUtils;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UserEditorController implements Initializable {
    public TextField usernameTextField;
    public TextField emailTextField;
    public TextField hashedPasswordTextField;
    public ComboBox<User.Type> roleComboBox;
    public CheckBox isSubscriberCheckBox;
    public CheckBox isDisableCheckBox;

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
        roleComboBox.setItems(FXCollections.observableArrayList(User.Type.values()));
        roleComboBox.setValue(User.Type.CUSTOMER);

        if (mutableUser.getUsername() != null) {
            usernameTextField.setText(mutableUser.getUsername().username());
        }

        if (mutableUser.getEmail() != null) {
            emailTextField.setText(mutableUser.getEmail().email());
        }

        if (mutableUser.getHashedPassword() != null) {
            hashedPasswordTextField.setText(mutableUser.getHashedPassword().hash());
        }

        if (mutableUser.getType() != null) {
            roleComboBox.setValue(mutableUser.getType());
        }

        isSubscriberCheckBox.setSelected(mutableUser.isSubscriber());
        isDisableCheckBox.setSelected(mutableUser.isSubscriber());
    }

    public static String generatePassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        return pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString() + "@";
    }

    public void onRevealClicked(MouseEvent mouseEvent) {
        final String newPassword = generatePassword();
        final HashedPassword hashedPassword = HashedPassword.hash(newPassword);
        mutableUser.setHashedPassword(hashedPassword);
        hashedPasswordTextField.setText(hashedPassword.hash());
        new Alert(Alert.AlertType.INFORMATION, newPassword, ButtonType.OK).showAndWait();
    }

    public void onConfirmClicked(MouseEvent mouseEvent) {
        if (hashedPasswordTextField.getText().isBlank()) {
            new Alert(Alert.AlertType.ERROR, "Require generate password", ButtonType.OK).showAndWait();
            return;
        }

        try {
            final User user = new User(
                    mutableUser.getId(),
                    Email.of(emailTextField.getText()),
                    Username.of(usernameTextField.getText()),
                    HashedPassword.of(hashedPasswordTextField.getText()),
                    roleComboBox.getValue(),
                    isSubscriberCheckBox.isSelected(),
                    mutableUser.getCreateAt() == null ? ZonedDateTime.now() : mutableUser.getCreateAt(),
                    isDisableCheckBox.isSelected()
            );

            mutableUser.setFrom(user);

            ClientApplication
                    .marketPlaceRepository
                    .userRepository()
                    .put(user);

            if (!userMarketplaceController.tableView.getItems().contains(mutableUser)) {
                userMarketplaceController.tableView.getItems().add(mutableUser);
            }

            userMarketplaceController.tableView.refresh();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}
