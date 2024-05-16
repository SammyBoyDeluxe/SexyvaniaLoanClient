package baller.example.sexyvanialoanclient;

import org.mariadb.jdbc.Connection;
import org.mariadb.jdbc.Statement;
import org.mariadb.jdbc.client.util.Parameter;

import java.sql.*;
import java.time.LocalDate;

/**This class represents the database and is used for querying, getting result sets
 *  and so forth.
 *
 */
public class Database {


        private static final String url = "jdbc:mariadb://localhost:3369/librarydb";
        private static final String user = "root";

        private static final String password = "123";
        private static Connection databaseConnection;

        /**Establishes a connection with the MariaDb database, returns a connection object representing the connection
         * to the database. From this we can create statements and execute queries using those statements, getting a
         * result-set back
         * @throws SQLException e
         */
        public Database() {
            Connection connection = null;
            try {
                databaseConnection = (Connection) DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();



            }
        }

//        public static Connection getDatabaseConnection(){
//            Connection connection = null;
//            try {
//                databaseConnection = (Connection) DriverManager.getConnection(url, user, password);
//            } catch (SQLException e) {
//                e.printStackTrace();
//
//            }
//
//
//        }

        /** This method tries inserting the email, password-pair
         * if the email already has an account the method returns false.
         * If the insertion was successful it returns true
         *
         * @param email
         * @param password
         * @return boolean, true if sign up was completed, false if the username was taken
         * @throws SQLException
         */
        public boolean signUpUser(String email, String password) throws SQLException {
                /*First we create a prepared statement to call our stored procedure and enter the in parameters*/


                /*First we create a prepared statement to call our stored procedure and enter the in parameters*/
                CallableStatement signUpStatement = databaseConnection.prepareCall("{CALL SignUpUser(?,?,?)}");
                signUpStatement.setString(1, email);
                signUpStatement.setString(2, password);
                signUpStatement.registerOutParameter(3, Types.BOOLEAN);

                signUpStatement.execute();

                boolean returnBoolean = signUpStatement.getBoolean(3);
                System.out.println(returnBoolean);

                return returnBoolean;


        }

    /**Registers the media as borrowed on todays date
     *
     *
     * @param emailCurrentUser
     * @param title
     * @throws SQLException
     */

    /**Registers the book as borrowed on today´s date
     *
     *
     * @param emailCurrentUser
     * @param title
     * @throws SQLException
     */
    public void borrowMedia( String emailCurrentUser,String title) throws SQLException {
        /*For the stored procedure we need: emailin, titleOfMedia and today´s date to add to the loan date since you can only borrow when you´re at the l
        library using the client.
         */
        Date todaysDate = Date.valueOf(LocalDate.now());
        CallableStatement borrowStatement = databaseConnection.prepareCall("{CALL BorrowMedia(?,?,?)}");
        borrowStatement.setString(1,emailCurrentUser);
        borrowStatement.setString(2,title);
        borrowStatement.setDate(3, todaysDate );
        borrowStatement.execute();




        }


    public void returnMedia( String emailCurrentUser,String title) throws SQLException {
        /*For the stored procedure we need: emailin, titleOfMedia and today´s date to add to the loan date since you can only borrow when you´re at the l
        library using the client.
         */
        Date todaysDate = Date.valueOf(LocalDate.now());
        CallableStatement preparedCall = databaseConnection.prepareCall("{CALL ReturnMedia(?,?)}");
        preparedCall.setString(1,title);
        preparedCall.setDate(2,todaysDate);
        preparedCall.execute();




    }

    public void returnBook(String title) throws SQLException {
        /*For the stored procedure we need: emailin, titleOfbook and today´s date to add to the return date since you can only borrow when you´re at the l
        library using the client.
         */
        Date todaysDate = Date.valueOf(LocalDate.now());
        CallableStatement preparedCall = databaseConnection.prepareCall("{CALL ReturnBook(?, ?)}");
        preparedCall.setString(1, title);
        preparedCall.setDate(2, todaysDate);
        preparedCall.execute();




    }

    /**Updates the database entry for the current user with a new email
     * via stored procedure
     *
     * @param newEmail
     * @returns true if successful (Unique email entry), false otherwise
     */
    public boolean updateEmail(String newEmail) throws SQLException {
        /*Procedure looks as follows: PROCEDURE `UpdateEmail`(IN emailCurrent VARCHAR(200), IN emailNew VARCHAR(200), OUT success BOOLEAN) */
        CallableStatement updateStatement = databaseConnection.prepareCall("{CALL UpdateEmail(?, ?, ?)}");
        updateStatement.setString(1,LibraryLoginController.currentUser.getEmail());
        updateStatement.setString(2, newEmail);
        updateStatement.registerOutParameter(3, Types.BOOLEAN);

        /*After having prepared the statement and entering our input we can run the query with .execute() and then obtain the out-paramenter from the prep
        statement instance
         */
        updateStatement.execute();

        boolean returnBoolean = updateStatement.getBoolean(3);
        /*If the procedure was successful we want to save the new email as this will be the future identifier*/
        if(returnBoolean) LibraryLoginController.currentUser.setEmail(newEmail);
        System.out.println(returnBoolean);
        return returnBoolean;


    }

