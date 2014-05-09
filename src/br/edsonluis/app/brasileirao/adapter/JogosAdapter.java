package br.edsonluis.app.brasileirao.adapter;

import java.util.Collections;
import java.util.List;

import br.edsonluis.app.brasileirao.model.Jogos;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class JogosAdapter extends BaseAdapter {

	private Context context;
	private List<Jogos> lista = Collections.emptyList();
	
	public JogosAdapter(Context context, List<Jogos> lista) {
		this.context = context;
		this.lista = lista;
	}
	
	@Override
	public int getCount() {
		return lista.size();
	}

	@Override
	public Jogos getItem(int position) {
		return lista.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
