package br.edsonluis.app.brasileirao.util;

public class Constantes {

	public static final boolean DEBUG = true;
	
	private static final int EDICAO = 2014;

	private static final String URL_DEFAULT = "http://www.futebits.com.br/ws/api/";
	private static final String URL_PARAM_SERIE_A = "/Campeonato%20Brasileiro/Serie%20A/" + EDICAO + "/Turno/Grupo%20Unico";
	
	public static final int QTD_RODADAS = 38;
	
	public static final String BRASILEIRAO_PREFERENCE = "brasileirao_" + EDICAO;

	public static final String JSON_TABELA = "tabela_json";
	public static final String JSON_RODADA = "json_rodada_";
	public static final String RODADA_ATUAL = "rodada_atual";
	public static final String FIRST_RUN = "first_run";
	
	public static final String URL_TABELA = URL_DEFAULT + "getTabelaGrupo" + URL_PARAM_SERIE_A;
	public static final String URL_RODADA_ATUAL = URL_DEFAULT + "getRodadaAtual" + URL_PARAM_SERIE_A;
	public static final String URL_RODADA = URL_DEFAULT + "getRodada" + URL_PARAM_SERIE_A + "/";
	
}