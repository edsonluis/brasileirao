package br.edsonluis.app.brasileirao.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.edsonluis.app.brasileirao.BrasileiraoApplication;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.activity.MainActivity;
import br.edsonluis.app.brasileirao.adapter.JogosAdapter;
import br.edsonluis.app.brasileirao.model.Jogos;
import br.edsonluis.app.brasileirao.model.Rodada;
import br.edsonluis.app.brasileirao.model.RodadaWrapper;
import br.edsonluis.app.brasileirao.util.Constantes;
import br.edsonluis.app.brasileirao.util.Utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class JogosFragment extends Fragment implements
		ActionBar.OnNavigationListener, SwipeRefreshLayout.OnRefreshListener {

	public static final String ARG_SECTION_NUMBER = "section_number";

	private ActionBar actionBar;
	private MainActivity context;
	private Rodada dadosRodada;
	private List<Jogos> listJogos;
	private ListView listView;
	private JogosAdapter listAdapter;
	private SwipeRefreshLayout swipeLayout;
	private int rodadaAtual;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_jogos, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		context = (MainActivity) getActivity();

		listView = (ListView) context.findViewById(R.id.listview_jogos);

		setActionBar();
		setSwipeLayout();

		rodadaAtual = Utils.getSharedPreferences().getInt(
				Constantes.RODADA_ATUAL, 0) - 1;

		actionBar.setSelectedNavigationItem(rodadaAtual);
		
		setTracker();
	}

	private void setTracker() {
		Tracker t = ((BrasileiraoApplication) context.getApplication())
				.getTracker(BrasileiraoApplication.TrackerName.APP_TRACKER);
		t.setScreenName("Jogos Fragment");
		t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	private void setSwipeLayout() {

		swipeLayout = (SwipeRefreshLayout) context
				.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(R.color.holo_green_dark,
				R.color.holo_red_dark, R.color.holo_blue_dark,
				R.color.holo_orange_dark);
	}

	private void setActionBar() {

		actionBar = context.getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		List<String> dropdownValues = new ArrayList<String>();
		for (int i = 0; i < Constantes.QTD_RODADAS; i++) {
			dropdownValues.add((i + 1) + "ª RODADA");
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actionBar.getThemedContext(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				dropdownValues);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(adapter, this);

	}

	private void loadData(final boolean forceUpdate) {

		new AsyncTask<Void, Void, Void>() {

			private RodadaWrapper rodadaWrapper;

			protected void onPreExecute() {
				swipeLayout.setRefreshing(true);
			};

			@Override
			protected Void doInBackground(Void... params) {
				try {
					rodadaWrapper = Rodada
							.obterRodada(rodadaAtual, forceUpdate);
					dadosRodada = rodadaWrapper.dadosRodada;
					listJogos = rodadaWrapper.jogos;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				swipeLayout.setRefreshing(false);
				rodadaAtual = dadosRodada.rodada;
				if (listJogos != null && listJogos.size() > 0) {
					Collections.sort(listJogos);
					listAdapter = new JogosAdapter(context, listJogos);
					listAdapter.notifyDataSetChanged();
					listView.setAdapter(listAdapter);
					listView.setVisibility(View.VISIBLE);
				} else {
					listView.setVisibility(View.GONE);
					context.showMensagemErroGenerico();
				}
			};

		}.execute();
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		rodadaAtual = position + 1;
		loadData(true);
		return true;
	}

	@Override
	public void onRefresh() {
		loadData(true);
	}
}
