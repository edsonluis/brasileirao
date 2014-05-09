package br.edsonluis.app.brasileirao.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Escudo implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("200x200")
	public String _200x200;
	
	@SerializedName("150x150")
	public String _150x150;
	
	@SerializedName("100x100")
	public String _100x100;
	
	@SerializedName("75x75")
	public String _75x75;
	
	@SerializedName("50x50")
	public String _50x50;
	
	@SerializedName("35x35")
	public String _35x35;
	
	@SerializedName("25x25")
	public String _25x25;
	
	@SerializedName("16x16")
	public String _16x16;
}
