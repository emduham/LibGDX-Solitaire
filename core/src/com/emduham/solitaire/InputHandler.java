package com.emduham.solitaire;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Evan on 2017-01-10.
 * Handles input for GameScreen
 */
class InputHandler implements InputProcessor {
    private final GameScreen gameScreen;

    private boolean isClick;

    InputHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.isClick = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.R) {
            gameScreen.getGame().setScreen(new MainMenuScreen(gameScreen.getGame(), false, null));
            return true;
        }

        if (keycode == Input.Keys.A) {
            gameScreen.toggleAutocomplete();
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
        if (button != Input.Buttons.LEFT || pointer > 0) {
            return false;
        }

        Vector3 vec = new Vector3(screenX, screenY, 0);

        vec = gameScreen.getGame().getCamera().unproject(vec, gameScreen.getGame().getViewport().getScreenX(), gameScreen.getGame().getViewport().getScreenY(), gameScreen.getGame().getViewport().getScreenWidth(), gameScreen.getGame().getViewport().getScreenHeight());

        //If click on discard, start dragging top discard card
        if (!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.DISCARD).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.DISCARD);
            gameScreen.startDragDiscard();
            //Start isClick timer
            startClickTimer();
            return true;
        }

        if (!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.SPADES).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.SPADES);
            gameScreen.startDragFinal(CardPosition.SPADES);
            //Don't need click detection, no auto-place for final piles
            return true;
        }

        if (!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.CLUBS).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.CLUBS);
            gameScreen.startDragFinal(CardPosition.CLUBS);
            //Don't need click detection, no auto-place for final piles
            return true;
        }

        if (!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.HEARTS).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.HEARTS);
            gameScreen.startDragFinal(CardPosition.HEARTS);
            //Don't need click detection, no auto-place for final piles
            return true;
        }

        if (!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.DIAMONDS).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.DIAMONDS);
            gameScreen.startDragFinal(CardPosition.DIAMONDS);
            //Don't need click detection, no auto-place for final piles
            return true;
        }

        if (!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW1).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW1);
            int index = gameScreen.getRows().get(0).size() - 1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for (int i = 0; i < gameScreen.getRows().get(0).size() - 1; i++) {
                if (currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW1, index);
            //Start isClick timer
            startClickTimer();
            return true;
        }

        if (!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW2).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW2);
            int index = gameScreen.getRows().get(1).size() - 1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for (int i = 0; i < gameScreen.getRows().get(1).size() - 1; i++) {
                if (currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW2, index);
            //Start isClick timer
            startClickTimer();
            return true;
        }

        if (!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW3).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW3);
            int index = gameScreen.getRows().get(2).size() - 1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for (int i = 0; i < gameScreen.getRows().get(2).size() - 1; i++) {
                if (currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW3, index);
            //Start isClick timer
            startClickTimer();
            return true;
        }

        if (!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW4).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW4);
            int index = gameScreen.getRows().get(3).size() - 1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for (int i = 0; i < gameScreen.getRows().get(3).size() - 1; i++) {
                if (currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW4, index);
            //Start isClick timer
            startClickTimer();
            return true;
        }

        if (!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW5).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW5);
            int index = gameScreen.getRows().get(4).size() - 1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for (int i = 0; i < gameScreen.getRows().get(4).size() - 1; i++) {
                if (currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW5, index);
            //Start isClick timer
            startClickTimer();
            return true;
        }

        if (!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW6).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW6);
            int index = gameScreen.getRows().get(5).size() - 1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for (int i = 0; i < gameScreen.getRows().get(5).size() - 1; i++) {
                if (currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW6, index);
            //Start isClick timer
            startClickTimer();
            return true;
        }

        if (!gameScreen.getDragging() && gameScreen.getBounds(CardPosition.ROW7).contains(vec.x, vec.y)) {
            gameScreen.setFailedDragPos(CardPosition.ROW7);
            int index = gameScreen.getRows().get(6).size() - 1;
            Rectangle currentRec = new Rectangle(0f, 505f, 1280f, 30f);
            for (int i = 0; i < gameScreen.getRows().get(6).size() - 1; i++) {
                if (currentRec.contains(vec.x, vec.y)) {
                    index = i;
                }
                currentRec.setPosition(0f, currentRec.getY() - 30f);
            }
            gameScreen.startDragRow(CardPosition.ROW7, index);
            //Start isClick timer
            startClickTimer();
            return true;
        }

        return false;
    }

    private void startClickTimer() {
        //Start isClick timer
        isClick = true;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isClick = false;
            }
        }, 0.2f);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT || pointer > 0) {
            return false;
        }

        Vector3 vec = new Vector3(screenX, screenY, 0);

        vec = gameScreen.getGame().getCamera().unproject(vec, gameScreen.getGame().getViewport().getScreenX(), gameScreen.getGame().getViewport().getScreenY(), gameScreen.getGame().getViewport().getScreenWidth(), gameScreen.getGame().getViewport().getScreenHeight());

        boolean isClick = this.isClick;
        if (isClick) {
            gameScreen.handleClick();
            this.isClick = false;
        }

        if (!gameScreen.getDragging()) {
            //Flip 3 cards from stock, or replenish stock
            if (gameScreen.getBounds(CardPosition.STOCK).contains(vec.x, vec.y)) {
                gameScreen.discard3();
            }
        } else {
            gameScreen.stopDragging(vec.x, vec.y, this.isClick);
            gameScreen.flipLastRowCards();
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer > 0) {
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
