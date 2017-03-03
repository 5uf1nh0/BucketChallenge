package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by sufin on 02/03/2017.
 */

public class MenuScreen extends BaseScreen{
    private TextButton play, credits;
    private Stage stage;
    private Skin skin;
    private Texture logo;
    private SpriteBatch batch;
    private int width,heigth;

    public MenuScreen(final MainGame game ) {
        super(game);
        batch=new SpriteBatch();
        stage = new Stage(new FitViewport(640,360));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        play = new TextButton("Jugar",skin);
        credits = new TextButton("Creditos",skin);
        logo = new Texture("logo.png");

        width=Gdx.graphics.getWidth();
        heigth=Gdx.graphics.getHeight();

        play.addCaptureListener((new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        }));

        credits.addCaptureListener((new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(game.creditsScreen);
            }
        }));

        play.setSize(200,80);
        credits.setSize(200,80);
        play.setPosition(40,140);
        credits.setPosition(40,40);

        stage.addActor(play);
        stage.addActor(credits);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.5f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(logo,width-logo.getWidth(),heigth-logo.getHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

}
