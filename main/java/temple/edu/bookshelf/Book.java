package temple.edu.bookshelf;


public class Book
{
    private final String title, author, coverURL;
    private final int bookId, duration;

    public Book(int bookId, String title, String author, String coverURL, int duration)
    {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.coverURL = coverURL;
        this.duration = duration;
    }

    public int getBookId() { return bookId; }

    public String getTitle() { return title; }

    public String getAuthor() { return author; }

    public String getCoverURL() { return coverURL; }

    public int getDuration() { return duration; }

}
