package baller.example.sexyvanialoanclient;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.ResourceBundle;

public class BorrowMenuController implements Initializable {

    @FXML
    private TableColumn<MediaBookModel, String> authorColumn;

    @FXML
    private TableColumn<MediaBookModel, String> genreMediaTypeColumn;

    @FXML
    private TableColumn<MediaBookModel, Integer> publicationYearColumn;

    @FXML
    private TableView<MediaBookModel> tableView;

    @FXML
    private TableColumn<MediaBookModel, String> titleColumn;


    @FXML
    private Button borrowButton;


    @FXML
    private Button returnButton;

    @FXML
    private TextField keywordTextField;
    /*We create a Database instance to use in the BorrowMenu*/
    private Database libraryDatabase = new Database();

    private ObservableList<MediaBookModel> mediaBookModelObservableList = FXCollections.observableArrayList();

    private ResultSet availableBooksAndMediaResultSet;

    /*This is used to store the data from a row upon clicking that row*/

    private MediaBookModel selectedMediaBook;
    /*This variable is used to indicate to the borrower when the media/book should be returned*/
    private LocalDate todaysDate = LocalDate.now();


    /*This is used to avoid a null pointer exception on running, since our tableColumns otherwise aren´t instantiated when we try to launch the scene*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*This makes sure we can click one at a time, simplyfying identifying if its media or a book */
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        try {
            availableBooksAndMediaResultSet = libraryDatabase.getAvailableBooksAndMedia();
            /*After getting the ResultSet we iterate through it and add our rows to our obeservable list */
            while (availableBooksAndMediaResultSet.next()) {
                String title = availableBooksAndMediaResultSet.getString("Title");
                String genreMediaType = availableBooksAndMediaResultSet.getString("Genre/Media-type");
                int pubYear = availableBooksAndMediaResultSet.getInt("Pub.year");
                String author = availableBooksAndMediaResultSet.getString("Author");

                mediaBookModelObservableList.add(new MediaBookModel(title, genreMediaType, pubYear, author));


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        /*To populate a tableview we need to set a cellfactory that adds each value (to a cell) in each column*/
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreMediaTypeColumn.setCellValueFactory(new PropertyValueFactory<>("genreMediaType"));
        publicationYearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));

        /*After having specified how to insert into each column we can now set the items of the tableview*/
        tableView.setItems(mediaBookModelObservableList);
        tableView.setDisable(false);

