package baller.example.sexyvanialoanclient;

import java.sql.Date;

public class LoanModel {
    private String title;
    private String genreMediaType;

    private String author;
    private Date loanDate;
    private Date returnDate;

    public LoanModel(String title, String genreMediaType,  String author, Date loanDate, Date returnDate) {
        this.title = title;
        this.genreMediaType = genreMediaType;
        this.author = author;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    public String getGenreMediaType() {
        return genreMediaType;
    }


    public Date getLoanDate() {
        return loanDate;
    }

    public String getTitle() {
        return title;
    }

    public Date getLoandate() {
        return loanDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public String getAuthor() {
        return author;
    }

    public LoanModel(String title, Date loandate, Date returnDate, String author) {
        this.title = title;
        this.loanDate = loandate;
        this.returnDate = returnDate;
        this.author = author;
    }
}
