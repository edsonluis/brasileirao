package br.edsonluis.app.brasileirao.model;

import java.io.Serializable;
import java.util.List;


public class TabelaWrapper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public int id_tipo;
	public String ds_tipo;
	public List<Tabela> tabela;
//	public List<Jogos> jogos;
	
}
