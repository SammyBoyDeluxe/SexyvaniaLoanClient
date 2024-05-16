package baller.example.sexyvanialoanclient;

public class MediaBookModel {
    private String title;
   private String genreMediaType;
   private int publicationYear;
   private String author;

    /**This is a data-transfer object for our borrow/return-pages
     *
     *
     *
     * @param title
     * @param genreMediaType
     * @param publicationYear
     * @param author
     */

    public MediaBookModel(String title, String genreMediaType, int publicationYear, String author) {
        this.title = title;
        this.genreMediaType = genreMediaType;
        this.publicationYear = publicationYear;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getGenreMediaType() {
        return genreMediaType;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public String getAuthor() {
        return author;
    }
}
