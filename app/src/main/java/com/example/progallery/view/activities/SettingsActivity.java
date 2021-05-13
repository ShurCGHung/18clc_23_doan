package com.example.progallery.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.progallery.R;
import com.example.progallery.helpers.Constant;
import com.example.progallery.helpers.LocaleHelpers;

public class SettingsActivity extends AppCompatActivity {

    Toolbar settingsToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        settingsToolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    public static class SettingsFragment extends PreferenceFragmentCompat  {

        ListPreference languagePreference;
        SharedPreferences preferences;
        SharedPreferences.OnSharedPreferenceChangeListener listener;
        SharedPreferences pinPreferences;
        String savedPin;

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            pinPreferences = getActivity().getSharedPreferences(Constant.PIN, MODE_PRIVATE);
            savedPin = pinPreferences.getString(Constant.PIN, "");
            PreferenceCategory category = (PreferenceCategory) findPreference("vault_passcode_key");
            Preference setPIN = findPreference("set_pin_key");
            Preference changePIN = findPreference("change_pin_key");
            getPreferenceScreen().addPreference(category);

            if (savedPin.isEmpty()) {
                category.removePreference(changePIN);
            } else {
                category.removePreference(setPIN);
            }

            preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
            preferences.registerOnSharedPreferenceChangeListener(listener);

            try {
                languagePreference = findPreference(Constant.KEY_PREF_LANGUAGE);
                languagePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Constant.language, newValue.toString());
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

            setPIN.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), SetPasscode.class);
                    preference.setIntent(intent);
                    startActivityForResult(preference.getIntent(), Constant.REQUEST_SET_PIN);
                    return true;
                }
            });

            changePIN.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), ChangePasscode.class);
                    preference.setIntent(intent);
                    startActivityForResult(preference.getIntent(), Constant.REQUEST_CHANGE_PIN);
                    return true;
                }
            });
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
            if (requestCode == Constant.REQUEST_SET_PIN) {
                getActivity().recreate();
            } else if (requestCode == Constant.REQUEST_CHANGE_PIN) {
                getActivity().recreate();
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