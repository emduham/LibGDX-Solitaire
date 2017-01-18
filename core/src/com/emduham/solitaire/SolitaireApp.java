package com.emduham.solitaire;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SolitaireApp extends Game {
    static final int BACK = 5;

    private OrthographicCamera camera;
    private FitViewport viewport;
    private SpriteBatch batch;
    private BitmapFont font48;
    private BitmapFont font16;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);
        viewport = new FitViewport(1280, 720, camera);

        //Setup fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.borderWidth = 1.5f;
        parameter.size = 48;
        font48 = generator.generateFont(parameter);
        parameter.borderWidth = 0.8f;
        parameter.size = 16;
        font16 = generator.generateFont(parameter);
        generator.dispose();
        font48.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font16.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        setScreen(new MainMenuScreen(this, false, null));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(75f / 255f, 180f / 255f, 65f / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    OrthographicCamera getCamera() {
        return camera;
    }

    SpriteBatch getBatch() {
        return batch;
    }

    FitViewport getViewport() {
        return viewport;
    }

    BitmapFont getFont48() {
        return font48;
    }

    BitmapFont getFont16() {
        return font16;
    }
}
