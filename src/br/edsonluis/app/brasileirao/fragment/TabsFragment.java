package br.edsonluis.app.brasileirao.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.util.Constantes;
import br.edsonluis.app.brasileirao.util.Utils;

public class TabsFragment extends Fragment implements OnTabChangeListener {

	public static final String TAG = TabsFragment.class.getSimpleName();

	private View mRoot;
	private TabHost mTabHost;
	private Integer mCurrentTab;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRoot = inflater.inflate(R.layout.tabs_fragment, null);
		mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
		setupTabs();
		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mCurrentTab = Utils.getSharedPreferences().getInt(
				Constantes.RODADA_ATUAL, 0) - 1;

		mTabHost.setOnTabChangedListener(this);
		mTabHost.setCurrentTab(mCurrentTab);
	}

	private void setupTabs() {
		mTabHost.setup();

		for (int i = 0; i < Constantes.QTD_RODADAS; i++) {
			mTabHost.addTab(newTab(i, "RODADA " + (i + 1)));
		}
	}

	private TabSpec newTab(Integer idTab, String tag) {
		View indicator = LayoutInflater.from(getActivity()).inflate(
				R.layout.tab,
				(ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
		((TextView) indicator.findViewById(R.id.text)).setText(tag);

		TabSpec tabSpec = mTabHost.newTabSpec(idTab.toString());
		tabSpec.setIndicator(indicator);
		tabSpec.setContent(new TabContentFactory() {
			public View createTabContent(String tag) {
				return new TextView(getActivity());
			}
		});
		return tabSpec;
	}

	@Override
	public void onTabChanged(String tabId) {
		Log.d(TAG, "onTabChanged(): tabId=" + tabId);
		mCurrentTab = Integer.valueOf(tabId);
		updateTab(tabId, android.R.id.tabcontent);
	}

	private void updateTab(String tabId, int placeholder) {
		FragmentManager fm = getFragmentManager();
		Fragment fragment = JogosFragment
				.newInstance(Integer.valueOf(tabId) + 1);
		if (fm.findFragmentByTag(tabId) == null) {
			fm.beginTransaction().replace(placeholder, fragment, tabId)
					.commit();
		}
	}

}
