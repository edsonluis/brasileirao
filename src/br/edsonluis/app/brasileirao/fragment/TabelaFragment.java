package br.edsonluis.app.brasileirao.fragment;

import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import br.edsonluis.app.base.image.cache.ImageLoader;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.activity.HomeActivity;
import br.edsonluis.app.brasileirao.model.Tabela;
import br.edsonluis.app.brasileirao.util.Utils;

public class TabelaFragment extends Fragment {

	private TableLayout layout;
	private List<Tabela> listData;
	private ProgressDialog dialog;
	private HomeActivity context;
	private ImageLoader imageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tabela, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		context = (HomeActivity) getActivity();
		context.restoreActionBar();

		layout = (TableLayout) context.findViewById(R.id.table_layout);
		dialog = ProgressDialog.show(context, null,
				getString(R.string.dialog_carregando), true, false);
		dialog.hide();
		imageLoader = new ImageLoader(context);
		loadData();
	}

	private void loadData() {
		new AsyncTask<Void, Void, Void>() {

			protected void onPreExecute() {
				dialog.show();
			};

			@Override
			protected Void doInBackground(Void... params) {
				try {
					listData = Tabela.obterTabela();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				dialog.hide();
				if (listData.size() > 0) {
					for (Tabela item : listData) {
						layout.addView(getRowView(item), item.Posicao - 1);
					}
					adjustTableRow();
				}
			};

		}.execute();
	}

	private void adjustTableRow() {

		TableLayout header = (TableLayout) context
				.findViewById(R.id.table_header_layout);

		List<Integer> colWidths = new LinkedList<Integer>();

		for (int rownum = 0; rownum < layout.getChildCount(); rownum++) {
			TableRow rowBody = (TableRow) layout.getChildAt(rownum);
			for (int cellnum = 0; cellnum < rowBody.getChildCount(); cellnum++) {
				View cell = rowBody.getChildAt(cellnum);
				Integer cellWidth = cell.getLayoutParams().width;
				if (colWidths.size() <= cellnum) {
					colWidths.add(cellWidth);
				} else {
					Integer current = colWidths.get(cellnum);
					if (cellWidth > current) {
						colWidths.remove(cellnum);
						colWidths.add(cellnum, cellWidth);
					}
				}
			}
		}

		int dips = Utils.convertPixelsToDp(130);
		((ImageView) context.findViewById(R.id.im_escudo))
				.setLayoutParams(new LayoutParams(0, dips));

		TableRow rowHeader = (TableRow) header.getChildAt(0);
		for (int cellnum = 0; cellnum < rowHeader.getChildCount(); cellnum++) {
			View cell = rowHeader.getChildAt(cellnum);
			cell.getLayoutParams().width = colWidths.get(cellnum);
		}

	}

	public View getRowView(Tabela item) {
		View view = context.getLayoutInflater().inflate(
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
		int dips = Utils.convertPixelsToDp(130);
		escudo.setLayoutParams(new LayoutParams(dips, dips));
		imageLoader.displayImage(item.getEscudo(), escudo);

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
}
