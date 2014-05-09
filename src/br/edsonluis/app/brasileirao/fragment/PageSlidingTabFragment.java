package br.edsonluis.app.brasileirao.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.edsonluis.app.base.widget.PagerSlidingTabStrip;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.util.Constantes;

public class PageSlidingTabFragment extends Fragment {

	public static final String TAG = PageSlidingTabFragment.class
			.getSimpleName();

	public static PageSlidingTabFragment newInstance() {
		PageSlidingTabFragment f = new PageSlidingTabFragment();
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pager, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.tabs);
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		TabPagerAdapter adapter = new TabPagerAdapter(getChildFragmentManager());
		adapter.setTitles();
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);

	}

	public class TabPagerAdapter extends FragmentPagerAdapter {

		private List<String> TITLES = new ArrayList<String>();

		public TabPagerAdapter(FragmentManager fm) {
			super(fm);

		}

		public void setTitles() {
			for (int i = 0; i < Constantes.QTD_RODADAS; i++) {
				TITLES.add(i, "RODADA " + (i + 1));
			}
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES.get(position);
		}

		@Override
		public int getCount() {
			return TITLES.size();
		}

		@Override
		public Fragment getItem(int position) {
			return CalendarioFragment.newInstance(position);
		}

	}

}
