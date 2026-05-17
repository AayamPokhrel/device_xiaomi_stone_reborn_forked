/*
 * Copyright (C) 2026 KamiKaonashi
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

package org.lineageos.settings.speaker;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.SystemProperties;

import androidx.preference.Preference;
import com.android.settingslib.widget.SettingsBasePreferenceFragment;
import androidx.preference.TwoStatePreference;

import org.lineageos.settings.R;

public class StereoSpeakerFragment extends SettingsBasePreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String PREF_STEREO_SPEAKER = "stereo_speaker_pref";
    private static final String PROP_STEREO_SPEAKER = "persist.sys.stereo_speaker";

    private TwoStatePreference mStereoSpeakerPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.stereo_speaker_settings, rootKey);

        mStereoSpeakerPref = (TwoStatePreference) findPreference(PREF_STEREO_SPEAKER);
        mStereoSpeakerPref.setChecked(
                SystemProperties.getBoolean(PROP_STEREO_SPEAKER, false));
        mStereoSpeakerPref.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mStereoSpeakerPref) {
            boolean enabled = (Boolean) newValue;
            if (enabled) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.stereo_speaker_warning_title)
                        .setMessage(R.string.stereo_speaker_warning_message)
                        .setPositiveButton(R.string.stereo_speaker_warning_enable, (dialog, which) -> {
                            applyAndPromptRestart(true);
                        })
                        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                            mStereoSpeakerPref.setChecked(false);
                        })
                        .setCancelable(false)
                        .show();
                return false;
            } else {
                applyAndPromptRestart(false);
                return true;
            }
        }
        return false;
    }

    private void applyAndPromptRestart(boolean enabled) {
        SystemProperties.set(PROP_STEREO_SPEAKER, enabled ? "1" : "0");
        mStereoSpeakerPref.setChecked(enabled);

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.stereo_speaker_restart_title)
                .setMessage(R.string.stereo_speaker_restart_message)
                .setPositiveButton(R.string.stereo_speaker_restart_now, (dialog, which) -> {
                    android.os.PowerManager pm = (android.os.PowerManager)
                            getContext().getSystemService(android.content.Context.POWER_SERVICE);
                    pm.reboot(null);
                })
                .setNegativeButton(R.string.stereo_speaker_restart_later, null)
                .setCancelable(false)
                .show();
    }
}
