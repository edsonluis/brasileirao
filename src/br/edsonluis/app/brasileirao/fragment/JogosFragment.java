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
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.activity.JogosActivity;
import br.edsonluis.app.brasileirao.adapter.JogosAdapter;
import br.edsonluis.app.brasileirao.model.Jogos;
import br.edsonluis.app.brasileirao.model.Rodada;
import br.edsonluis.app.brasileirao.model.RodadaWrapper;
import br.edsonluis.app.brasileirao.util.Constantes;
import br.edsonluis.app.brasileirao.util.Utils;

public class JogosFragment extends Fragment implements
		ActionBar.OnNavigationListener, SwipeRefreshLayout.OnRefreshListener {

	public static final String ARG_SECTION_NUMBER = "section_number";

	private ActionBar mActionBar;
	private JogosActivity mContext;
	private Rodada dadosRodada;
	private List<Jogos> listJogos;
	private ListView mListView;
	private JogosAdapter mListAdapter;
	private SwipeRefreshLayout mSwipeLayout;
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

		mContext = (JogosActivity) getActivity();

		mListView = (ListView) mContext.findViewById(R.id.listview_jogos);

		setActionBar();
		setSwipeLayout();

		rodadaAtual = Utils.getSharedPreferences().getInt(
				Constantes.RODADA_ATUAL, 0) - 1;

		mActionBar.setSelectedNavigationItem(rodadaAtual);
	}

	private void setSwipeLayout() {

		mSwipeLayout = (SwipeRefreshLayout) mContext
				.findViewById(R.id.swipe_container);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorScheme(R.color.holo_green_dark,
				R.color.holo_red_dark, R.color.holo_blue_dark,
				R.color.holo_orange_dark);
	}

	private void setActionBar() {

		mActionBar = mContext.getSupportActionBar();
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		List<String> dropdownValues = new ArrayList<String>();
		for (int i = 0; i < Constantes.QTD_RODADAS; i++) {
			dropdownValues.add((i + 1) + "ª RODADA");
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				mActionBar.getThemedContext(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				dropdownValues);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mActionBar.setListNavigationCallbacks(adapter, this);

	}

	private void loadData(final boolean forceUpdate) {

		new AsyncTask<Void, Void, Void>() {

			private RodadaWrapper rodadaWrapper;

			protected void onPreExecute() {
				mSwipeLayout.setRefreshing(true);
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
				mSwipeLayout.setRefreshing(false);
				rodadaAtual = dadosRodada.rodada;
				if (listJogos != null && listJogos.size() > 0) {
					Collections.sort(listJogos);
					mListAdapter = new JogosAdapter(mContext, listJogos);
					mListAdapter.notifyDataSetChanged();
					mListView.setAdapter(mListAdapter);
					mListView.setVisibility(View.VISIBLE);
				} else {
					mListView.setVisibility(View.GONE);
					mContext.showMensagemErroGenerico();
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
