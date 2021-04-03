package temple.edu.bookshelf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.squareup.picasso.Picasso;


public class BookDetailsFragment extends Fragment
{

    private Book book;
    private TextView title;
    private TextView author;
    private ImageView coverImage;
    static String BOOK_TITLE = "BOOK_TITLE";
    static String BOOK_AUTHOR = "BOOK_AUTHOR";
    static String BOOK_IMAGE_LINK = "BOOK_IMAGE_LINK";

    public BookDetailsFragment()
    {
        // Required empty public constructor
    }

    public static BookDetailsFragment newInstance(Book book)
    {
        BookDetailsFragment newDetailsFragment = new BookDetailsFragment();
        Bundle b = new Bundle();
        b.putString(BOOK_TITLE,book.getTitle());
        b.putString(BOOK_AUTHOR,book.getAuthor());
        b.putString(BOOK_IMAGE_LINK,book.getCoverUrl());
        newDetailsFragment.setArguments(b);
        return newDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            book = new Book(bundle.getString(BOOK_TITLE), bundle.getString(BOOK_AUTHOR), bundle.getString(BOOK_IMAGE_LINK));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        title = v.findViewById(R.id.bookTitle);
        author = v.findViewById(R.id.bookAuthor);
        coverImage = v.findViewById(R.id.coverImage);
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        Picasso.get().load(book.getCoverUrl()).into(coverImage);

        return v;
    }

    public void update(String title, String author, String coverUrl)
    {
        TextView titles = getView().findViewById(R.id.bookTitle);
        TextView authors = getView().findViewById(R.id.bookAuthor);
        ImageView cover = getView().findViewById(R.id.coverImage);
        titles.setText(title);
        authors.setText(author);
        Picasso.get().load(coverUrl).into(cover);

    }

}
