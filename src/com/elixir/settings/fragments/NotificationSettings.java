package com.elixir.settings.fragments;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import android.os.Bundle;
import com.android.settings.R;

import com.android.settings.SettingsPreferenceFragment;

public class NotificationSettings extends SettingsPreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.elixir_settings_notifications);
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.ELIXIR_SETTINGS;
    }
}
