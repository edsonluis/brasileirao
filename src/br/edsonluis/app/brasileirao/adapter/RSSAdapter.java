package br.edsonluis.app.brasileirao.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.model.RSSItem;

public class RSSAdapter extends ArrayAdapter<RSSItem> {
	
	private List<RSSItem> objects = null;
	private Context context;

	public RSSAdapter(Context context, int textviewid, List<RSSItem> objects) {
		super(context, textviewid, objects);

		this.context = context;
		this.objects = objects;
	}

	@Override
	public int getCount() {
		return ((null != objects) ? objects.size() : 0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RSSItem getItem(int position) {
		return ((null != objects) ? objects.get(position) : null);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (null == view) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.listview_noticias_item, null);
		}

		RSSItem data = objects.get(position);

		if (null != data) {
			TextView title = (TextView) view.findViewById(R.id.txtTitle);
			TextView date = (TextView) view.findViewById(R.id.txtDate);

			title.setText(data.title);
			date.setText("em " + data.getPubDate());
		}

		return view;
	}
}
