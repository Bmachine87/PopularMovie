package com.example.android.popularmovie;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general_preference);
        bindPreferenceSummaryToValue(findPreference("sort_by"));
    }

    private void bindPreferenceSummaryToValue(Preference pref) {
        pref.setOnPreferenceChangeListener(this);

        onPreferenceChange(pref, PreferenceManager
                .getDefaultSharedPreferences(pref.getContext())
                .getString(pref.getKey(), "");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String value = newValue.toString();

        if(preference instanceof ListPreference) {
            ListPreference listPref = (ListPreference) preference;

            int prefIndex = listPref.findIndexOfValue(value);
            if(prefIndex >= 0) {
                preference.setSummary(listPref.getEntries()[prefIndex]);
            }
        } else {
            preference.setSummary(value);
        }
        return true;
    }

}
