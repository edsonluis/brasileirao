package br.edsonluis.app.brasileirao.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.fragment.JogosFragment;
import br.edsonluis.app.brasileirao.fragment.NavigationDrawerFragment;
import br.edsonluis.app.brasileirao.fragment.SobreFragment;
import br.edsonluis.app.brasileirao.fragment.TabelaFragment;
import br.edsonluis.app.brasileirao.model.Rodada;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

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

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			return true;
		}
		return true;
	}

}
