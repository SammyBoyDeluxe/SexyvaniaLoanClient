package baller.example.sexyvanialoanclient;

public class CurrentUser {



    private String email;

    /**This class holds the last email that was matched with a email/password pair in the database.
     * A users email is used in the stored procedure as an identifier for the specific user and is the only thing
     * we need to save since this gives us access to both loaning to that user and finding the users loanhistory
     * @param emailIn
     */
    public CurrentUser(String emailIn){
        email = emailIn;



    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    /**Wipes the email entry from the CurrentUser instance.
     *
     *
     */
    public void logOutCurrentUser(){
        email = "";




    }

}
