package com.example.audioplayernr;

import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView songTitle;
    private SeekBar seekBar;
    private ListView songsList;
    private Button previousButton, playPauseButton, nextButton;
    private MediaPlayer mediaPlayer;
    private ArrayList<String> songs;
    private int currentSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songTitle = findViewById(R.id.songTitle);
        seekBar = findViewById(R.id.seekBar);
        songsList = findViewById(R.id.songsList);
        previousButton = findViewById(R.id.previousButton);
        playPauseButton = findViewById(R.id.playPauseButton);
        nextButton = findViewById(R.id.nextButton);

        // Инициализация песен
        songs = new ArrayList<>();
        songs.add("US Anthem");
        songs.add("Russian Anthem");
        songs.add("France Anthem");
        songs.add("Polish Anthem");
        songs.add("Finnish Anthem");

        // Массив с песнями
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songs);
        songsList.setAdapter(adapter);

        // mediaplayer
        mediaPlayer = MediaPlayer.create(this, R.raw.song1);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextSong();
            }
        });

        // Ползунок для управления временем
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Переключение песен
        songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentSongIndex = position;
                playSong(currentSongIndex);
            }
        });

        // Пред. песня
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPreviousSong();
            }
        });

        // Пауза/воспроизведение
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playPauseButton.setText("▶");
                } else {
                    mediaPlayer.start();
                    playPauseButton.setText("||");
                }
            }
        });

        // След. песня
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNextSong();
            }
        });

        // Обновление ползунка
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 1000);
    }

    private void playSong(int songIndex) {
        mediaPlayer.release();
        switch (songIndex) {
            case 0:
                mediaPlayer = MediaPlayer.create(this, R.raw.song1);
                break;
            case 1:
                mediaPlayer = MediaPlayer.create(this, R.raw.song2);
                break;
            case 2:
                mediaPlayer = MediaPlayer.create(this, R.raw.song3);
                break;
            case 3:
                mediaPlayer = MediaPlayer.create(this, R.raw.song4);
                break;
            case 4:
                mediaPlayer = MediaPlayer.create(this, R.raw.song5);
                break;
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playNextSong();
            }
        });
        mediaPlayer.start();
        playPauseButton.setText("||");
        songTitle.setText(songs.get(songIndex));
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void playPreviousSong() {
        currentSongIndex--;
        if (currentSongIndex < 0) {
            currentSongIndex = songs.size() - 1;
        }
        playSong(currentSongIndex);
    }

    private void playNextSong() {
        currentSongIndex++;
        if (currentSongIndex >= songs.size()) {
            currentSongIndex = 0;
        }
        playSong(currentSongIndex);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}