        FilteredList<MediaBookModel> mediaBookModelFilteredList = new FilteredList<>(mediaBookModelObservableList, b -> true);
        keywordTextField.textProperty().addListener((observableValue, s, t1) -> {

            mediaBookModelFilteredList.setPredicate(MediaBookModel -> {
                /*This makes sure that if updated value inside the textField is null, blank etc. It will return the entire list
                Since return terminates the block it returns all and doesn´t run the other code
                 */
                if(t1.isBlank() || t1.isEmpty() || t1 == null){
                    return true;

                }
                /*However if the textField isn´t empty we want to update our filtered list. To be able to check on not just one keyword
                we split the String on each word in it and search for that. The regex means split on one or more whitespace characters
                 */
                String[] searchKeyWords = t1.toLowerCase().split("\\s+");
                /*We set alllkeywords match to true as standard and then*/
                boolean allKeywordsMatch = true;
                for(int i = 0 ; i < searchKeyWords.length ; i++) {
                    /*Since we have the predicate b->true that means that all rows are retrieved as standard, changing a specific
                     We will then iterate through the list of rows and give them a false value if and only if it doesn´t include any
                     of the substrings in the textField (separated by one or more spaces). MediaBookModel as a parameter refers to a single
                     row as we go through the iteration of the list.
                     */
                    boolean keywordMatches = (MediaBookModel.getTitle().toLowerCase().contains(searchKeyWords[i]) ||
                            MediaBookModel.getGenreMediaType().toLowerCase().contains(searchKeyWords[i]) ||
                            MediaBookModel.getAuthor().toLowerCase().contains(searchKeyWords[i]) ||
                            Integer.toString(MediaBookModel.getPublicationYear()).contains(searchKeyWords[i]));
                    /*So for each keyword in the textfield we see whether that keyword exists in ANY of the columns,
                     if it doesn´t then for at least one keyword in the textfield, that keyword doesn´t exist in any of the columns.
                     Then we know that all the keywords doesn´t match. As such the lower if-statement. If that happens we don´t need to
                     check the rest of the collection of the keywords, we can exit the for-loop
                     */
                    if (!keywordMatches) {
                        allKeywordsMatch = false;
                        break;
                    }
                }
                /*When the loop terminates either we know that all keywords match, then we want the table to show that row.,
                or all keywords doesn´t match in which case we don´t want that to be included in the filtered selection. We can return allkeywordsmatch
                for each row to see whether that row matches our filtering or not
                 */

                return allKeywordsMatch;
            });

        });
        /*A sorted list is created, a list sorted using our own comparator that sorts on our specific text values.*/
        SortedList<MediaBookModel> mediaBookModelSortedList = new SortedList<>(mediaBookModelFilteredList);
        /*Binds the list and tableView so that the list of the tableview is updated when the sortedList is updated*/
        mediaBookModelSortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(mediaBookModelSortedList);


    }

    /**
     * Checks the database and validates the list with available books and media
     */
    private void refreshTable() {
        try {
            /*Clears the mediaBookModelObservable list to then refill it*/
            mediaBookModelObservableList.clear();
            availableBooksAndMediaResultSet = libraryDatabase.getAvailableBooksAndMedia();
            /*After getting the ResultSet we iterate through it and add our rows to our obeservable list */
            while (availableBooksAndMediaResultSet.next()) {
                String title = availableBooksAndMediaResultSet.getString("Title");
                String genreMediaType = availableBooksAndMediaResultSet.getString("Genre/Media-type");
                int pubYear = availableBooksAndMediaResultSet.getInt("Pub.year");
                String author = availableBooksAndMediaResultSet.getString("Author");

                mediaBookModelObservableList.add(new MediaBookModel(title, genreMediaType, pubYear, author));


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tableView.setItems(mediaBookModelObservableList);


    }


    @FXML
    void onBorrowButtonClick(ActionEvent event) throws SQLException {
        /*We check for the media-related enums to conclude if the object is some type of media or a book, we should only do something
        if the selectedMediaBook-variable is null we can´t borrow that entry so we check for that first
         */
        if (selectedMediaBook != null) {
            Alert borrowAlert;
            if (selectedMediaBook.getGenreMediaType().equalsIgnoreCase("dvd") || selectedMediaBook.getGenreMediaType().equalsIgnoreCase("game") ||
                    selectedMediaBook.getGenreMediaType().equalsIgnoreCase("magazine") || selectedMediaBook.getGenreMediaType().equalsIgnoreCase("cd")) {

                libraryDatabase.borrowMedia(LibraryLoginController.currentUser.getEmail(), selectedMediaBook.getTitle());
                borrowAlert = new Alert(Alert.AlertType.CONFIRMATION, "You just borrowed " + selectedMediaBook.getTitle() + "\n It should be returned by: " + todaysDate.plusDays(10).toString());
                refreshTable();
            }
            /*If selectedMediaBook isn´t null and isn´t media then we should use borrowBook*/
            else {
                libraryDatabase.borrowBook(LibraryLoginController.currentUser.getEmail(), selectedMediaBook.getTitle());
                borrowAlert = new Alert(Alert.AlertType.CONFIRMATION, "You just borrowed " + selectedMediaBook.getTitle() + "\n It should be returned by: " + todaysDate.plusDays(30).toString());
                refreshTable();

            }

        }

    }

    @FXML
    void getSelectedItem(MouseEvent event) {

        MediaBookModel selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            /*The saved selectedMediaBook is used when the borrowButton is clicked, if it´s null we do nothing*/
            this.selectedMediaBook = selectedItem;


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


}