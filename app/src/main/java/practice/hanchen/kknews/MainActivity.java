package practice.hanchen.kknews;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	private static final String LOG_TAG = "MainActivity";
	private DrawerLayout mDrawer;
	private Toolbar toolbar;
	private FragmentChannels fragmentChannels = null;
	private FragmentPersonal fragmentPersonal = null;
	private FragmentSetting fragmentSettings = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		mDrawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		if(fragmentChannels == null) {
			fragmentChannels = new FragmentChannels();
		}
		setFragment(fragmentChannels);
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
}
