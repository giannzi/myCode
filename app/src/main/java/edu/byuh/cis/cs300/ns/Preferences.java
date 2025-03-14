package edu.byuh.cis.cs300.ns;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

/**
 * PrefsActivity - Allows users to modify game settings, including:
 * - Background music toggle
 * - Square movement speed
 * - Theme selection
 */
public class Preferences extends AppCompatActivity {

    /**
     * Handles the back button action in the settings screen.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle the back button on the toolbar
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Creates the preferences UI and sets up the action bar.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Retrieves the user's preference for background music.
     * @param c the application context
     * @return true if music is enabled, false otherwise
     */
    public static boolean getMusicPref(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("MUSIC_PREF", true);
    }

    /**
     * Retrieves the user's preference for square movement speed.
     * @param c the application context
     * @return speed as an integer (e.g., 30 for fast, 10 for medium, 6 for slow)
     */
    public static int getSpeedPref(Context c) {
        String speed = PreferenceManager.getDefaultSharedPreferences(c).getString("SPEED_PREF", "10");
        return Integer.parseInt(speed);
    }

    public static int getThemePref(Context c) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("BKGD_PREF", String.valueOf(R.drawable.temple)));
    }


    /**
     * SettingsFragment - Defines and initializes user preferences.
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle b, String s) {
            Context context = getPreferenceManager().getContext();
            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

            // Background Music Toggle
            SwitchPreference musicPref = new SwitchPreference(context);
            musicPref.setTitle("Play Background Music");
            musicPref.setSummaryOn("Background music will play");
            musicPref.setSummaryOff("Background music will not play");
            musicPref.setDefaultValue(true);
            musicPref.setKey("MUSIC_PREF");
            screen.addPreference(musicPref);

            // Light or Dark Theme Toggle
            SwitchPreference lightPref = new SwitchPreference(context);
            lightPref.setTitle("Light or Dark Theme");
            lightPref.setSummaryOn("Light Theme");
            lightPref.setSummaryOff("Dark Theme");
            lightPref.setDefaultValue(true);
            lightPref.setKey("LIGHT_PREF");
            screen.addPreference(lightPref);

            // Square Movement Speed
            ListPreference speedPref = new ListPreference(context);
            String[] speedEntries = {"Fast", "Medium", "Slow"};
            String[] speedValues = {"30", "10", "1"};
            speedPref.setEntries(speedEntries);
            speedPref.setEntryValues(speedValues);
            speedPref.setTitle("Square Speed");
            speedPref.setSummary("Adjust the movement speed of the squares.");
            speedPref.setDefaultValue("10");
            speedPref.setKey("SPEED_PREF");
            screen.addPreference(speedPref);

            // Background Theme Selection
            ListPreference bkgdArt = new ListPreference(context);
            String[] artEntries = {"Temple Scene","Snowy Scene", "Desert Scene"};
            String[] artEntryValues = {
                    String.valueOf(R.drawable.temple),
                    String.valueOf(R.drawable.snowy),
                    String.valueOf(R.drawable.desert)
            };
            bkgdArt.setEntries(artEntries);
            bkgdArt.setEntryValues(artEntryValues);
            bkgdArt.setTitle("Select Background Theme");
            bkgdArt.setSummary("Choose a visual theme for the game.");
            bkgdArt.setDefaultValue(String.valueOf(R.drawable.temple));
            bkgdArt.setKey("BKGD_PREF");
            screen.addPreference(bkgdArt);

            setPreferenceScreen(screen);
        }
    }


}
