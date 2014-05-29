package br.edsonluis.app.brasileirao.fragment;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import br.edsonluis.app.brasileirao.BrasileiraoApplication;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.activity.MainActivity;
import br.edsonluis.app.brasileirao.util.Utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class SobreFragment extends Fragment {

	private MainActivity mContext;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_sobre, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mContext = (MainActivity) getActivity();
		mContext.restoreActionBar();
		mContext.getSupportActionBar().setTitle(R.string.title_sobre);

		ImageButton b = (ImageButton) mContext.findViewById(R.id.futebits_logo);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://www.futebits.com.br/")));
			}
		});

		setTracker();

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				if (Utils.isOnline()) {
					DefaultHttpClient client = new DefaultHttpClient();
					HttpGet getRequest = new HttpGet(
							"http://www.futebits.com.br/ws/api/getIdentidadeFutebits");
					try {
						HttpResponse getResponse = client.execute(getRequest);
						int statusCode = getResponse.getStatusLine()
								.getStatusCode();
						if (statusCode != HttpStatus.SC_OK) {
							throw new Exception("Erro: " + statusCode);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						client.getConnectionManager().shutdown();
					}
				}
				return null;
			}
		}.execute();

	}

	private void setTracker() {
		Tracker t = ((BrasileiraoApplication) mContext.getApplication())
				.getTracker(BrasileiraoApplication.TrackerName.APP_TRACKER);
		t.setScreenName("Sobre Fragment");
		t.send(new HitBuilders.AppViewBuilder().build());
	}

}
