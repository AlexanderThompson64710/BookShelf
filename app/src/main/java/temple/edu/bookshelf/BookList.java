package temple.edu.bookshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookList extends BaseAdapter
{
    private ArrayList<Book> bookArray;
    private Context context;
    private LayoutInflater inflater;

    public BookList(Context context, ArrayList<Book> bookArray)
    {
        this.context = context;
        this.bookArray = bookArray;
        inflater = LayoutInflater.from(context);
    }

    public void add(Book book) {
        bookArray.add(book);
    }

    public void remove(Book book) {
        bookArray.remove(book);
    }

    public Book get(int index) {
        return bookArray.get(index);
    }

    public int size() {
        return bookArray.size();
    }

    public ArrayList<Book> getBookArrayList() {
        return bookArray;
    }

    @Override
    public int getCount() {
        return bookArray.size();
    }

    @Override
    public Object getItem(int position) {
        return bookArray.get(position);
    }

    //Required implementation when extending base adapter (Not currently used)
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.booklist_fragment, parent, false);

        //Set up text for title
        TextView title = convertView.findViewById(R.id.title);
        title.setText(bookArray.get(position).getTitle());

        //Set up text for author
        TextView author = convertView.findViewById(R.id.author);
        author.setText(bookArray.get(position).getAuthor());

        return convertView;
    }
}
