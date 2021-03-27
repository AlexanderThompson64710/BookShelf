package temple.edu.bookshelf;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    BookList myBooks;
    Book books;
    BookDetailsFragment details;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean landscape = (findViewById(R.id.detailFragment) != null);

        //Retrieve title and author from the book-name-array.xml
        Resources res = getResources();
        String[] titles = res.getStringArray(R.array.books);
        String[] authors = res.getStringArray(R.array.authors);

        //Add books and authors to the book array list
        ArrayList<Book> bookAL = new ArrayList<Book>();
        for(int i = 0; i < titles.length; i++)
        {
            bookAL.add(new Book(titles[i], authors[i]));
        }

        myBooks = new BookList(this, bookAL);
        books= null;
        //Start up with book list first
        loadFragment(R.id.listFragment, BookListFragment.newInstance(myBooks, this), false);

        //If in landscape mode start with two fragment setup
        details = BookDetailsFragment.newInstance(books);
        if (landscape)
            loadFragment(R.id.detailFragment, details, false);

    }

    public void selectBook(int position)
    {
        books = myBooks.get(position);

        boolean landscape = (findViewById(R.id.detailFragment) != null);

        //If not in landscape mode set it to portrait mode with book list as its fragment.
        if(!landscape)
        {
            Fragment newFrag = BookDetailsFragment.newInstance(books);
            details = (BookDetailsFragment)newFrag;
            loadFragment(R.id.listFragment, newFrag, true);
        }
        else //We update the book displayed
        {
            details.displayBook(books);
        }
    }

    // Load fragments
    private void loadFragment(int paneId, Fragment fragment, boolean placeOnBackstack)
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction()
                .replace(paneId, fragment);
        if (placeOnBackstack)
            ft.addToBackStack(null);
        ft.commit();

        //Used to check if fragment is properly attached.
        fm.executePendingTransactions();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        boolean landscape = (findViewById(R.id.detailFragment) != null);


        onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
        landscape = (findViewById(R.id.detailFragment) != null);

        //Create new instance of each fragment
        details = BookDetailsFragment.newInstance(books);
        Fragment list = BookListFragment.newInstance(myBooks, this);

        //When in landscape mode
        if(landscape)
        {
            loadFragment(R.id.listFragment, list, false);
            loadFragment(R.id.detailFragment, details, false);
        }
        else
        {
            if(books != null)
                loadFragment(R.id.listFragment, details, true);
            else
                loadFragment(R.id.listFragment, list, false);
        }
    }

}