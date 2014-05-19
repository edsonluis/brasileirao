package br.edsonluis.app.brasileirao.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.adapter.TabsAdapter;
import br.edsonluis.app.brasileirao.util.Constantes;
import br.edsonluis.app.brasileirao.util.Utils;

public class TabsFragment extends Fragment implements
		TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

	public static final String TAG = TabsFragment.class.getSimpleName();

	private View mRoot;
	private TabHost mTabHost;
	private Integer mCurrentTab;
	private ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private HorizontalScrollView mHScrollView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRoot = inflater.inflate(R.layout.tabs_fragment, null);
		mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
		mTabHost.setOnTabChangedListener(this);
		mTabHost.setup();

		mViewPager = (ViewPager) mRoot.findViewById(R.id.viewPager);
		mTabsAdapter = new TabsAdapter(getActivity(), getFragmentManager());
		mTabsAdapter.notifyDataSetChanged();
		mViewPager.setAdapter(mTabsAdapter);
		mViewPager.setOnPageChangeListener(this);

		mHScrollView = (HorizontalScrollView) mRoot
				.findViewById(R.id.hScrollView);

		for (int i = 0; i < Constantes.QTD_RODADAS; i++) {
			mTabHost.addTab(newTab(i, "RODADA " + (i + 1)));
		}

		return mRoot;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		setRetainInstance(true);

		mCurrentTab = Utils.getSharedPreferences().getInt(
				Constantes.RODADA_ATUAL, 0) - 1;
		mViewPager.setCurrentItem(mCurrentTab);
	}

	private TabSpec newTab(Integer idTab, String tag) {
		ViewGroup viewGroup = (ViewGroup) mRoot.findViewById(android.R.id.tabs);
		View indicator = LayoutInflater.from(getActivity()).inflate(
				R.layout.tab, viewGroup, false);

		((TextView) indicator.findViewById(R.id.text)).setText(tag);

		TabSpec tabSpec = mTabHost.newTabSpec(idTab.toString());
		tabSpec.setIndicator(indicator);
		tabSpec.setContent(new TabContentFactory() {

			@Override
			public View createTabContent(String tag) {
				return new TextView(getActivity());
			}
		});
		return tabSpec;
	}

	@Override
	public void onTabChanged(String tabId) {
		mCurrentTab = mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(mCurrentTab);
		Log.d(TAG, "onTabChanged(): tabId=" + mCurrentTab);
	}

	@Override
	public void onPageScrollStateChanged(int position) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		TabWidget widget = mTabHost.getTabWidget();
		int oldFocusability = widget.getDescendantFocusability();
		widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		mTabHost.setCurrentTab(position);
		widget.setDescendantFocusability(oldFocusability);

		mHScrollView.scrollTo(position, 0);
		mHScrollView.refreshDrawableState();
	}

}
