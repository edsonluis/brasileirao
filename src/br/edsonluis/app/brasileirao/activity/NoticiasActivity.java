package br.edsonluis.app.brasileirao.activity;

import android.os.Bundle;
import br.edsonluis.app.brasileirao.fragment.NoticiasFragment;

public class NoticiasActivity extends DefaultActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setFragment(new NoticiasFragment());
	}
}
