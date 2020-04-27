package com.yaronfuks.flyingsheep;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.CircleShape;

import java.util.Random;

public class FlyingSheep extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture background;
    private Texture bottomTube;
    private Texture topTube;
    private Texture gameOver;
    private int score = 0;
    private int scoringTube = 0;

    private Circle birdCircle;
    private ShapeRenderer shapeRenderer;

    private Rectangle[] topTubeRectangles;
    private Rectangle[] bottomTubeRectangles;
    private BitmapFont font;


    private Texture[] birds;
    private int flapState = 0;
    private float birdY = 0;
    private float velocity = 0;
    private int gameState = 0;
    private float gravity = 2;
    private float gap = 600;
    private float maxTubeOffset;
    private Random randomGenerator;
    private float tubeVelocity = 4;
    private int numberOfTubes = 4;
    private float[] tubeX = new float[numberOfTubes];
    private float[] tubeOffset = new float[numberOfTubes];
    private float distanceBetweenTubes;


    @Override
    public void create() {
        batch = new SpriteBatch();

        birdCircle = new Circle();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        gameOver = new Texture("gameover.png");
        background = new Texture("bg.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");


        bottomTube = new Texture("bottomtube.png");
        topTube = new Texture("toptube.png");
        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 0.80f;
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

       startGame();



        }

        public void startGame() {

            birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
            for (int i = 0; i < numberOfTubes; i++) {

                tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 600);

                tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() / 1.8f - 10 + i * distanceBetweenTubes;

                topTubeRectangles[i] = new Rectangle();
                bottomTubeRectangles[i] = new Rectangle();

            }
        }

    @Override
    public void render() {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        if (gameState == 1) {

            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2 - birds[0].getWidth()/2 - topTube.getWidth()){
                score++;
                Gdx.app.log("Score", String.valueOf(score));

                if (scoringTube < numberOfTubes - 1){
                    scoringTube++;
                }else {
                    scoringTube = 0;
                }
            }
            if (Gdx.input.justTouched()) {
                velocity = -30;
            }

            for (int i = 0; i < numberOfTubes; i++) {
                if (tubeX[i] < -topTube.getWidth()) {

                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

                } else {

                    tubeX[i] -= tubeVelocity;
                }
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth() + 20, topTube.getHeight() + 1000);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - topTube.getHeight() + tubeOffset[i] - 1000, topTube.getWidth() + 20, topTube.getHeight() + 1000);

                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth() + 20, topTube.getHeight() + 1000);
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - topTube.getHeight() + tubeOffset[i] - 1000, topTube.getWidth() + 20, topTube.getHeight() + 1000);

            }
            if (birdY > 0) {

                velocity += gravity;
                birdY -= velocity + 0.7f;
            }else {
                gameState = 2;
            }

        } else if (gameState == 0){
            if (Gdx.input.justTouched()) {

                gameState = 1;
            }

        }else if (gameState == 2){
            batch.draw(gameOver,  0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            if (Gdx.input.justTouched()) {

                if (Gdx.input.justTouched()){

                    gameState = 1;
                    startGame();
                    score = 0;
                    scoringTube = 0;
                    velocity = 0;

                }


            }

        }
        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }

        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        font.draw(batch, String.valueOf(score), 100, 200);
        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);


        for (int i=0; i<numberOfTubes; i++){

            if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){

               gameState = 2;
            }

        }


    }

    @Override
    public void dispose() {
        batch.dispose();

    }
}
