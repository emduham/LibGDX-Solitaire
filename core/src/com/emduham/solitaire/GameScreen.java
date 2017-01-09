package com.emduham.solitaire;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Evan on 2017-01-08.
 */
public class GameScreen implements Screen {
    //Game to setScreen()
    private Game game;

    private SpriteBatch batch;
    private TextureAtlas deckImgs;
    private Sprite testCard;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        deckImgs = new TextureAtlas("resizedcarddeck.pack");
        testCard = deckImgs.createSprite("diamonds", 13);
        testCard.setPosition(0, 0);
    }

    @Override
    public void render(float delta) {
        batch.begin();
        testCard.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        deckImgs.dispose();
    }
}
