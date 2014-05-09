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

	public String getEscudo() {
		return (Equipe != null) ? Equipe.escudo._150x150 : "";
	}

	private static TabelaWrapper getTabelaWrapper(String json) {
		return new Gson().fromJson(json, TabelaWrapper.class);
	}

	public static List<Tabela> obterTabela() throws Exception {

		TabelaWrapper tabelaWrapper = null;
		String json = Utils.getTabelaJson();

		if (json != null) {
			
			tabelaWrapper = getTabelaWrapper(json);
			
		} else if (Utils.isOnline()) {
			
			if (Constantes.DEBUG)
				Log.d(Tabela.class.getSimpleName(), "URL: " + Constantes.URL_TABELA);
			
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(Constantes.URL_TABELA);

			try {
				HttpResponse getResponse = client.execute(getRequest);
				int statusCode = getResponse.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					throw new Exception("Código de erro: " + statusCode);
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
			throw new Exception("Seu dispositivo está sem internet.");
		}

		return (tabelaWrapper != null) ? tabelaWrapper.tabela
				: new ArrayList<Tabela>();

	}

}
