package br.edsonluis.app.brasileirao.adapter;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.model.Jogos;
import br.edsonluis.app.brasileirao.util.Utils;

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
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.listview_jogos_item, null);
		}
		
		ImageView escudo_m = (ImageView) convertView.findViewById(R.id.im_escudo_mandante);
		ImageView escudo_v = (ImageView) convertView.findViewById(R.id.im_escudo_visitante);
		TextView gols_m = (TextView) convertView.findViewById(R.id.gols_mandante);
		TextView gols_v = (TextView) convertView.findViewById(R.id.gols_visitante);
		TextView data = (TextView) convertView.findViewById(R.id.data_jogo);
		TextView hora = (TextView) convertView.findViewById(R.id.hora_jogo);
		TextView local = (TextView) convertView.findViewById(R.id.local_jogo);
		
		Jogos item = getItem(position);
		
		int dips;
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
			dips = Utils.convertPixelsToDp(40);
		} else {
			dips = Utils.convertPixelsToDp(240);
		}
		escudo_m.setLayoutParams(new LayoutParams(dips, dips));
		escudo_m.setImageResource(item.getEscudoMandante());
		
		escudo_v.setLayoutParams(new LayoutParams(dips, dips));
		escudo_v.setImageResource(item.getEscudoVisitante());

		if (item.gols_mandante != null)
			gols_m.setText(item.gols_mandante);
		else
			gols_m.setText(" ");
		
		if (item.gols_visitante != null)
			gols_v.setText(item.gols_visitante);
		else
			gols_v.setText(" ");
			
		data.setText(item.data);
		local.setText(item.estadio);
		String[] hora_s = item.hora.split(":");
		hora.setText(hora_s[0] + ":" + hora_s[1]);

		return convertView;
	}

}
