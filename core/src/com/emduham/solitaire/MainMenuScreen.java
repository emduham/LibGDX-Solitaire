package com.emduham.solitaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Evan on 1/16/2017.
 */
public class MainMenuScreen implements Screen, InputProcessor {
    private SolitaireApp game;

    private Sprite back;

    public MainMenuScreen(SolitaireApp game) {
        this.game = game;
    }

    @Override
    public void show() {
        back = new TextureAtlas("resizedcarddeck.pack").createSprite("back", SolitaireApp.BACK);
        back.setPosition(10f, 550f);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        game.getBatch().setProjectionMatrix(game.getCamera().combined);
        game.getBatch().begin();
        back.draw(game.getBatch());
        //TODO - Draw: "Click the deck to start..."
        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        game.getViewport().update(width, height);
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

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button != Input.Buttons.LEFT || pointer > 0) {
            return false;
        }

        Vector3 vec = new Vector3(screenX, screenY, 0);

        vec = game.getCamera().unproject(vec, game.getViewport().getScreenX(), game.getViewport().getScreenY(), game.getViewport().getScreenWidth(), game.getViewport().getScreenHeight());

        if(new Rectangle(10f, 550f, 115f, 160f).contains(vec.x, vec.y)) {
            game.setScreen(new GameScreen(game));
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
