package com.lab69.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {
    Button pause, next, previous,shuffle,repeat;
    TextView songNameText;
    static MediaPlayer mp;
    int position;
    SeekBar sb;
    ArrayList mySongs;
    Thread updateSeekBar;
    String sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        songNameText = findViewById(R.id.txtSongLabel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Now Playing");

        pause = findViewById(R.id.pause_id);

        previous = findViewById(R.id.previous_id);
        next = findViewById(R.id.next_id);

        shuffle = findViewById(R.id.shuffle_id);
        repeat = findViewById(R.id.repeat_id);



        sb = findViewById(R.id.seekBar);

        updateSeekBar = new Thread() {
            @Override
            public void run() {

                int totalduration = mp.getDuration();
                int currentposition = 0;


                while (currentposition < totalduration) {
                    try {
                        sleep(500);
                        currentposition = mp.getCurrentPosition();
                        sb.setProgress(currentposition);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        if (mp != null) {
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");

       sname=mySongs.get(position).toString();

        final String songname = i.getStringExtra("songname");
        songNameText.setText(songname);
        songNameText.setSelected(true);

        position = bundle.getInt("pos", 0);

        Uri uri = Uri.parse(mySongs.get(position).toString());

        mp = MediaPlayer.create(getApplicationContext(), uri);
        mp.start();
        sb.setMax(mp.getDuration());
        updateSeekBar.start();
        sb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
        sb.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mp.seekTo(seekBar.getProgress());
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.setMax(mp.getDuration());
                if (mp.isPlaying()) {
                    pause.setBackgroundResource(R.drawable.play_icon);
                    mp.pause();

                } else {
                    pause.setBackgroundResource(R.drawable.pause_icon);
                    mp.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                mp.release();

                position = ((position + 1) % mySongs.size());
                Uri uri = Uri.parse(mySongs.get(position).toString());

                mp = MediaPlayer.create(getApplicationContext(), uri);
                sname = mySongs.get(position).toString();
                songNameText.setText(sname);

                try {
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                mp.release();

                position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);

                Uri uri = Uri.parse(mySongs.get(position).toString());

                mp = MediaPlayer.create(getApplicationContext(), uri);
                sname=mySongs.get(position).toString();
                songNameText.setText(sname);
                try{
                    mp.start();
                }catch(Exception e){}
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


}