    /**Updates the password for the specified user in the database
     *
     *
     * @param newPassword
     */
    public void updatePassword(String newPassword){

        /*The procedure has the following appearance:PROCEDURE `UpdatePassword`(IN emailIn VARCHAR(200), IN passwordIn VARCHAR(40)) */
        try {
            CallableStatement updatePasswordStatement = databaseConnection.prepareCall("{CALL UpdatePassword(?, ?)}");
            updatePasswordStatement.setString(1, LibraryLoginController.currentUser.getEmail());
            updatePasswordStatement.setString(2,newPassword);
            updatePasswordStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void borrowBook( String emailCurrentUser,String title) throws SQLException {
        /*For the stored procedure we need: emailin, titleOfBook and today´s date to add to the loan date since you can only borrow when you´re at the l
        library using the client.
         */
        Date todaysDate = Date.valueOf(LocalDate.now());
        CallableStatement borrowStatement = databaseConnection.prepareCall("{CALL BorrowBook(?,?,?)}");
        borrowStatement.setString(1,emailCurrentUser);
        borrowStatement.setString(2,title);
        borrowStatement.setDate(3, todaysDate );
        borrowStatement.execute();




    }

        /** This method checks the database for a username/password match. If there is one
         * true is returned, otherwise false
         * @param email
         * @param password
         * @return boolean, true if login was completed, false if the username/password pair
         * didn´t exist
         * @throws SQLException
         */
        public boolean logInUser(String email, String password) throws SQLException {
            CallableStatement loginStatement = databaseConnection.prepareCall("{CALL LogInUser(?,?,?)}");
            loginStatement.setString(1,email);
            loginStatement.setString(2,password);
            loginStatement.registerOutParameter(3,Types.BOOLEAN);
            loginStatement.execute();
            boolean returnBoolean = loginStatement.getBoolean(3);
            System.out.println(returnBoolean);
            return returnBoolean;
        }


    /**This method queries the database for all available book and media-entities
     *
     *
     *
     * @return ResultSet [available books and media]
     */
    public ResultSet getAvailableBooksAndMedia() throws SQLException {
            /*We get the availableBooksAndMedia view from the database, obtaining the available (for borrowing) books and media */

            PreparedStatement getAvailableBooksAndMedia = databaseConnection.prepareStatement("SELECT * FROM availablebooksandmediadisplayview");
            return getAvailableBooksAndMedia.executeQuery();





        }

    /**Checks the database and gets all entries from the loaned-section for a the logged in user
     * 
     * @return ResultSet loanedbooksandmediaforuser
     * @throws SQLException
     */
    public ResultSet getLoanHistory() throws SQLException {

            CallableStatement getUserIdStatement = databaseConnection.prepareCall("{CALL GetUserId(?, ?)}");
            getUserIdStatement.setString(1, LibraryLoginController.currentUser.getEmail());
            getUserIdStatement.registerOutParameter(2, Types.INTEGER);
            getUserIdStatement.execute();

            int userId = getUserIdStatement.getInt(2);

            PreparedStatement getLoanedBooksAndMedia = databaseConnection.prepareStatement("SELECT * FROM loanedbooksandmediadisplayview WHERE userid = ?");
            getLoanedBooksAndMedia.setInt(1, userId);

            return getLoanedBooksAndMedia.executeQuery();


//        /*We need to filter the loanedbooksandmedia view since this contains all loaned books and media, we only want those associated with the user
//        and as such we call the GetUserId(emailIn VARCHAR(200), useridout INT)
//         */
//        CallableStatement getUserIdStatement = databaseConnection.prepareCall("{CALL GetUserId(?,?)}");
//        getUserIdStatement.setString(1,LibraryLoginController.currentUser.getEmail());
//        getUserIdStatement.registerOutParameter(2, Types.INTEGER);
//
//        ResultSet userIdResultSet = getUserIdStatement.executeQuery();
//        /*Gets the outputvariable and sets the userid-variable to that value*/
//        int userId = userIdResultSet.getInt(1);
//        /*We get the loanedBooksAndMedia view from the database, obtaining the available (for returning) books and media */
//        PreparedStatement getLoanedBooksAndMedia = databaseConnection.prepareStatement("SELECT * FROM loanedbooksandmediadisplayview WHERE userid = ?");
//        getLoanedBooksAndMedia.setInt(1,userId);
//        return getLoanedBooksAndMedia.executeQuery();





    }


    public void closeDatabaseConnection() throws SQLException {
        databaseConnection.close();


        }


    }

