/*
 * Copyright (C) 2017-2019 The Dirty Unicorns Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nusantara.wings.fragments.system;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import java.util.ArrayList;
import java.util.List;

import com.nusantara.support.preferences.SystemSettingMasterSwitchPreference;

@SearchIndexable(forTarget = SearchIndexable.ALL & ~SearchIndexable.ARC)
public class Miscellaneous extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {


    private static final String GAMING_MODE_ENABLED = "gaming_mode_enabled";

    private SystemSettingMasterSwitchPreference mGamingMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.nad_miscellaneous);

        final PreferenceScreen prefScreen = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        mGamingMode = (SystemSettingMasterSwitchPreference) findPreference(GAMING_MODE_ENABLED);
        mGamingMode.setChecked((Settings.System.getInt(resolver,
                Settings.System.GAMING_MODE_ENABLED, 0) == 1));
        mGamingMode.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mGamingMode) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver,
                    Settings.System.GAMING_MODE_ENABLED, value ? 1 : 0);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.NUSANTARA_PRJ;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();

                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.nad_miscellaneous;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);
                    final Resources res = context.getResources();
                    return keys;
        }
    };
}
