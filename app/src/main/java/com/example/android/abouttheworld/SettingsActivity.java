package com.example.android.abouttheworld;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

// Create SettingsActivity that displays the settings for the news
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Add the preferences from the settings_main xml file
            addPreferencesFromResource(R.xml.settings_main);

            // Find the section preference and bind the preference summary to it
            Preference section = findPreference(getString(R.string.section_key));
            bindPreferenceSummaryToValue(section);

            // Find the type preference and bind the preference summary to it
            Preference type = findPreference(getString(R.string.type_key));
            bindPreferenceSummaryToValue(type);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            // Change the value object to a String
            String newStringVal = value.toString();

            // If the preference is a list preference
            if (preference instanceof ListPreference) {
                // Cast it to ListPreference
                ListPreference listPreference = (ListPreference) preference;

                // Find the index of the preference selected
                int prefIndex = listPreference.findIndexOfValue(newStringVal);

                // If the index is more than 0, get the entries and find the selected one
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    CharSequence summaryLabel = labels[prefIndex];
                    newStringVal = summaryLabel.toString();
                }
            }

            // Set the preference summary
            preference.setSummary(newStringVal);
            return true;
        }


        // Create helper method to bind the summary to the value
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }

    }
}

