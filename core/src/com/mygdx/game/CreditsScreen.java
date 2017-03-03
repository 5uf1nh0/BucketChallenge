package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by sufin on 01/03/2017.
 */

public class CreditsScreen extends BaseScreen {

    private Stage stage;
    private Skin skin;
    private Label credits;
    private TextButton back;

    public CreditsScreen(final com.mygdx.game.MainGame game) {
        super(game);

        stage = new Stage(new FitViewport(640, 360));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        back = new TextButton("Atras", skin);

        credits = new Label("BucketChallenge v1.0\n" +
                "Copyright (C) 2016-2017 Sufy Saguer\n" +
                "This game is GNU GPL. Get the code at github.com/5uf1nh0/BucketChallenge\n\n" +
                "Licensed under Creative Commons: By Attribution 3.0", skin);

        back.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Take me to the game screen!
                game.setScreen(game.menuScreen);
            }
        });

        credits.setPosition(20, 340 - credits.getHeight());
        back.setSize(200, 80);
        back.setPosition(40, 50);

        stage.addActor(back);
        stage.addActor(credits);
    }

    @Override
    public void show() {Gdx.input.setInputProcessor(stage);}

    @Override
    public void hide() {Gdx.input.setInputProcessor(null);}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.5f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
}
