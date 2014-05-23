package br.edsonluis.app.brasileirao.model;

import java.io.Serializable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import br.edsonluis.app.brasileirao.util.Constantes;
import br.edsonluis.app.brasileirao.util.Utils;

import com.google.gson.Gson;

public class Rodada implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String TAG = Rodada.class.getSimpleName();

	public int rodada;
	public String grupo;
	public String fase;
	public int edicao;
	public String divisao;
	public String campeonato;

	private static RodadaWrapper getRodadaWrapper(String json) {
		return new Gson().fromJson(json, RodadaWrapper.class);
	}

	public static RodadaWrapper obterRodadaAtual(boolean forceUpdate)
			throws Exception {
		return obterRodada(0, forceUpdate);
	}

	public static RodadaWrapper obterRodada(final int rodada) throws Exception {
		return obterRodada(rodada, false);
	}

	public static RodadaWrapper obterRodada(final int rodada,
			boolean forceUpdate) throws Exception {

		RodadaWrapper rodadaWrapper = null;
		String json = null;
		String url = (rodada == 0) ? Constantes.URL_RODADA_ATUAL
				: Constantes.URL_RODADA + rodada;

		if (Utils.isOnline() && forceUpdate) {
			if (Constantes.DEBUG)
				Log.d(TAG, "URL: " + url);

			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(url);

			try {
				HttpResponse getResponse = client.execute(getRequest);
				int statusCode = getResponse.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					throw new Exception("Erro: " + statusCode);
				}

				HttpEntity entity = getResponse.getEntity();
				if (entity != null) {
					json = Utils.convertStreamToString(entity.getContent());
					rodadaWrapper = getRodadaWrapper(json);
					Utils.saveRodadaJson(json,
							rodadaWrapper.dadosRodada.rodada, (rodada == 0));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				client.getConnectionManager().shutdown();
			}
		} else {
			json = Utils.getRodadaJson(rodada);
			rodadaWrapper = getRodadaWrapper(json);
		}

		return rodadaWrapper;
	}
}
