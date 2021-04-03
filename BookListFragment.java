package temple.edu.bookshelf;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class BookListFragment extends Fragment
{

    private View v;
    private BookSelectedInterface bookSelectedInterface;
    private ArrayList<Book> arrayBooks;
    ListView listView;
    BookAdapter bookAdapter;

    private final static String BOOK = "BOOK";

    interface BookSelectedInterface
    {
        void bookSelected(int position);
    }
    public BookListFragment()
    {
        // Required empty public constructor
    }
    public static BookListFragment newInstance(ArrayList<Book> books)
    {
        BookListFragment newBookListFragment = new BookListFragment();
        Bundle b = new Bundle();
        b.putSerializable(BOOK, books);
        newBookListFragment.setArguments(b);
        return newBookListFragment;
    }

    @Override
    public void onAttach(Context context) throws RuntimeException
    {
        super.onAttach(context);
        if(context instanceof BookSelectedInterface)
        {
            bookSelectedInterface = (BookSelectedInterface)context;
        }
        else
        {
            throw new RuntimeException(getContext() + "must implement BookSelectedInterface");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if( bundle != null)
        {
            arrayBooks = (ArrayList<Book>)bundle.get(BOOK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_list, container, false);
        bookAdapter = new BookAdapter(getContext(),arrayBooks);
        listView = v.findViewById(R.id.bookList);
        if(listView.getParent() != null)
        {
            ((ViewGroup)listView.getParent()).removeView(v);
        }
        listView.setAdapter(bookAdapter);

        listView.setOnItemClickListener((parent, view, position, id) ->
                bookSelectedInterface.bookSelected(position));
        return v;
    }

    public void updateBooks(ArrayList<Book> newBooks)
    {
        arrayBooks = newBooks;
        listView.setAdapter(new BookAdapter(this.getContext(), arrayBooks));
    }
}
