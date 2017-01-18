package com.emduham.solitaire;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Evan on 1/10/2017.
 * Stores all information used to process and draw cards.
 */
class Card {
    private final Sprite front;
    private final Sprite back;
    private final int rank;
    private final Suit suit;
    private boolean faceUp;

    Card(Sprite front, Sprite back, Suit suit, int rank) {
        this.front = front;
        this.back = back;
        this.suit = suit;
        this.rank = rank;
        this.faceUp = false;
    }

    void toggleFaceUp() {
        faceUp = !faceUp;
    }

    void draw(Batch batch, float x, float y) {
        if (faceUp) {
            front.setPosition(x, y);
            front.draw(batch);
        } else {
            back.setPosition(x, y);
            back.draw(batch);
        }
    }

    Vector2 getPosition() {
        if (faceUp) {
            return new Vector2(front.getX(), front.getY());
        } else {
            return new Vector2(back.getX(), back.getY());
        }
    }

    Sprite getBack() {
        return back;
    }

    int getRank() {
        return rank;
    }

    Suit getSuit() {
        return suit;
    }

    boolean isFaceUp() {
        return faceUp;
    }
}
