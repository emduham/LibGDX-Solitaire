package com.emduham.solitaire;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
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
        if(keycode == Input.Keys.R) {
            gameScreen.getGame().setScreen(new GameScreen(gameScreen.getGame()));
            return true;
        }

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

        vec = gameScreen.getGame().getCamera().unproject(vec, gameScreen.getGame().getViewport().getScreenX(), gameScreen.getGame().getViewport().getScreenY(), gameScreen.getGame().getViewport().getScreenWidth(), gameScreen.getGame().getViewport().getScreenHeight());

        gameScreen.getBounds(CardPosition.DISCARD);

        //If click on discard, start dragging top discard card
        if(!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.DISCARD).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.DISCARD);
            gameScreen.startDragDiscard();
            return true;
        }

        if(!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW1).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW1);
            int index = gameScreen.getRows()[0].size()-1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for(int i = 0; i < gameScreen.getRows()[0].size() - 1; i++) {
                if(currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW1, index);
            return true;
        }

        if(!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW2).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW2);
            int index = gameScreen.getRows()[1].size()-1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for(int i = 0; i < gameScreen.getRows()[1].size() - 1; i++) {
                if(currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW2, index);
            return true;
        }

        if(!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW3).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW3);
            int index = gameScreen.getRows()[2].size()-1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for(int i = 0; i < gameScreen.getRows()[2].size() - 1; i++) {
                if(currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW3, index);
            return true;
        }

        if(!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW4).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW4);
            int index = gameScreen.getRows()[3].size()-1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for(int i = 0; i < gameScreen.getRows()[3].size() - 1; i++) {
                if(currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW4, index);
            return true;
        }

        if(!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW5).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW5);
            int index = gameScreen.getRows()[4].size()-1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for(int i = 0; i < gameScreen.getRows()[4].size() - 1; i++) {
                if(currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW5, index);
            return true;
        }

        if(!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW6).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW6);
            int index = gameScreen.getRows()[5].size()-1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for(int i = 0; i < gameScreen.getRows()[5].size() - 1; i++) {
                if(currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW6, index);
            return true;
        }

        if(!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW7).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW7);
            int index = gameScreen.getRows()[6].size()-1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for(int i = 0; i < gameScreen.getRows()[6].size() - 1; i++) {
                if(currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW7, index);
            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button != Input.Buttons.LEFT || pointer > 0) {return false;}

        Vector3 vec = new Vector3(screenX, screenY, 0);

        vec = gameScreen.getGame().getCamera().unproject(vec, gameScreen.getGame().getViewport().getScreenX(), gameScreen.getGame().getViewport().getScreenY(), gameScreen.getGame().getViewport().getScreenWidth(), gameScreen.getGame().getViewport().getScreenHeight());

        //Flip 3 cards from stock, or replenish stock
        if(gameScreen.getBounds(CardPosition.STOCK).contains(vec.x, vec.y)) {
            gameScreen.discard3();
        }

        if(gameScreen.getDragging()) {
            gameScreen.stopDragging(vec.x, vec.y);
            gameScreen.flipLastRowCards();
        }

        if(gameScreen.isFinished()) {
            gameScreen.getGame().setScreen(new GameScreen(gameScreen.getGame()));
            return true;
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(pointer > 0) {
            return false;
        }

        Vector3 vec = new Vector3(screenX, screenY, 0);

        vec = gameScreen.getGame().getCamera().unproject(vec, gameScreen.getGame().getViewport().getScreenX(), gameScreen.getGame().getViewport().getScreenY(), gameScreen.getGame().getViewport().getScreenWidth(), gameScreen.getGame().getViewport().getScreenHeight());

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
