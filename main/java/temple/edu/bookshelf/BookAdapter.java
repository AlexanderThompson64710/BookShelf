package temple.edu.bookshelf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter
{
    private ArrayList<Book> books;
    private Context context;
    private LayoutInflater inflater;

    public BookAdapter(Context context, ArrayList<Book> bookArray)
    {
        this.context = context;
        this.books = bookArray;
        inflater = LayoutInflater.from(context);
    }

    public void add(Book book) { books.add(book); }

    public void remove(Book book)
    {
        books.remove(book);
    }

    public Book get(int index)
    {
        return books.get(index);
    }

    public int size()
    {
        return books.size();
    }

    public ArrayList<Book> getBookArrayList()
    {
        return books;
    }

    @Override
    public int getCount()
    {
        return books.size();
    }

    @Override
    public Object getItem(int position)
    {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Inflate the convertView
        convertView = inflater.inflate(R.layout.fragment_list, parent, false);

        //Set book title
        TextView title = convertView.findViewById(R.id.title);
        title.setText(books.get(position).getTitle());

        //Set book author
        TextView author = convertView.findViewById(R.id.author);
        author.setText(books.get(position).getAuthor());

        return convertView;
    }
}
