package br.edsonluis.app.brasileirao.fragment;

import java.util.Collections;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.activity.HomeActivity;
import br.edsonluis.app.brasileirao.adapter.JogosAdapter;
import br.edsonluis.app.brasileirao.model.Jogos;
import br.edsonluis.app.brasileirao.model.Rodada;
import br.edsonluis.app.brasileirao.model.RodadaWrapper;

public class JogosFragment extends Fragment {

	public static final String ARG_SECTION_NUMBER = "section_number";

	private ProgressDialog dialog;
	private HomeActivity context;
	private Rodada dadosRodada;
	private List<Jogos> listJogos;
	private ListView listView;
	private JogosAdapter listAdapter;
	private int rodadaAtual;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	public static Fragment newInstance(Integer tabId) {
		Fragment fragment = new JogosFragment();
		Bundle args = new Bundle();
		args.putInt(JogosFragment.ARG_SECTION_NUMBER, tabId);
		fragment.setArguments(args);
		return fragment;
	}

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

		listView = (ListView) context.findViewById(R.id.listview_jogos);

		rodadaAtual = getArguments().getInt(ARG_SECTION_NUMBER);

		loadData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (dialog != null)
			dialog.dismiss();
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
					rodadaWrapper = Rodada.obterRodada(rodadaAtual);
					dadosRodada = rodadaWrapper.dadosRodada;
					listJogos = rodadaWrapper.jogos;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				dialog.hide();
				rodadaAtual = dadosRodada.rodada;
				if (listJogos != null && listJogos.size() > 0) {
					Collections.sort(listJogos);
					listAdapter = new JogosAdapter(context, listJogos);
					listAdapter.notifyDataSetChanged();
					listView.setAdapter(listAdapter);
				}
			};

		}.execute();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.findItem(R.id.action_refresh).setVisible(false);
	}
}
