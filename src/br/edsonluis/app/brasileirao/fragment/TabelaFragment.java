package br.edsonluis.app.brasileirao.fragment;

import java.util.List;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.activity.MainActivity;
import br.edsonluis.app.brasileirao.model.Tabela;
import br.edsonluis.app.brasileirao.util.Constantes;
import br.edsonluis.app.brasileirao.util.Utils;

public class TabelaFragment extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener {

	private TableLayout mTableLayout;
	private List<Tabela> mListData;
	private MainActivity mContext;
	private SwipeRefreshLayout mSwipeLayout;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tabela, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mContext = (MainActivity) getActivity();
		mContext.restoreActionBar();

		mTableLayout = (TableLayout) mContext.findViewById(R.id.table_layout);
		int dips;
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			dips = Utils.convertPixelsToDp(20);
		} else {
			dips = Utils.convertPixelsToDp(120);
		}
		mTableLayout.getChildAt(0).findViewById(R.id.im_escudo)
				.setLayoutParams(new LayoutParams(dips, dips));
		setSwipeLayout();

		loadData(checkFirstRun());
	}

	private void setSwipeLayout() {

		mSwipeLayout = (SwipeRefreshLayout) mContext
				.findViewById(R.id.swipe_container);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorScheme(R.color.holo_green_dark,
				R.color.holo_red_dark, R.color.holo_blue_dark,
				R.color.holo_orange_dark);
	}

	private boolean checkFirstRun() {

		boolean forceUpdate = Utils.getSharedPreferences().getBoolean(
				Constantes.FIRST_RUN, true);

		if (forceUpdate)
			Utils.updateFirstRun();
		else if (Utils.isOnline())
			forceUpdate = true;

		return forceUpdate;
	}

	private void loadData(final boolean forceUpdate) {

		if (forceUpdate || mTableLayout.getChildCount() == 1) {
			new AsyncTask<Void, Void, Void>() {

				protected void onPreExecute() {
					mSwipeLayout.setRefreshing(true);
				};

				@Override
				protected Void doInBackground(Void... params) {
					try {
						mListData = Tabela.obterTabela(forceUpdate);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				}

				protected void onPostExecute(Void result) {
					mSwipeLayout.setRefreshing(false);
					if (mListData != null && mListData.size() > 0) {
						for (Tabela item : mListData) {
							if (mTableLayout.getChildAt(item.Posicao) != null)
								mTableLayout.removeViewAt(item.Posicao);
							mTableLayout
									.addView(getRowView(item), item.Posicao);
						}
					} else {
						mContext.showMensagemErroGenerico();
					}
				};

			}.execute();
		}
	}

	public View getRowView(Tabela item) {
		View view = mContext.getLayoutInflater().inflate(
				R.layout.tablerow_tabela_item, null);

		TextView posicao = (TextView) view.findViewById(R.id.tv_posicao);
		ImageView escudo = (ImageView) view.findViewById(R.id.im_escudo);
		TextView nome = (TextView) view.findViewById(R.id.tv_nome);
		TextView pontos = (TextView) view.findViewById(R.id.tv_pontos);
		TextView jogos = (TextView) view.findViewById(R.id.tv_jogos);
		TextView vitorias = (TextView) view.findViewById(R.id.tv_vitorias);
		TextView empates = (TextView) view.findViewById(R.id.tv_empates);
		TextView derrotas = (TextView) view.findViewById(R.id.tv_derrotas);
		TextView gols_pro = (TextView) view.findViewById(R.id.tv_gols_pro);
		TextView gols_contra = (TextView) view
				.findViewById(R.id.tv_gols_contra);
		TextView saldo_gols = (TextView) view.findViewById(R.id.tv_saldo_gols);

		posicao.setText("" + item.Posicao);
		nome.setText(item.getNome().trim());
		pontos.setText(item.P);
		jogos.setText(item.J);
		vitorias.setText(item.V);
		empates.setText(item.E);
		derrotas.setText(item.D);
		gols_pro.setText(item.GP);
		gols_contra.setText(item.GC);
		saldo_gols.setText(item.SG);
		int dips;
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			dips = Utils.convertPixelsToDp(20);
		} else {
			dips = Utils.convertPixelsToDp(120);
		}
		escudo.setLayoutParams(new LayoutParams(dips, dips));
		escudo.setImageDrawable(getResources().getDrawable(item.getEscudo()));

		if (item.Posicao > 0 && item.Posicao <= 4)
			posicao.setTextColor(getResources().getColor(
					R.color.holo_blue_light));
		else if (item.Posicao > 16 && item.Posicao <= 20)
			posicao.setTextColor(getResources()
					.getColor(R.color.holo_red_light));

		if ((item.Posicao % 2) == 0)
			view.setBackgroundColor(getResources().getColor(
					R.color.holo_gray_light));
		else
			view.setBackgroundColor(getResources().getColor(
					android.R.color.white));

		return view;
	}

	@Override
	public void onRefresh() {
		loadData(true);
	}

}
