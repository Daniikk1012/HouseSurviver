package com.danikgames.housesurviver.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.danikgames.housesurviver.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		System.setProperty("user.name", "defaultuser0");
		config.width = 800;
		config.height = 450;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
