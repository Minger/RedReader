/*******************************************************************************
 * This file is part of RedReader.
 *
 * RedReader is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedReader is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RedReader.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package org.quantumbadger.redreader.settings;

import android.os.Bundle;
import org.holoeverywhere.preference.ListPreference;
import org.holoeverywhere.preference.Preference;
import org.holoeverywhere.preference.PreferenceFragment;
import org.quantumbadger.redreader.R;

public final class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(final Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		final String panel = getArguments().getString("panel");
		final int resource;

		try {
			resource = R.xml.class.getDeclaredField("prefs_" + panel).getInt(null);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}

		addPreferencesFromResource(resource);

		final int[] listPrefsToUpdate = {
				R.string.pref_appearance_twopane_key,
				R.string.pref_behaviour_fling_post_left_key,
				R.string.pref_behaviour_fling_post_right_key,
				R.string.pref_appearance_theme_key,
				R.string.pref_cache_maxage_listing_key,
				R.string.pref_cache_maxage_thumb_key,
				R.string.pref_cache_maxage_image_key
		};

		for(int pref : listPrefsToUpdate) {

			final ListPreference listPreference = (ListPreference)findPreference(getString(pref));

			if(listPreference == null) continue;

			final int index = listPreference.findIndexOfValue(listPreference.getValue());
			if(index < 0) continue;

			listPreference.setSummary(listPreference.getEntries()[index]);

			// TODO may cause a (tiny) memory leak, or may be ineffective if weak refs are used
			listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					final int index = listPreference.findIndexOfValue((String)newValue);
					listPreference.setSummary(listPreference.getEntries()[index]);
					return true;
				}
			});
		}
	}
}
