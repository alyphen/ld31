package io.github.alyphen.ld31.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import io.github.alyphen.ld31.LD31;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "LD31";
		config.width = 640;
		config.height = 480;
		config.resizable = false;
		new LwjglApplication(new LD31(), config);
	}
}
