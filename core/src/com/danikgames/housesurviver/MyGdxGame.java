package com.danikgames.housesurviver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
	SpriteBatch batch;
	OrthographicCamera camera;

	Texture test, gameButtonsT, grassBlockT, houseT, treeT, stoneT, ironOreT, appleT;

	GameScreen mainScr;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();

		camera.setToOrtho(false, 1600, 800);

		test = new Texture(Gdx.files.internal("badlogic.jpg"));
		gameButtonsT = new Texture(Gdx.files.internal("game_buttons.png"));
		grassBlockT = new Texture(Gdx.files.internal("grass_block.png"));
		houseT = new Texture(Gdx.files.internal("house.png"));
		treeT = new Texture(Gdx.files.internal("tree.png"));
		stoneT = new Texture(Gdx.files.internal("stone.png"));
		ironOreT = new Texture(Gdx.files.internal("iron_ore.png"));
		appleT = new Texture(Gdx.files.internal("apple.png"));

		mainScr = new GameScreen(this);

		setScreen(mainScr);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		test.dispose();
		gameButtonsT.dispose();
		grassBlockT.dispose();
		houseT.dispose();
		treeT.dispose();
		stoneT.dispose();
		ironOreT.dispose();
		appleT.dispose();
		super.dispose();
	}
}
