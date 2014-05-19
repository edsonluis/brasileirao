package br.edsonluis.app.brasileirao.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import br.edsonluis.app.brasileirao.fragment.JogosFragment;
import br.edsonluis.app.brasileirao.util.Constantes;

public class TabsAdapter extends FragmentStatePagerAdapter {

	private Context mContext;
	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

	static final class TabInfo {
		private final Bundle args;

		TabInfo(Bundle _args) {
			args = _args;
		}
	}

	public TabsAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;

		TabInfo info = null;
		Bundle args = null;
		for (int i = 0; i < Constantes.QTD_RODADAS; i++) {
			args = new Bundle();
			args.putInt(JogosFragment.ARG_SECTION_NUMBER, i + 1);
			info = new TabInfo(args);
			mTabs.add(info);
		}
	}

	@Override
	public int getCount() {
		return mTabs.size();
	}

	@Override
	public Fragment getItem(int position) {
		TabInfo info = mTabs.get(position);
//		Fragment fragment = JogosFragment.newInstance(info.args);
//		if (fm.findFragmentByTag(info.tag) == null) {
//			FragmentTransaction transaction = fm.beginTransaction();
//			transaction.replace(android.R.id.tabcontent, fragment, info.tag);
//			transaction.commit();
//		}
		return Fragment.instantiate(mContext, JogosFragment.class.getName(),
				info.args);
		// return fragment;
	}

	@Override
	public int getItemPosition(Object object) {
		return TabsAdapter.POSITION_NONE;
	}
}
