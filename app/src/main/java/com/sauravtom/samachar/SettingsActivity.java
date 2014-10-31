package com.sauravtom.samachar;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by root on 29/10/14.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preference_xml);
    }
}
