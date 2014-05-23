package br.edsonluis.app.brasileirao.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.edsonluis.app.brasileirao.BrasileiraoApplication;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.activity.MainActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class SobreFragment extends Fragment {

	private MainActivity context;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sobre, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		context = (MainActivity) getActivity();
		context.restoreActionBar();

		setTracker();
	}

	private void setTracker() {
		Tracker t = ((BrasileiraoApplication) context.getApplication())
				.getTracker(BrasileiraoApplication.TrackerName.APP_TRACKER);
		t.setScreenName("Sobre Fragment");
		t.send(new HitBuilders.AppViewBuilder().build());
	}

}
