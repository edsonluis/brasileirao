package br.edsonluis.app.brasileirao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;
import br.edsonluis.app.brasileirao.BrasileiraoApplication;
import br.edsonluis.app.brasileirao.R;
import br.edsonluis.app.brasileirao.util.Constantes;
import br.edsonluis.app.brasileirao.util.Utils;

import com.google.gson.Gson;

public class Tabela implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Context context = BrasileiraoApplication.getContext();

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
		return obterTabela(false);
	}

	public static List<Tabela> obterTabela(boolean forceUpdate) throws Exception {

		TabelaWrapper tabelaWrapper = null;
		String json = null;
		
		if (Utils.isOnline()) {
			if (forceUpdate || Utils.checkUpdateDate()) {
				
				if (Constantes.DEBUG)
					Log.d(Tabela.class.getSimpleName(), "URL: " + Constantes.URL_TABELA);
				
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet getRequest = new HttpGet(Constantes.URL_TABELA);

				try {
					HttpResponse getResponse = client.execute(getRequest);
					int statusCode = getResponse.getStatusLine().getStatusCode();
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
			}
		}
			
		if (json == null) {
			json = Utils.getTabelaJson();
			tabelaWrapper = getTabelaWrapper(json);
			
			if (json == null)
				throw new Exception(context.getString(R.string.mensagem_sem_internet));
		}

		return (tabelaWrapper != null) ? tabelaWrapper.tabela
				: new ArrayList<Tabela>();

	}

}
