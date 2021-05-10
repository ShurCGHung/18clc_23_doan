package com.example.progallery.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.progallery.R;
import com.example.progallery.helpers.LocaleHelpers;

public class SettingsActivity extends AppCompatActivity {


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
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        public static final String KEY_PREF_LANGUAGE = "pref_key_language";
        private static final String language = "language";
        ListPreference languagePreference;
        SharedPreferences preferences;
        SharedPreferences.OnSharedPreferenceChangeListener listener;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            preferences.registerOnSharedPreferenceChangeListener(listener);

            try {
                languagePreference = findPreference(KEY_PREF_LANGUAGE);
                languagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(language, newValue.toString());
                    editor.commit();
                    editor.apply();
                    LocaleHelpers.setLocale(getContext(), newValue.toString());
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    return true;
                });
            } catch (NullPointerException e) {
                Toast.makeText(getActivity(), "Set Language Error", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
        }
    }
}