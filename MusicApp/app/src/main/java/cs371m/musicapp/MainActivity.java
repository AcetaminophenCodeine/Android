package cs371m.musicapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.os.Handler;
import android.widget.ImageButton;
import android.content.Intent;
public class MainActivity extends AppCompatActivity {

    private String[] songNames;
    private int songResources[];
    private SeekBar seekbar;
    public static MediaPlayer mp;
    private Handler timeHandler;
    private int songCount;
    private ListView songListView;
    private ImageButton forwardButton;
    private ImageButton backwardButton;
    private ImageButton startPauseButton;
    private TextView nextup;
    private TextView nowplaying;

    protected Runnable songProgress = new Runnable()
    {
        public void run()
        {
            setTimeText();
            seekbar.setProgress((mp.getCurrentPosition() * 100)/mp.getDuration());
            timeHandler.postDelayed(this, 100);
        }
    };


    public void songCountIncrement()
    {
            songCount++;
    }

    //waits for settings to be selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            Bundle data = new Bundle();
            Intent intent = new Intent(getApplicationContext(), Settings.class);
            intent.putExtras(data);
            startActivityForResult(intent, 1);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle data = getIntent().getExtras();
                setSongNamesText();
            }
        }
    }

    // inflates Menu view for settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    // returns string array of song names
    protected String[] getSongNames() {
        songNames = getResources().getStringArray(R.array.song_names_array);
        return songNames;
    }

    protected void setSongNamesText()
    {
        nowplaying.setText("Now playing: " + songNames[songCount]);

        if(songCount < (songResources.length-1)) {
            nextup.setText("Next up: " + songNames[songCount + 1]);
        }
        else
        {
            nextup.setText("Next up: " + songNames[0]);
        }
    }

    protected String setTimeFormat(int num)
    {
        if(num < 10)
        {
            return "0" + num;
        }
        else
        {
            return "" + num ;
        }
    }

    protected void setTimeText()
    {
        int startMinutes = mp.getCurrentPosition() / 60000;
        int startSeconds = (mp.getCurrentPosition() - startMinutes * 60000)/1000;

        int finishMinutes = (mp.getDuration() - mp.getCurrentPosition()) / 60000;
        int finishSeconds = ((mp.getDuration() - mp.getCurrentPosition()) - (finishMinutes * 60000)) / 1000;

        TextView time1 = (TextView) findViewById(R.id.start_time_text);
        TextView time2 = (TextView) findViewById(R.id.finish_time_text);

        time1.setText(setTimeFormat(startMinutes) + ":" + setTimeFormat(startSeconds));
        time2.setText(setTimeFormat(finishMinutes) + ":" + setTimeFormat(finishSeconds));

    }

    protected void loopThroughSongs()
    {

        if (songCount < songResources.length)
        {
            mp = MediaPlayer.create(this, songResources[songCount]);

            mp.setOnCompletionListener(new OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    mp.release();
                    songCountIncrement();
                    loopThroughSongs();
                }
            });

        }
        else
        {
            songCount = 0;
            mp = MediaPlayer.create(this, songResources[songCount]);

            mp.setOnCompletionListener(new OnCompletionListener()
            {
                @Override
                public void onCompletion(MediaPlayer mp)
                {
                    mp.release();
                    songCountIncrement();
                    loopThroughSongs();
                }
            });
        }

        mp.start();
        setSongNamesText();
        seekbar.setProgress((mp.getCurrentPosition() * 100)/mp.getDuration());
        timeHandler.postDelayed(songProgress, 100);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songCount = 0;
        timeHandler = new Handler();
        songNames = getResources().getStringArray(R.array.song_names_array);
        songResources = GetSongResources.getSongResources();
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        //seekbar.setClickable(false);
        songListView = (ListView) findViewById(R.id.song_names_listview);
        forwardButton = (ImageButton) findViewById(R.id.skip_forward_button);
        backwardButton = (ImageButton) findViewById(R.id.skip_backward_button);
        startPauseButton = (ImageButton) findViewById(R.id.play_button);
        nextup = (TextView) findViewById(R.id.next_up_text);
        nowplaying = (TextView) findViewById(R.id.now_playing_text);

        loopThroughSongs();

        songListView.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                mp.release();
                songCount = position;
                loopThroughSongs();
            }
        } );

        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                seekBar.setProgress(progress);
                if (fromUser) {
                    int currentSongPlace = progress * mp.getDuration() / 100;
                    mp.seekTo(currentSongPlace);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar){}

            public void onStopTrackingTouch(SeekBar seekBar){}

        });

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(songCount < (songResources.length - 1) )
                {
                    songCount++;
                }
                else
                {
                    songCount = 0;
                }

                mp.release();
                loopThroughSongs();
            }
        });

        backwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(songCount == 0 )
                {
                    songCount = songResources.length - 1;
                }
                else
                {
                    songCount--;
                }

                mp.release();
                loopThroughSongs();
            }
        });

        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying())
                {
                    mp.pause();
                    startPauseButton.setImageResource(R.drawable.play);
                }
                else
                {
                    mp.start();
                    startPauseButton.setImageResource(R.drawable.pause);
                }
            }
        });

    }



}
