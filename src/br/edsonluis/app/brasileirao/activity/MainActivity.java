package br.edsonluis.app.brasileirao.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import br.edsonluis.app.brasileirao.BrasileiraoApplication;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.fragment.JogosFragment;
import br.edsonluis.app.brasileirao.fragment.NavigationDrawerFragment;
import br.edsonluis.app.brasileirao.fragment.SobreFragment;
import br.edsonluis.app.brasileirao.fragment.TabelaFragment;
import br.edsonluis.app.brasileirao.model.Rodada;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private CharSequence mTitle;
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		adView = (AdView) this.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice(getString(R.string.device_test_id)).build();
		adView.loadAd(adRequest);

		((BrasileiraoApplication) getApplication()).getTracker(BrasileiraoApplication.TrackerName.APP_TRACKER);
		
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					Rodada.obterRodadaAtual(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	@Override
	public void onPause() {
		adView.pause();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		adView.resume();
	}

	@Override
	public void onDestroy() {
		adView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		switch (position) {
		case 0:
			transaction.replace(R.id.container, new TabelaFragment());
			break;

		case 1:
			transaction.replace(R.id.container, new JogosFragment());
			transaction.addToBackStack(null);
			break;

		case 2:
			transaction.replace(R.id.container, new SobreFragment());
			transaction.addToBackStack(null);
			break;
		}
		transaction.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			return true;
		}
		return true;
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

}
