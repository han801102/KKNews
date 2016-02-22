package practice.hanchen.kknews.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Calendar;

import practice.hanchen.kknews.fragments.FragmentChannels;
import practice.hanchen.kknews.fragments.FragmentPersonal;
import practice.hanchen.kknews.fragments.FragmentSetting;
import practice.hanchen.kknews.R;
import practice.hanchen.kknews.services.RSSCrawlerService;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	private static final String LOG_TAG = "MainActivity";
	private DrawerLayout mDrawer;
	private FragmentChannels fragmentChannels = null;
	private FragmentPersonal fragmentPersonal = null;
	private FragmentSetting fragmentSettings = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		mDrawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		if (fragmentChannels == null) {
			fragmentChannels = new FragmentChannels();
		}
		setFragment(fragmentChannels);

		SharedPreferences settings = getSharedPreferences("practice.hanchen.kknews_preferences", 0);
		boolean autoUpdate = settings.getBoolean("auto_update", false);
		String updateFreq = settings.getString("update_freq", "30");
		if (autoUpdate) {
			startCrawlRSS(Integer.parseInt(updateFreq));
		} else {
			Intent intent = new Intent(this, RSSCrawlerService.class);
			startService(intent);
		}
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		Fragment fragment = null;
		switch (item.getItemId()) {
			case R.id.nav_news:
				if (fragmentChannels == null) {
					fragmentChannels = new FragmentChannels();
				}
				fragment = fragmentChannels;
				break;
			case R.id.nav_personal:
				if (fragmentPersonal == null) {
					fragmentPersonal = new FragmentPersonal();
				}
				fragment = fragmentPersonal;
				break;
			case R.id.nav_setting:
				if (fragmentSettings == null) {
					fragmentSettings = new FragmentSetting();
				}
				fragment = fragmentSettings;
				break;
			default:
				if (fragmentChannels == null) {
					fragmentChannels = new FragmentChannels();
				}
				fragment = fragmentChannels;
				break;
		}

		setFragment(fragment);

		item.setChecked(true);
		mDrawer.closeDrawers();

		return true;
	}

	public void setFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.main, fragment)
				.commit();
	}

	public void startCrawlRSS(int time) {
		Calendar cal = Calendar.getInstance();
		Intent intent = new Intent(this, RSSCrawlerService.class);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), time * 1000, pendingIntent);
		startService(intent);
	}
}
