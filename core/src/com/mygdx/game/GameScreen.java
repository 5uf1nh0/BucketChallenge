package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sufin on 24/02/2017.
 */

public class GameScreen extends BaseScreen {
    public Texture dropImage;
    public Texture bucketImage;
    public Texture stoneImage;
    public Sound dropSound;
    public Music rainMusic;
    public OrthographicCamera camera;
    public Rectangle bucket,rock;
    public Array<Rectangle> raindrops,rainstones;
    private Stage stage;
    public long lastDropTime;
    public int cuentaGotas;


    public GameScreen(com.mygdx.game.MainGame game) {
        super(game);

        stage=new Stage();
        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("gota.png"));
        stoneImage = new Texture(Gdx.files.internal("piedra.png"));
        bucketImage = new Texture(Gdx.files.internal("cubo.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = game.getManager().get("goteo.wav");
        rainMusic = game.getManager().get("lluvia.mp3");
        rainMusic.setLooping(true);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // create a Rectangle to logically represent the bucket
        bucket = new Rectangle();
        //bucket.set(bucket.getX(),bucket.getY(),bucket.getWidth(),bucket.getHeight());
        bucket.x = 800 / 2 - 64 / 2; // center the bucket horizontally
        bucket.y = 20; // bottom left corner of the bucket is 20 pixels above

        // the bottom screen edge
        bucket.width = 64;
        bucket.height = 64;

        rock=new Rectangle();
        rock.set(rock.getX(),rock.getY(),rock.getWidth(),rock.getHeight());
        // create the raindrops array and spawn the first raindrop
        raindrops = new Array<Rectangle>();
        rainstones = new Array<Rectangle>();
        spawnRaindrop();

    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();

        Rectangle rainstone = new Rectangle();
        rainstone.x = MathUtils.random(0, 800 - 64);
        rainstone.y = 480;
        rainstone.width = 64;
        rainstone.height = 64;
        rainstones.add(rainstone);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        game.batch.begin();
        game.font.draw(game.batch, "Gotas capturadas: " + cuentaGotas, 0, 480);
        game.batch.draw(bucketImage, bucket.getX(), bucket.getY(), bucket.getWidth(), bucket.getHeight());
        game.batch.draw(stoneImage, rock.getX(), rock.getY(), rock.getWidth(), rock.getHeight());


        for (Rectangle raindrop : raindrops) {
            game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }

        for (Rectangle rainstone : rainstones) {
            game.batch.draw(stoneImage, rainstone.x, rainstone.y);
        }

        game.batch.end();

        // process user input
        if (Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucket.x = touchPos.x - 64 / 2;
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            bucket.x += 200 * Gdx.graphics.getDeltaTime();

        // make sure the bucket stays within the screen bounds
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > 800 - 64)
            bucket.x = 800 - 64;

        // check if we need to create a new raindrop
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRaindrop();

        comprobarColisiones();

        // move the raindrops, remove any that are beneath the bottom edge of
        // the screen or that hit the bucket. In the later case we increase the
        // value our drops counter and add a sound effect.
        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();

            if (raindrop.y + 64 < 0)
                iter.remove();

            if (raindrop.overlaps(bucket)) {
                cuentaGotas++;
                dropSound.play();
                iter.remove();
            }

            Iterator<Rectangle> iter2 = rainstones.iterator();
            Rectangle rainstone = iter2.next();
            rainstone.y -= 200 * Gdx.graphics.getDeltaTime();

            if(rainstone.y + 64<0)
                iter2.remove();

            if (rainstone.overlaps(bucket)) {
                //rockSound.play();
                game.setScreen(game.gameOverScreen);
            }

        }

    }

    private void comprobarColisiones(){

        if(bucket.getY()+bucket.getHeight() < rock.getHeight()){
            System.out.println("Colision");
            stage.addAction(
                    Actions.sequence(Actions.delay(1f),Actions.run(new Runnable(){
                        public void run(){
                            game.setScreen(game.gameOverScreen);
                        }
                    }))
            );
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        rainMusic.play();

    }

    @Override
    public void hide() {stage.clear();}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
    }

}
