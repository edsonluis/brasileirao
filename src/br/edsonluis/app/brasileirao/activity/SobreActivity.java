package br.edsonluis.app.brasileirao.activity;

import br.edsonluis.app.brasileirao.fragment.SobreFragment;
import android.os.Bundle;

public class SobreActivity extends DefaultActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setFragment(new SobreFragment());
	}
}
