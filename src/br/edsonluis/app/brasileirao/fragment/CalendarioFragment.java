package br.edsonluis.app.brasileirao.fragment;

import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.activity.HomeActivity;
import br.edsonluis.app.brasileirao.model.Jogos;
import br.edsonluis.app.brasileirao.model.Rodada;
import br.edsonluis.app.brasileirao.model.RodadaWrapper;
import br.edsonluis.app.brasileirao.util.Constantes;

public class CalendarioFragment extends Fragment implements TabListener {

	public static final String ARG_POSITION = "object";

	private ActionBar actionBar;
	private ProgressDialog dialog;
	private HomeActivity context;
	private Rodada dadosRodada;
	private List<Jogos> listJogos;
	private ListView listView;
	private int rodadaAtual;

	public static CalendarioFragment newInstance(int position) {

		CalendarioFragment fragment = new CalendarioFragment();

		Bundle args = new Bundle();
		args.putInt(ARG_POSITION, position);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_calendario, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		context = (HomeActivity) getActivity();
		listView = (ListView) context.findViewById(R.id.listview_calendario);

		String dialogMessage = getString(R.string.dialog_carregando);
		dialog = ProgressDialog.show(context, null, dialogMessage, true);
		dialog.hide();

		actionBar = context.getSupportActionBar();
		if (actionBar.getTabCount() == 0) {
			for (int i = 0; i < Constantes.QTD_RODADAS; i++) {
				String title = "RODADA " + (i + 1);
				Tab tab = actionBar.newTab();
				tab.setTag((i + 1));
				tab.setText(title).setTabListener(CalendarioFragment.this);
				actionBar.addTab(tab);
			}
		}

		Bundle args = getArguments();
		rodadaAtual = args.getInt(ARG_POSITION);

		loadData();
	}

	private void loadData() {

		new AsyncTask<Void, Void, Void>() {

			private RodadaWrapper rodadaWrapper;
			private int tempRodada;

			protected void onPreExecute() {
				dialog.show();
			};

			@Override
			protected Void doInBackground(Void... params) {
				tempRodada = rodadaAtual;
				try {
					rodadaWrapper = (rodadaAtual == 0) ? Rodada
							.obterRodadaAtual() : Rodada
							.obterRodada(rodadaAtual);
					dadosRodada = rodadaWrapper.dadosRodada;
					listJogos = rodadaWrapper.jogos;
					rodadaAtual = dadosRodada.rodada;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				dialog.hide();
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				if (tempRodada == 0)
					actionBar.setSelectedNavigationItem(dadosRodada.rodada - 1);

				if (listJogos != null && listJogos.size() > 0) {

				}
			};

		}.execute();
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
//		loadData();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}
}
