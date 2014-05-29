package br.edsonluis.app.brasileirao.activity;

import android.os.Bundle;
import br.edsonluis.app.brasileirao.fragment.JogosFragment;

public class JogosActivity extends DefaultActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setFragment(new JogosFragment());
	}
}
