/*
Copyright (c) 2012, Phillip Thelen
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package net.pherth.omnomagon;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

import android.preference.PreferenceManager;
import android.util.Log;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;

public class MainPreference extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {

	private ListPreference cityPref;
	private ListPreference mensaPref;
	private ListPreference pricePref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.mainpreferences);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

		getListView().setCacheColorHint(0x00000000);
		getListView().setDivider(null);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar));
		actionBar.setDisplayShowHomeEnabled(false);

		CharSequence entry;
		cityPref = (ListPreference) findPreference("cityPreference");
		entry = cityPref.getEntry();
		cityPref.setSummary(entry);


		mensaPref = (ListPreference) findPreference("mensaPreference");
		if (cityPref.getValue() != null) {
			mensaPref.setEntries(getResources().getIdentifier(cityPref.getValue(), "array", "net.pherth.omnomagon"));
			mensaPref.setEntryValues(getResources().getIdentifier(cityPref.getValue()+"Values", "array", "net.pherth.omnomagon"));
		} else {
			mensaPref.setEntries(R.array.beList);
			mensaPref.setEntryValues(R.array.beListValues);
		}
		entry = mensaPref.getEntry();
		mensaPref.setSummary(entry);


		ListPreference pricePref = (ListPreference) findPreference("priceCategory");
		pricePref.setOnPreferenceChangeListener(setListListener());
		entry = pricePref.getEntry();
		pricePref.setSummary(entry);

		ListPreference testPref = (ListPreference) findPreference("priceCategory");
	}


	private OnPreferenceChangeListener setListListener(){
		return new OnPreferenceChangeListener() {
			@Override()
			public boolean onPreferenceChange(Preference arg0, Object arg1) {
				int index = ((ListPreference) arg0).findIndexOfValue(arg1.toString());
				CharSequence summary = ((ListPreference) arg0).getEntries()[index];
				arg0.setSummary(summary);
				return true;
			}
		};
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.d("Preference", "Herp the derp");
		if (key.equals("cityPreference")) {
			if (cityPref.getValue() == "ulm") {
				mensaPref.setEnabled(false);
			} else {
				mensaPref.setEnabled(true);
				if (cityPref.getValue() != null) {
					mensaPref.setEntries(getResources().getIdentifier(cityPref.getValue(), "array", "net.pherth.omnomagon"));
					mensaPref.setEntryValues(getResources().getIdentifier(cityPref.getValue()+"Values", "array", "net.pherth.omnomagon"));
				} else {
					mensaPref.setEntries(R.array.beList);
					mensaPref.setEntryValues(R.array.beListValues);
				}
				mensaPref.setValueIndex(0);
				CharSequence entry = mensaPref.getEntry();
				mensaPref.setSummary(entry);
			}
		} else if (key.equals("mensaPreference")) {
			mensaPref.setSummary(mensaPref.getEntry());

		} else if (key.equals("pricePreference")) {
			//pricePref.setSummary(pricePref.getEntry());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}



	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

	}
}
