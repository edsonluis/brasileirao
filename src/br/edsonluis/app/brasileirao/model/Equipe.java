package br.edsonluis.app.brasileirao.model;

import java.io.Serializable;

public class Equipe implements Serializable {

	private static final long serialVersionUID = 1L;

	public int id;
	public String nome;
	public String nome_popular;
	public String site;
	public String twitter;
	public String cidade;
	public String estado;
	public String pais;
	public Escudo escudo;
}
