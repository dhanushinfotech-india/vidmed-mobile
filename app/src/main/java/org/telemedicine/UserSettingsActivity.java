package org.telemedicine;


import android.os.Bundle;
import android.preference.PreferenceActivity;



public class UserSettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_settings);

    }
}
