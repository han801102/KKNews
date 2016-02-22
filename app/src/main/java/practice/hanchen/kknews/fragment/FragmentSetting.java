package practice.hanchen.kknews.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.preference.PreferenceFragment;

import java.util.Calendar;

import practice.hanchen.kknews.utils.DBHelper;
import practice.hanchen.kknews.R;

/**
 * Created by HanChen on 2016/2/2.
 */
public class FragmentSetting extends PreferenceFragment {
	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);

		addPreferencesFromResource(R.xml.fragment_preference);
		findPreference("auto_update").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				int updateFreq = Integer.parseInt(preference.getSharedPreferences().getString("update_freq", "0"));
				if (!(boolean) newValue) {
					updateFreq = 0;
				}
				resetAlarmManagerFreq(updateFreq);
				return true;
			}
		});
		findPreference("update_freq").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean autoUpdate = preference.getSharedPreferences().getBoolean("auto_update", false);
				if (autoUpdate) {
					resetAlarmManagerFreq(Integer.parseInt((String) newValue));
				}
				return true;
			}
		});
	}

	public void resetAlarmManagerFreq(int time) {
		Calendar cal = Calendar.getInstance();
		Intent intent = new Intent(getActivity(), DBHelper.RSSCrawlerService.class);
		PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		if (time > 0) {
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), time * 1000, pendingIntent);
		}
	}
}
