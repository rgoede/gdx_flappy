package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class Flappy extends ApplicationAdapter {
    private Texture birdImage;
    private Texture obstacleImage;
    private TextureRegion invertedObstacleImage;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private int speed = 0;
    private int altitude = 200;
    private Array<Rectangle> bottomObstacles;
    private Array<Rectangle> topObstacles;


    @Override
    public void create() {
        birdImage = new Texture(Gdx.files.internal("bird.gif"));
        obstacleImage = new Texture(Gdx.files.internal("obstacle.jpg"));
        invertedObstacleImage = new TextureRegion(obstacleImage);
        invertedObstacleImage.flip(false, true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        batch = new SpriteBatch();
        bottomObstacles = new Array<Rectangle>();
        topObstacles = new Array<Rectangle>();

        spawnObstacle();
    }

    @Override
    public void render() {
        clear();
        updateBirdPosition();
        updateAllObstaclePosition();
        draw();
    }

    private void updateAllObstaclePosition() {
        boolean removedObject = false;
        Iterator<Rectangle> iter1 = bottomObstacles.iterator();
        while (iter1.hasNext()) {
            Rectangle obstacle1 = iter1.next();
            obstacle1.x -= 200 * Gdx.graphics.getDeltaTime();
            if (obstacle1.x < -200) {
                iter1.remove();
                removedObject = true;
            }
        }
        if (topObstacles.size > 1) {
            throw new RuntimeException();
        }
        Iterator<Rectangle> iter = topObstacles.iterator();
        while (iter.hasNext()) {
            Rectangle obstacle = iter.next();
            obstacle.x -= 200 * Gdx.graphics.getDeltaTime();
            if (obstacle.x <  -200) {
                iter.remove();
                removedObject = true;
            }
        }
        if (removedObject) {
            spawnObstacle();
        }
    }

    private void updateBirdPosition() {
        altitude += (speed / 5);
        speed -= 1;
        if (Gdx.input.isTouched()) {
            speed = 20;
        }
    }

    private void clear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Rectangle obstacle : bottomObstacles) {
            batch.draw(obstacleImage, obstacle.x, obstacle.y);
        }
        for (Rectangle obstacle : topObstacles) {
            batch.draw(invertedObstacleImage, obstacle.x, obstacle.y);
        }
        batch.draw(birdImage, 250, altitude, 100, 100);
        batch.end();
    }

    private void spawnObstacle() {
        Rectangle bottomObstacle = new Rectangle();
        bottomObstacle.x = 800;
        bottomObstacle.y = MathUtils.random(-200, 0);
        bottomObstacle.width = 150;
        bottomObstacle.height = 300;
        bottomObstacles.add(bottomObstacle);
        Rectangle topObstacle = new Rectangle();
        topObstacle.x = 800;
        topObstacle.y = bottomObstacle.y + 400;
        topObstacle.width = 150;
        topObstacle.height = 300;
        topObstacles.add(topObstacle);
    }

}
