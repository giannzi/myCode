package edu.byuh.cis.cs300.ns;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


/**
 * MainActivity as the entry point of application
 */
public class MainActivity extends AppCompatActivity {

    private GameView gv;
    private static Context appContext;  // Static application context
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = getApplicationContext();  // Capture context once
        gv = new GameView(this);
        setContentView(gv);


        if (Preferences.getMusicPref(this)){
            mediaPlayer = MediaPlayer.create(this, R.raw.music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer !=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }


    /**
     * It provides access to application context
     * @return
     */
    public static Context getAppContext() {
        return appContext;  // Any class (like NumberedSquare) can call this
    }


    /**
     * Calculates the ideal font size
     * @param lowerThreshold
     * @return
     */
    public static float findThePerfectFontSize(float lowerThreshold) {
        float fontSize = 1;
        Paint p = new Paint();
        p.setTextSize(fontSize);
        while (true) {
            float asc = -p.getFontMetrics().ascent;
            if (asc > lowerThreshold) {
                break;
            }
            fontSize++;
            p.setTextSize(fontSize);
        }
        return fontSize;
    }
}
