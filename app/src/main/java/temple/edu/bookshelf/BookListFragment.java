package temple.edu.bookshelf;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class BookListFragment extends Fragment {

    private BookList bookList;
    private ListView listview;
    private MainActivity mainActivity;

    public BookListFragment() {
        //Required empty default constructor
    }

    public static BookListFragment newInstance() {
        return new BookListFragment();
    }

    public static BookListFragment newInstance(BookList booklist, MainActivity parent)
    {
        BookListFragment bookListFragment = new BookListFragment();
        bookListFragment.bookList = booklist;
        bookListFragment.mainActivity = parent;
        return bookListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.booklist_fragment, null);
        listview = v.findViewById(R.id.listFragment);
        listview.setAdapter(this.bookList);
        listview.setOnItemClickListener((parent, view, position, id) -> mainActivity.selectBook(position));
        return v;
    }
}