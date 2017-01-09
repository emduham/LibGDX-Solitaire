package com.emduham.solitaire.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emduham.solitaire.SolitaireApp;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Solitaire";
		cfg.width = 1280;
		cfg.height = cfg.width/16*9;
		cfg.resizable = false;
		new LwjglApplication(new SolitaireApp(), cfg);
	}
}
