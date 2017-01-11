package com.emduham.solitaire;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Evan on 2017-01-10.
 */
public class InputHandler implements InputProcessor {
    private GameScreen gameScreen;

    public InputHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
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
        if(button != Input.Buttons.LEFT || pointer > 0) {return false;}

        Vector3 vec = new Vector3(screenX, screenY, 0);

        vec = gameScreen.getGame().getCamera().unproject(vec);

        gameScreen.getBounds(CardPosition.DISCARD);

        //If click on discard, start dragging top discard card
        if(!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.DISCARD).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.DISCARD);
            gameScreen.startDragDiscard();
            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button != Input.Buttons.LEFT || pointer > 0) {return false;}

        Vector3 vec = new Vector3(screenX, screenY, 0);

        vec = gameScreen.getGame().getCamera().unproject(vec);

        //Flip 3 cards from stock, or replenish stock
        if(gameScreen.getBounds(CardPosition.STOCK).contains(vec.x, vec.y)) {
            gameScreen.discard3();
        }

        if(gameScreen.getDragging()) {
            gameScreen.stopDragging();
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(pointer > 0) {
            return false;
        }

        Vector3 vec = new Vector3(screenX, screenY, 0);

        vec = gameScreen.getGame().getCamera().unproject(vec);

        gameScreen.setCardBufferLocation(vec.x, vec.y);

        return true;
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
