package baller.example.sexyvanialoanclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReturnMenuController implements Initializable {

    @FXML
    private TableColumn<LoanModel, String> authorColumn;

    @FXML
    private TableColumn<LoanModel, String> genreMediaTypeColumn;

    @FXML
    private TextField keywordTextField;

    @FXML
    private TableColumn<LoanModel, Date> loanDateColumn;

    @FXML
    private TableColumn<LoanModel, Integer> publicationYearColumn;

    @FXML
    private Button returnButton;

    @FXML
    private TableColumn<LoanModel, Date> returnDateColumn;

    @FXML
    private Button returnToUserMenuButton;

    @FXML
    private TableView<LoanModel> tableView;

    @FXML
    private TableColumn<LoanModel, String> titleColumn;
    private LoanModel selectedLoan;
    private  Database libraryDatabase;
    private ObservableList<LoanModel> loanModelObservableList = FXCollections.observableArrayList();

    private ResultSet loanHistoryResultSet;

    /*This is used to store the data from a row upon clicking that row*/

    /*This variable is used to indicate to the borrower when the media/book should be returned*/
    private LocalDate todaysDate = LocalDate.now();

    @FXML
    void getSelectedItem(MouseEvent event) {
        LoanModel selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            /*The saved selectedMediaBook is used when the borrowButton is clicked, if it´s null we do nothing*/
            this.selectedLoan = selectedItem;


        }
    }

    @FXML
    void onReturnBookMediaButtonClick(ActionEvent event) throws SQLException {
        /*We check for the media-related enums to conclude if the object is some type of media or a book, we should only do something
        if the selectedMediaBook-variable is null we can´t borrow that entry so we check for that first
         */
        if (selectedLoan != null) {
            Alert returnAlert;

            if (selectedLoan.getGenreMediaType().equalsIgnoreCase("dvd") || selectedLoan.getGenreMediaType().equalsIgnoreCase("game") ||
                    selectedLoan.getGenreMediaType().equalsIgnoreCase("magazine") || selectedLoan.getGenreMediaType().equalsIgnoreCase("cd")) {

                libraryDatabase.returnMedia(LibraryLoginController.currentUser.getEmail(), selectedLoan.getTitle());
                returnAlert = new Alert(Alert.AlertType.CONFIRMATION, "Thank you for returning : " + selectedLoan.getTitle(), ButtonType.OK);
                refreshTable();
                returnAlert.show();
            }
            /*If selectedMediaBook isn´t null and isn´t media then we should use borrowBook*/
            else {
                libraryDatabase.returnBook(selectedLoan.getTitle());
                returnAlert = new Alert(Alert.AlertType.CONFIRMATION, "You just returned : " + selectedLoan.getTitle(), ButtonType.OK);
                returnAlert.show();
                refreshTable();


            }

        }
    }

    @FXML
    void returnToUserMenuPage(ActionEvent event) throws IOException {
        File logInFxmlFile = new File("src/main/resources/baller/example/sexyvanialoanclient/user_menu_page.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(logInFxmlFile.toURI().toURL());
        Parent rootLogOut = fxmlLoader.load();
        Scene logInScene = new Scene(rootLogOut);
        Node source = (Node) event.getSource();
        Stage stage1 = (Stage) source.getScene().getWindow();
        stage1.setScene(logInScene);
        stage1.show();
    }
    private void refreshTable() {
        try {
            /*Clears the mediaBookModelObservable list to then refill it*/
            loanModelObservableList.clear();
            loanHistoryResultSet = libraryDatabase.getLoanHistory();
            /*After getting the ResultSet we iterate through it and add our rows to our obeservable list */
            while (loanHistoryResultSet.next()) {
                String title = loanHistoryResultSet.getString("Title");
                String genreMediaType = loanHistoryResultSet.getString("Genre");
                String author = loanHistoryResultSet.getString("Author");
                Date loanDate = loanHistoryResultSet.getDate("Loan date");
                Date returnDate = loanHistoryResultSet.getDate("Return date");

                loanModelObservableList.add(new LoanModel(title, genreMediaType,author,loanDate,returnDate));


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tableView.setItems(loanModelObservableList);


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.libraryDatabase = new Database();
        /*This makes sure we can click one at a time, simplyfying identifying if its media or a book */
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        try {
          ResultSet loanHistoryResultSet = libraryDatabase.getLoanHistory();
            /*After getting the ResultSet we iterate through it and add our rows to our obeservable list */
            while (loanHistoryResultSet.next()) {
                String title = loanHistoryResultSet.getString("Title");
                String genreMediaType = loanHistoryResultSet.getString("Genre");
                String author = loanHistoryResultSet.getString("Author");
                Date loanDate = loanHistoryResultSet.getDate("Loan date");
                Date returnDate = loanHistoryResultSet.getDate("Return date");

                loanModelObservableList.add(new LoanModel(title, genreMediaType, author,loanDate, returnDate));


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*To populate a tableview we need to set a cellfactory that adds each value (to a cell) in each column*/
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreMediaTypeColumn.setCellValueFactory(new PropertyValueFactory<>("genreMediaType"));
        loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        /*After having specified how to insert into each column we can now set the items of the tableview*/
        tableView.setItems(loanModelObservableList);
        tableView.setDisable(false);

        FilteredList<LoanModel> loanModelFilteredList = new FilteredList<>(loanModelObservableList, b -> true);
        keywordTextField.textProperty().addListener((observableValue, s, t1) -> {

            loanModelFilteredList.setPredicate(LoanModel -> {
                /*This makes sure that if updated value inside the textField is null, blank etc. It will return the entire list
                Since return terminates the block it returns all and doesn´t run the other code
                 */
                if(t1.isBlank() || t1.isEmpty() || t1 == null){
                    return true;

                }
                /*See BorrowMenuController for full walk through of filtering logic*/
                String[] searchKeyWord = t1.toLowerCase(Locale.ROOT).split("\\s+");
                boolean allKeywordsMatches = true;
                /*We check all columns to see if it contains what´s written in the textfield, if it is we return the element,*/
                for(int i = 0 ; i < searchKeyWord.length ; i++) {
                   boolean keywordMatch =  (LoanModel.getTitle().toLowerCase().contains(searchKeyWord[i]) ||LoanModel.getGenreMediaType().toLowerCase().contains(searchKeyWord[i]) || (LoanModel.getAuthor().toLowerCase().contains(searchKeyWord[i]) ));


                   if(!keywordMatch){
                       allKeywordsMatches = false;
                       /*To be more programming "pretty" we could change i to searchKeyWord.length + 1*/
                       break;

                   }
                }
                return allKeywordsMatches;

            });

        });
        /*A sorted list is created, a list sorted using our own comparator that sorts on our specific text values.*/
        SortedList<LoanModel> loanModelSortedList = new SortedList<>(loanModelFilteredList);
        /*Binds the list and tableView so that the list of the tableview is updated when the sortedList is updated*/
        loanModelSortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(loanModelSortedList);
    }
}
