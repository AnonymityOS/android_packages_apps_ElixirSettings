package com.elixir.settings.fragments;

import com.android.internal.logging.MetricsProto.MetricsEvent;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemProperties;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;
import com.android.settings.R;
import android.support.v4.app.Fragment;

import java.util.Locale;
import android.text.TextUtils;
import android.view.View;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import android.util.Log;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

import java.util.List;
import java.util.ArrayList;

public class StatusBarSettings extends SettingsPreferenceFragment implements
        OnPreferenceChangeListener {

        private static final String KEY_ELIXIR_LOGO_COLOR = "status_bar_elixir_logo_color";
        private static final String KEY_ELIXIR_LOGO_STYLE = "status_bar_elixir_logo_style";

        private ColorPickerPreference mElixirLogoColor;
        private ListPreference mElixirLogoStyle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.elixir_settings_statusbar);

            PreferenceScreen prefSet = getPreferenceScreen();
            final ContentResolver resolver = getActivity().getContentResolver();
            Context context = getActivity();

            mElixirLogoStyle = (ListPreference) findPreference(KEY_ELIXIR_LOGO_STYLE);
            int elixirLogoStyle = Settings.System.getIntForUser(resolver,
                    Settings.System.STATUS_BAR_ELIXIR_LOGO_STYLE, 0,
                    UserHandle.USER_CURRENT);
            mElixirLogoStyle.setValue(String.valueOf(elixirLogoStyle));
            mElixirLogoStyle.setSummary(mElixirLogoStyle.getEntry());
            mElixirLogoStyle.setOnPreferenceChangeListener(this);

            // Aicp logo color
            mElixirLogoColor =
                (ColorPickerPreference) prefSet.findPreference(KEY_ELIXIR_LOGO_COLOR);
            mElixirLogoColor.setOnPreferenceChangeListener(this);
            int intColor = Settings.System.getInt(resolver,
                    Settings.System.STATUS_BAR_ELIXIR_LOGO_COLOR, 0xffffffff);
            String hexColor = String.format("#%08x", (0xffffffff & intColor));
            mElixirLogoColor.setSummary(hexColor);
            mElixirLogoColor.setNewPreviewColor(intColor);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
            if (preference == mElixirLogoColor) {
                String hex = ColorPickerPreference.convertToARGB(
                        Integer.valueOf(String.valueOf(newValue)));
                preference.setSummary(hex);
                int intHex = ColorPickerPreference.convertToColorInt(hex);
                Settings.System.putInt(resolver,
                        Settings.System.STATUS_BAR_ELIXIR_LOGO_COLOR, intHex);
                return true;
            } else if (preference == mElixirLogoStyle) {
                int elixirLogoStyle = Integer.valueOf((String) newValue);
                int index = mElixirLogoStyle.findIndexOfValue((String) newValue);
                Settings.System.putIntForUser(
                        resolver, Settings.System.STATUS_BAR_ELIXIR_LOGO_STYLE, elixirLogoStyle,
                        UserHandle.USER_CURRENT);
                mElixirLogoStyle.setSummary(
                        mElixirLogoStyle.getEntries()[index]);
                return true;
            }
        return false;
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsEvent.ELIXIR_SETTINGS;
    }

}
