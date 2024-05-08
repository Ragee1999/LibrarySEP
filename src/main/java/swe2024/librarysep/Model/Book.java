package swe2024.librarysep.Model;

public class Book {

    private Integer bookId;
    private String title;
    private String author;
    private Integer releaseYear;


    public Book(Integer bookId, String title, String author, Integer releaseYear) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
    }


    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
}
