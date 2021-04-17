package temple.edu.bookshelf;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ControlFragment extends Fragment
{
    private boolean playing;
    private Book currentBook;
    private MainActivity mainActivity;
    private Button playPause, search, stop;
    private TextView nowPlaying;

    public ControlFragment()
    {
        //Required empty default constructor
    }

    public static ControlFragment newInstance()
    {
        ControlFragment controlFragment = new ControlFragment();
        controlFragment.playing = false;
        return controlFragment;
    }

    public static ControlFragment newInstance(MainActivity parent) {
        ControlFragment controlFragment = new ControlFragment();
        controlFragment.playing = false;
        controlFragment.mainActivity = parent;
        return controlFragment;
    }

    public void setPlayBook(Book book)
    {
        currentBook = book;

        if(currentBook != null)
            playing = true;
        else
            playing = false;

        buttonTextSwitch();
        updateHeader();
    }

    private void updateHeader()
    {
        String header = getString(R.string.now_playing_header);

        if(currentBook != null)
            header += currentBook.getTitle();

        if(nowPlaying != null)
            nowPlaying.setText(header);
    }

    private void playPause()
    {
        playing = !playing;
        buttonTextSwitch();

        mainActivity.pause();
    }

    private void buttonTextSwitch()
    {
        if(playPause == null)
            return;

        if(playing)
            playPause.setText(R.string.pause);
        else
            playPause.setText(R.string.play);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_control, null);

        nowPlaying = v.findViewById(R.id.nowPlaying);
        updateHeader();

        playPause = v.findViewById(R.id.playPause);
        buttonTextSwitch();
        playPause.setOnClickListener(v13 -> playPause());

        stop = v.findViewById(R.id.stop);
        stop.setOnClickListener(v12 -> mainActivity.stop());

        search = v.findViewById(R.id.search);
        search.setOnClickListener(v1 -> mainActivity.searchForBook());

        return v;
    }
}