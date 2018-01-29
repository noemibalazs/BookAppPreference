package com.example.android.bookapppreference;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class BookSharedPreference extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.main_settings);

            Preference orderBy = findPreference(getString(R.string.settings_order_key));
            bindPreferenceSummeryToValue(orderBy);

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference)preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex>=0){
                    CharSequence [] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            }
           else
               { preference.setSummary(stringValue);}
            return true;
        }

        private void bindPreferenceSummeryToValue(Preference preference){
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String prefString = sharedPreferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, prefString);

        }
    }


}
