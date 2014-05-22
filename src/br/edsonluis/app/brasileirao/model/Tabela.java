package br.edsonluis.app.brasileirao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import br.edsonluis.app.brasileirao.util.Constantes;
import br.edsonluis.app.brasileirao.util.Utils;

import com.google.gson.Gson;

public class Tabela implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String TAG = Tabela.class.getSimpleName();
//	private static final Context context = BrasileiraoApplication.getContext();

	public Equipe Equipe;
	public String P;
	public String J;
	public String V;
	public String E;
	public String D;
	public String GP;
	public String GC;
	public String SG;
	public int Posicao;

	public String getNome() {
		return (Equipe != null) ? Utils.abreviarNome(Equipe.nome_popular)
				: "Clube";
	}

	public int getEscudo() {
		return (Equipe != null) ? Utils
				.getDrawableResourceByName(Equipe.escudo._150x150.replace(
						"http://www.futebits.com.br/content/escudos/150x150/",
						"").replace(".png", "")) : 0;
	}

	private static TabelaWrapper getTabelaWrapper(String json) {
		return new Gson().fromJson(json, TabelaWrapper.class);
	}

	public static List<Tabela> obterTabela() throws Exception {
		return obterTabela(false);
	}

	public static List<Tabela> obterTabela(boolean forceUpdate)
			throws Exception {

		TabelaWrapper tabelaWrapper = null;
		String json = null;

		if (Utils.isOnline()) {
			if (forceUpdate) {

				if (Constantes.DEBUG)
					Log.d(TAG, "URL: " + Constantes.URL_TABELA);

				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet getRequest = new HttpGet(Constantes.URL_TABELA);

				try {
					HttpResponse getResponse = client.execute(getRequest);
					int statusCode = getResponse.getStatusLine()
							.getStatusCode();
					if (statusCode != HttpStatus.SC_OK) {
						throw new Exception("Erro: " + statusCode);
					}

					HttpEntity entity = getResponse.getEntity();
					if (entity != null) {
						json = Utils.convertStreamToString(entity.getContent());
						tabelaWrapper = getTabelaWrapper(json);
						Utils.saveTabelaJson(json);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					client.getConnectionManager().shutdown();
				}
			} else {
				json = Utils.getTabelaJson();
				tabelaWrapper = getTabelaWrapper(json);
			}
		} else {
			json = Utils.getTabelaJson();
			tabelaWrapper = getTabelaWrapper(json);
		}

//		if (json == null) {
//			tabelaWrapper = new TabelaWrapper();
//			tabelaWrapper.tabela = obterTabela(true);
//		}

		return (tabelaWrapper != null) ? tabelaWrapper.tabela
				: new ArrayList<Tabela>();

	}

}
