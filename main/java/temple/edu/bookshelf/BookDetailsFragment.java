package temple.edu.bookshelf;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class BookDetailsFragment extends Fragment
{
    private Book book;
    private TextView title, author;
    private ImageView bookImage;
    private MainActivity mainActivity;
    private Button button;

    public BookDetailsFragment()
    {
        //Required empty default constructor
    }

    public static BookDetailsFragment newInstance()
    {
        return new BookDetailsFragment();
    }

    public static BookDetailsFragment newInstance(Book book, MainActivity parent)
    {
        BookDetailsFragment bookDetailsFragment = new BookDetailsFragment();
        bookDetailsFragment.book = book;
        bookDetailsFragment.mainActivity = parent;
        bookDetailsFragment.displayBook(bookDetailsFragment.book);
        return bookDetailsFragment;
    }

    public void updateBook(Book book)
    {
        this.book = book;
    }

    public void displayBook(Book book)
    {
        this.book = book;
        if (title != null && author != null && bookImage != null && book != null)
        {
            title.setText(book.getTitle());
            author.setText(book.getAuthor());

            try
            {
                Picasso.get().load(Uri.parse(book.getCoverURL())).into(bookImage);
            }
            catch(Exception e){ e.printStackTrace(); }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View v = inflater.inflate(R.layout.fragment_details, null);
        title = v.findViewById(R.id.title1);
        author = v.findViewById(R.id.author1);
        bookImage = v.findViewById(R.id.bookCover);
        button = v.findViewById(R.id.playThisBook);

        if(this.book != null)
            this.displayBook(this.book);

        button.setOnClickListener(v1 -> mainActivity.playThisBook());

        return v;
    }
}
