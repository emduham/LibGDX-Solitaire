package com.emduham.solitaire.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.emduham.solitaire.SolitaireApp;

class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Solitaire";
        cfg.width = (int) (LwjglApplicationConfiguration.getDesktopDisplayMode().width * 0.8);
        cfg.height = cfg.width / 16 * 9;
        cfg.resizable = true;
        cfg.addIcon("icon128.png", Files.FileType.Internal);
        cfg.addIcon("icon32.png", Files.FileType.Internal);
        cfg.addIcon("icon16.png", Files.FileType.Internal);
        new LwjglApplication(new SolitaireApp(), cfg);
    }
}
