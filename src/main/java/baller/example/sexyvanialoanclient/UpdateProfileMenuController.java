package baller.example.sexyvanialoanclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;

public class UpdateProfileMenuController {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button returnToUserMenuButton;

    @FXML
    private Button updateEmailButton;

    @FXML
    private Button updatePasswordButton;

    @FXML
    private Label welcomeText;

    private Database libraryDatabase = new Database();

    @FXML
    void onUpdateEmailButtonClick(ActionEvent event) throws SQLException {
        /*When we complete the action we want the user to find out the result of their action*/
        Alert operationAlert;
        /*If the email field is empty when we click the button we don´t want it to do anything, so we check for that*/
        if(!(emailTextField.getText().isBlank()) || !(emailTextField.getText().isEmpty())){
           boolean updateEmailSuccess = libraryDatabase.updateEmail(emailTextField.getText());
           if(updateEmailSuccess){
               operationAlert = new Alert(Alert.AlertType.CONFIRMATION,"Your email was successfully updated!",ButtonType.OK);
               operationAlert.show();

           }
           else{
               operationAlert = new Alert(Alert.AlertType.CONFIRMATION,"The email is already registered in our database, please choose another one", ButtonType.OK);
               operationAlert.show();
           }

        }

    }

    @FXML
    void onUpdatePasswordButtonPress(ActionEvent event) {

        /*If the passwordfield is empty when we click the button we don´t want it to do anything, so we check for that*/
        if(!(passwordField.getText().isBlank()) || !(passwordField.getText().isEmpty())){
           libraryDatabase.updatePassword(passwordField.getText());
            /*There are no specific checks for passwords, they don´t need to be unique so if it runs without SQL-exceptions it´s all good*/
            Alert operationAlert = new Alert(Alert.AlertType.CONFIRMATION,"Your password has been updated!",ButtonType.OK);
        }
    }

    @FXML
    void returnToUserMenu(ActionEvent event) throws IOException, SQLException {
        File logInFxmlFile = new File("src/main/resources/baller/example/sexyvanialoanclient/user_menu_page.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(logInFxmlFile.toURI().toURL());
        Parent rootLogOut = fxmlLoader.load();
        Scene logInScene = new Scene(rootLogOut);
        Node source = (Node) event.getSource();
        Stage stage1 = (Stage) source.getScene().getWindow();
        stage1.setScene(logInScene);
        libraryDatabase.closeDatabaseConnection();
        stage1.show();

    }

}

