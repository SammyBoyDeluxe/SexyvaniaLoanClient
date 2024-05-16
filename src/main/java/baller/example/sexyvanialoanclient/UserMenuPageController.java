package baller.example.sexyvanialoanclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javafx.scene.Node;


public class UserMenuPageController {

    @FXML
    private Button borrowButton;

    @FXML
    private Button browseButton;

    @FXML
    private Button logOutButton;

    @FXML
    private Button profileUpdateButton;

    @FXML
    private Button returnButton;

    private Scene scene;
    private Stage stage;
    private Parent root;




    /*Shows a confirmation message that you are logged out and then opens the login page. Also wipes the used email from the currentUser instance*/
    @FXML
    void onLogOutButtonClick(ActionEvent event) throws IOException {
        Alert logOutAlert = new Alert(Alert.AlertType.CONFIRMATION,"You are now logged out, welcome back!", ButtonType.OK);
        File logInFxmlFile = new File("src/main/resources/baller/example/sexyvanialoanclient/library_login_page.fxml");
        Node source = (Node) event.getSource();
        Stage stage1 = (Stage)source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(logInFxmlFile.toURI().toURL());
        Parent rootLogOut = fxmlLoader.load();
        Scene logInScene = new Scene(rootLogOut);
        stage1.setScene(logInScene);
        LibraryLoginController.currentUser.logOutCurrentUser();
        logOutAlert.show();
        stage1.show();




    }

    @FXML
    void openBorrowWindow(ActionEvent event) throws IOException {
        File logInFxmlFile = new File("src/main/resources/baller/example/sexyvanialoanclient/borrow_menu_page.fxml");
        Node source = (Node) event.getSource();
        Stage stage1 = (Stage)source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(logInFxmlFile.toURI().toURL());
        Parent rootLogOut = fxmlLoader.load();
        Scene logInScene = new Scene(rootLogOut);
        stage1.setScene(logInScene);
        stage1.show();

    }

    @FXML
    void openBrowseWindow(ActionEvent event) {

    }

    @FXML
    void openReturnWindow(ActionEvent event) throws IOException {
        File logInFxmlFile = new File("src/main/resources/baller/example/sexyvanialoanclient/return_menu_page.fxml");
        Node source = (Node) event.getSource();
        Stage stage1 = (Stage)source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(logInFxmlFile.toURI().toURL());
        Parent rootLogOut = fxmlLoader.load();
        Scene logInScene = new Scene(rootLogOut);
        stage1.setScene(logInScene);
        stage1.show();

    }

    @FXML
    void openUpdateProfileWindow(ActionEvent event) throws IOException {

        File logInFxmlFile = new File("src/main/resources/baller/example/sexyvanialoanclient/update_profile_menu_page.fxml");
        Node source = (Node) event.getSource();
        Stage stage1 = (Stage)source.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(logInFxmlFile.toURI().toURL());
        Parent rootLogOut = fxmlLoader.load();
        Scene logInScene = new Scene(rootLogOut);
        stage1.setScene(logInScene);
        stage1.show();

    }



}

