package com.emduham.solitaire;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Evan on 1/10/2017.
 * Stores all information used to process and draw cards.
 */
class Card {
    private Sprite front;
    private Sprite back;
    private int rank;
    private Suit suit;
    private boolean faceUp;

    Card(Sprite front, Sprite back, Suit suit, int rank, boolean faceUp) {
        this.front = front;
        this.back = back;
        this.suit = suit;
        this.rank = rank;
        this.faceUp = faceUp;
    }

    void toggleFaceUp() {
        faceUp = !faceUp;
    }

    void draw(SpriteBatch batch, float x, float y) {
        if(faceUp) {
            front.setPosition(x, y);
            front.draw(batch);
        } else {
            back.setPosition(x, y);
            back.draw(batch);
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
