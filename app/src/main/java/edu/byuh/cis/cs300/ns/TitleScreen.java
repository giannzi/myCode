package edu.byuh.cis.cs300.ns;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * TitleScreen (Splash Screen) - The first screen the user sees when launching the game.
 * - Shows a title screen with an About button and an Options button.
 * - The About button displays game credits in an AlertDialog.
 * - The Options button opens a Preferences screen.
 * - Tapping anywhere else starts the game.
 */
public class TitleScreen extends AppCompatActivity {

    private ImageView iv; // ImageView to display the splash screen

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        // Set up the splash screen image
        iv = new ImageView(this);
        iv.setImageResource(R.drawable.splash);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(iv);
    }

    /**
     * Handles user taps on the splash screen.
     * - Tapping the top-left area opens the About dialog.
     * - Tapping the top-right area opens the Options (Preferences) screen.
     * - Tapping anywhere else starts the game.
     *
     * @param m MotionEvent containing touch data
     * @return true to indicate the event was handled
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            float h = iv.getHeight();
            float w = iv.getWidth();
            float x = m.getX();
            float y = m.getY();

            if (y < h * 0.25) { // User tapped the top section (buttons)
                if (x < w * 0.5) {
                    // Open About dialog (credits/instructions)
                    showAboutDialog();
                } else {
                    // Open Options/Preferences screen
                    Intent optionsIntent = new Intent(this, Preferences.class);
                    startActivity(optionsIntent);
                }
            } else {
                // Start the game (MainActivity)
                Intent gameIntent = new Intent(this, MainActivity.class);
                startActivity(gameIntent);
                finish();
            }
        }
        return true;
    }

    /**
     * Displays an AlertDialog with game credits and instructions.
     */
    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Frozen Squares")
                .setMessage("This game is created by Zia, a CS 300 student")
                .setNeutralButton("OK", null)
                .show();
    }
}
