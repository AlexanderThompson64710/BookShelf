package temple.edu.bookshelf;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class BookListFragment extends Fragment
{
    private BookAdapter bookAdapter;
    private ListView listview;
    private MainActivity parentAct;

    public BookListFragment() {
        //Required empty default constructor
    }

    public static BookListFragment newInstance()
    {
        return new BookListFragment();
    }

    public static BookListFragment newInstance(BookAdapter bookAdapter, MainActivity parent)
    {
        BookListFragment bookListFragment = new BookListFragment();
        bookListFragment.bookAdapter = bookAdapter;
        bookListFragment.parentAct = parent;
        return bookListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View v = inflater.inflate(R.layout.fragment_list, null);
        listview = v.findViewById(R.id.list_frame);
        listview.setAdapter(this.bookAdapter);
        listview.setOnItemClickListener((parent, view, position, id) -> parentAct.selectBook(position));
        return v;
    }
}