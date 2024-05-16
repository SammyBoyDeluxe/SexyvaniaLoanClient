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
import java.sql.SQLException;

public class LibraryLoginController {
    /*False if signUpcheckbox not checked, true otherwise*/
    private boolean signUp = false;
    /*This instance of currentUser can be used to track active user via the entered email, static since we always want to be able to save it
    to use it from controllers
     */
    public static CurrentUser currentUser;
    @FXML
    private TextField emailTextField;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox signUpCheckbox;

    @FXML
    private Label welcomeText;
    /*This creates a database-object that we can use to query the database. It should be accessible from all controllers*/
    private Database libraryDatabase = new Database();

    @FXML
    void onLoginButtonClick(ActionEvent event) throws SQLException, IOException {

        /*We make sure to get the current stage so we later can use it show the next scene.
       This is accomplished using the JavaFx Node that define an element as a node in JavaFX hierarchy and as
          such its events happen in the stage we want to use*/
        Node source = (Node) event.getSource();
        Stage stage1 = (Stage)source.getScene().getWindow();

        /*We check whether there´s information entered into the fields, if there isn´t we do nothing*/
        if(!(emailTextField.getText().isBlank()) && !(passwordField.getText().isBlank())) {
            /*The signUpUser() and logInUser() methods both return booleans that indicate
            whether there´s a match in the database. We store this value in operationsuccess
             */
            boolean operationSuccess;
            Alert operationAlert;
            /*Depending on the signUp boolean we know what method to run, signUpUser() or logInUser()*/
            if (signUp == true) {

                operationSuccess = libraryDatabase.signUpUser(emailTextField.getText(), passwordField.getText());

            } else {
                operationSuccess = libraryDatabase.logInUser(emailTextField.getText(),passwordField.getText());

            }
                /*The following else-if statements check what method we ran and post alerts depending on the result*/
            if(signUp == true && operationSuccess == true){
                operationAlert = new Alert(Alert.AlertType.INFORMATION,"Sign up completed! You are now logged in",ButtonType.OK);
                operationAlert.show();
                /*On accepted, unique email we change to the user menu and save the email into the currentUser variable for later access*/
                currentUser = new CurrentUser(emailTextField.getText());
                libraryDatabase.closeDatabaseConnection();
                switchScene(stage1);
            }
            else if(signUp == true && operationSuccess == false){
                operationAlert = new Alert(Alert.AlertType.INFORMATION,"This email already has an associated account, please enter another email",ButtonType.OK);
                operationAlert.show();
            }
            else  if(signUp == false && operationSuccess == true){
                operationAlert = new Alert(Alert.AlertType.INFORMATION,"Log in completed! You are now logged in",ButtonType.OK);
                operationAlert.show();
                /*Upon accepted user/pass-pair we change scene and save the email into the currentUser variable for later access*/
                currentUser = new CurrentUser(emailTextField.getText());
                libraryDatabase.closeDatabaseConnection();
                switchScene(stage1);
            }
            else  if(signUp == false && operationSuccess == false){
                operationAlert = new Alert(Alert.AlertType.INFORMATION,"The account information doesn´t exist in our database, please try again",ButtonType.OK);
                operationAlert.show();
            }
        }
    }

    @FXML
    void onSignUpCheckBoxClick(ActionEvent event) {
        signUp = signUpCheckbox.isSelected();
        /*Depending on whether the sign up box or not the login button changes to reflect the action*/
        /*Since it starts on false we check when it changes as first condition*/
        if(signUp == true){
            loginButton.setText("Sign up");


        }
        else{
            loginButton.setText("Log in");

        }
    }


    /**This message takes in the stage and changes to the usermenupage
     *
     *
     * @param stage1
     * @throws IOException
     */
    private void switchScene(Stage stage1) throws IOException {
        File logInFxmlFile = new File("src/main/resources/baller/example/sexyvanialoanclient/user_menu_page.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(logInFxmlFile.toURI().toURL());
        Parent rootLogOut = fxmlLoader.load();
        Scene logInScene = new Scene(rootLogOut);
        stage1.setScene(logInScene);
        stage1.show();



    }

}
