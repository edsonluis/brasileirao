package br.edsonluis.app.brasileirao.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.activity.HomeActivity;
import br.edsonluis.app.brasileirao.model.Jogos;
import br.edsonluis.app.brasileirao.model.Rodada;
import br.edsonluis.app.brasileirao.model.RodadaWrapper;
import br.edsonluis.app.brasileirao.util.Constantes;

public class JogosFragment extends Fragment implements ActionBar.OnNavigationListener {

	private ActionBar actionBar;
	private ProgressDialog dialog;
	private HomeActivity context;
	private Rodada dadosRodada;
	private List<Jogos> listJogos;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private int rodadaAtual = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_jogos, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		context = (HomeActivity) getActivity();

		String dialogMessage = getString(R.string.dialog_carregando);
		dialog = ProgressDialog.show(context, null, dialogMessage, true);
		dialog.hide();

		actionBar = context.getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		List<String> TITLES = new ArrayList<String>();
		for (int i = 0; i < Constantes.QTD_RODADAS; i++) {
			TITLES.add(i, "Rodada " + (i + 1));
		}

		adapter = new ArrayAdapter<String>(actionBar.getThemedContext(),
				android.R.layout.simple_spinner_item, android.R.id.text1,
				TITLES);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		actionBar.setListNavigationCallbacks(adapter, this);

		loadData();
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		rodadaAtual = position + 1;
		loadData();
		return false;
	}

	private void loadData() {

		new AsyncTask<Void, Void, Void>() {

			private RodadaWrapper rodadaWrapper;

			protected void onPreExecute() {
				dialog.show();
			};

			@Override
			protected Void doInBackground(Void... params) {
				try {
					rodadaWrapper = (rodadaAtual == 0) ? Rodada
							.obterRodadaAtual() : Rodada
							.obterRodada(rodadaAtual);
					dadosRodada = rodadaWrapper.dadosRodada;
					listJogos = rodadaWrapper.jogos;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				dialog.hide();

				if (rodadaAtual == 0)
					actionBar.setSelectedNavigationItem(dadosRodada.rodada - 1);

				rodadaAtual = dadosRodada.rodada;
				if (listJogos != null && listJogos.size() > 0) {

				}
			};

		}.execute();
	}
}
