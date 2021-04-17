package temple.edu.bookshelf;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;
import android.os.IBinder;
import android.widget.SeekBar;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Book> bookArrayList;
    private BookAdapter bookAdapter;
    private Book currentBook, playBook;
    private BookDetailsFragment bookDetailsFragment;
    private ControlFragment controlFragment;
    private AudiobookService.MediaControlBinder abService;

    private String URL;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private boolean twoPanes;

    private SeekBar seekbar;
    private int progress;
    private boolean playing;

    private TimerTask timertask;
    private Timer timer;

    public ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Checks if two panes
        twoPanes = (findViewById(R.id.details_frame) != null);

        Resources res = getResources();
        URL = res.getString(R.string.base_url);

        serviceConnection = new ServiceConnection()
        {
            public void onServiceConnected(ComponentName className, IBinder binder) { abService = (AudiobookService.MediaControlBinder)binder; }

            public void onServiceDisconnected(ComponentName className) {
                abService = null;
            }
        };

        //Bind service
        Intent intent = null;
        intent = new Intent(this, AudiobookService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        //Set volley request
        requestQueue = Volley.newRequestQueue(this);

        bookArrayList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookArrayList);
        currentBook = null;

        //Store search terms
        sharedPreferences = this.getSharedPreferences("edu.temple.bookshelf", Context.MODE_PRIVATE);
        String searchTerm = sharedPreferences.getString("searchTerm", "");

        updateBook(searchTerm);

        //Load default fragment
        controlFragment = ControlFragment.newInstance(this);
        loadFragment(R.id.control_frame, controlFragment, false);
        loadFragment(R.id.list_frame, BookListFragment.newInstance(bookAdapter, this), false);

        //Load details fragment whenever two panes comes back true
        bookDetailsFragment = BookDetailsFragment.newInstance(currentBook, this);

        if (twoPanes)
            loadFragment(R.id.details_frame, bookDetailsFragment, false);

        //Create timer
        createTimerTask();
        timer = new Timer("Timer");

        //Stores data to resume book if restarted
        String bookTitle = sharedPreferences.getString("bookTitle", null);

        //Create new book if no book exists and no audio book is playing
        if(bookTitle != null)
        {
            int bookID = sharedPreferences.getInt("bookID", 0);
            progress = sharedPreferences.getInt("progress", 0);
            int bookDuration = sharedPreferences.getInt("bookDuration", 0);
            String bookAuthor = sharedPreferences.getString("bookAuthor", "");
            String bookURL = sharedPreferences.getString("bookURL", "");
            currentBook = new Book(bookID, bookTitle, bookAuthor, bookURL, bookDuration);
            playBook = currentBook;
            controlFragment.setPlayBook(playBook);

            TimerTask task = new TimerTask()
            {
                public void run()
                {
                    playing = true;
                    setupSeekBar();
                    abService.play(playBook.getBookId(), progress);
                }
            };

            long initialDelay = 5000;
            timer.schedule(task, initialDelay);
        }
    }

    //Create timer if none found
    private void createTimerTask()
    {
        //Redefine timer
        timertask = new TimerTask()
        {
            public void run()
            {
                seekTimeUpdate();
            }
        };
    }

    //Function to load frame and add to back stack
    private void loadFragment(int paneId, Fragment fragment, boolean placeOnStack)
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction()
                .replace(paneId, fragment);
        if (placeOnStack)
            ft.addToBackStack(null);
        ft.commit();
        fm.executePendingTransactions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                String searchTerm = data.getStringExtra("response");

                //Store last typed term
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("searchTerm", searchTerm);
                editor.commit();

                updateBook(searchTerm);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);

        controlFragment = ControlFragment.newInstance(this);
        loadFragment(R.id.control_frame, controlFragment, false);
        controlFragment.setPlayBook(playBook);
        setupSeekBar();

        twoPanes = (findViewById(R.id.details_frame) != null);

        bookDetailsFragment = BookDetailsFragment.newInstance(currentBook, this);
        Fragment list = BookListFragment.newInstance(bookAdapter, this);

        //Switch to Landscape
        if (twoPanes) {
            loadFragment(R.id.list_frame, list, false);
            loadFragment(R.id.details_frame, bookDetailsFragment, false);
        }
        else {
            if (currentBook != null)
                loadFragment(R.id.list_frame, bookDetailsFragment, true);
            else
                loadFragment(R.id.list_frame, list, false);
        }
    }

    public void selectBook(int position)
    {
        currentBook = bookAdapter.get(position);

        //Reinitialize two panes
        twoPanes = (findViewById(R.id.details_frame) != null);

        //If two panes not true set book list fragment instead
        if (!twoPanes) {
            BookDetailsFragment newFrag = BookDetailsFragment.newInstance(currentBook, this);
            loadFragment(R.id.list_frame, newFrag, true);
        }
        else {
            bookDetailsFragment.displayBook(currentBook);
        }
    }

    public void searchForBook()
    {
        Intent intent = new Intent(this, BookSearch.class);
        startActivityForResult(intent, 1);
    }

    private void updateBook(String searchTerm)
    {
        String bookURL = URL+searchTerm;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, bookURL, null, response -> {
            try
            {
                ArrayList<Book> newList = new ArrayList<>();

                for (int i = 0; i < response.length(); i++)
                {
                    JSONObject currObj = response.getJSONObject(i);
                    int id = currObj.getInt("id");
                    int duration = currObj.getInt("duration");
                    String title = currObj.getString("title");
                    String author = currObj.getString("author");
                    String coverURL = currObj.getString("cover_url");
                    newList.add(new Book(id, title, author, coverURL, duration));
                }

                bookAdapter = new BookAdapter(MainActivity.this, newList);
                Fragment list = BookListFragment.newInstance(bookAdapter, MainActivity.this);
                loadFragment(R.id.list_frame, list, false);

            }
            catch (Exception e) { e.printStackTrace(); }
        }, error -> error.printStackTrace());

        requestQueue.add(jsonArrayRequest);

    }

    //Pauses audio book
    public void pause()
    {
        abService.pause();

        if(playBook != null)
            playing = !playing;

        if(!playing) {
            timer.cancel();
            timer.purge();
        }
    }

    //Play audio book
    public void playThisBook()
    {
        if(currentBook == null)
            return;

        if(abService == null)
            return;

        abService.stop();

        progress = 0;
        playBook = currentBook;
        controlFragment.setPlayBook(playBook);
        setupSeekBar();
        playing = true;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("bookID", playBook.getBookId());
        editor.putString("bookTitle", playBook.getTitle());
        editor.putString("bookAuthor", playBook.getAuthor());
        editor.putString("bookURL", playBook.getCoverURL());
        editor.putInt("bookDuration", playBook.getDuration());
        editor.putInt("progress", 0);
        editor.commit();

        createTimerTask();
        long delay = 2000;
        timer.schedule(timertask, delay);

        abService.play(playBook.getBookId());
    }

    public void seekTimeUpdate()
    {
        //Update progress of seekbar
        progress += 2;
        seekbar.setProgress(progress);

        //Save progress of seekbar
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("progress", progress);
        editor.commit();

        createTimerTask();
        long delay = 2000;
        timer.schedule(timertask, delay);
    }


    public void setupSeekBar()
    {
        seekbar = findViewById(R.id.seekBar);
        seekbar.setMax(playBook.getDuration());
        seekbar.setProgress(progress);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int setProgress, boolean user)
            {
                if (user) {
                    abService.seekTo(setProgress);
                    progress = setProgress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    //Stop audio book
    public void stop()
    {
        abService.stop();
        playing = false;

        playBook = null;
        controlFragment.setPlayBook(null);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("bookTitle", null);
        editor.apply();

        timer.cancel();
        timer.purge();
    }
}