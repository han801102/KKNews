package practice.hanchen.kknews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by HanChen on 2016/2/2.
 */
public class FragmentSetting extends PreferenceFragment {
	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);

		addPreferencesFromResource(R.xml.fragment_preference);
	}
}
