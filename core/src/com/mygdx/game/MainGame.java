package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends Game {
	public SpriteBatch batch;
	public BitmapFont font;
	private AssetManager manager;
	public BaseScreen loadingScreen,menuScreen,gameScreen,gameOverScreen,creditsScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font=new BitmapFont();
		manager=new AssetManager();
		manager.load("logo.png",Texture.class);
		manager.load("gota.png", Texture.class);
		manager.load("cubo.png", Texture.class);
		manager.load("piedra.png", Texture.class);
		manager.load("gameover.png", Texture.class);
		manager.load("goteo.wav", Sound.class);
		manager.load("lluvia.mp3", Music.class);

		loadingScreen=new LoadingScreen(this);
		setScreen(loadingScreen);
	}

	public void finishLoading(){
		gameScreen = new GameScreen(this);
		gameOverScreen = new GameOverScreen(this);
		creditsScreen = new CreditsScreen(this);
		menuScreen = new com.mygdx.game.MenuScreen(this);
		setScreen(menuScreen);
	}

	public AssetManager getManager() {
		return manager;
	}
}
