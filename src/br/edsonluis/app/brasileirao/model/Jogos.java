package br.edsonluis.app.brasileirao.model;

import java.io.Serializable;

import br.edsonluis.app.brasileirao.util.Utils;

public class Jogos implements Serializable {

	private static final long serialVersionUID = 1L;

	public String nome_equipe_mandante;
	public String nome_equipe_visitante;
	public String estadio;
	public String cidade;
	public String estado;
	public String pais;
	public String data;
	public String hora;
	public String gols_mandante;
	public String gols_visitante;
	public Escudo escudo_equipe_mandante;
	public Escudo escudo_equipe_visitante;

	public String getNomeMandante() {
		return Utils.abreviarNome(nome_equipe_mandante);
	}

	public int getEscudoMandante() {
		return Utils.getDrawableResourceByName(escudo_equipe_mandante._150x150
				.replace("http://www.futebits.com.br/content/escudos/150x150/",
						"").replace(".png", ""));
	}

	public String getNomeVisitante() {
		return Utils.abreviarNome(nome_equipe_visitante);
	}

	public int getEscudoVisitante() {
		return Utils.getDrawableResourceByName(escudo_equipe_visitante._150x150
				.replace("http://www.futebits.com.br/content/escudos/150x150/",
						"").replace(".png", ""));
	}
}
