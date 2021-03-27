package temple.edu.bookshelf;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BookDetailsFragment extends Fragment
{
    private Book book;
    TextView title;
    TextView author;

    public BookDetailsFragment() {
        //Required empty default constructor
    }

    public static BookDetailsFragment newInstance() {
        return new BookDetailsFragment();
    }

    public static BookDetailsFragment newInstance(Book book)
    {
        BookDetailsFragment BookDetailsFragment = new BookDetailsFragment();
        BookDetailsFragment.book = book;
        BookDetailsFragment.displayBook(BookDetailsFragment.book);
        return BookDetailsFragment;
    }

    public void updateBook(Book book) {
        this.book = book;
    }

    public void displayBook(Book book) {
        this.book = book;
        if (title != null && author != null && book != null) {
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.details_fragment, null);
        title = v.findViewById(R.id.textTitle);
        author = v.findViewById(R.id.textAuthor);
        if(this.book != null)
            this.displayBook(this.book);

        return v;
    }
}
