package com.emduham.solitaire;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Evan on 1/10/2017.
 */
public class Card {
    private Sprite front;
    private Sprite back;
    private int rank;
    private Suit suit;
    private boolean faceUp;

    public Card(Sprite front, Sprite back, Suit suit, int rank, boolean faceUp) {
        this.front = front;
        this.back = back;
        this.suit = suit;
        this.rank = rank;
        this.faceUp = faceUp;
    }

    public void toggleFaceUp() {
        faceUp = !faceUp;
    }

    public void draw(SpriteBatch batch, float x, float y) {
        if(faceUp) {
            front.setPosition(x, y);
            front.draw(batch);
        } else {
            back.setPosition(x, y);
            back.draw(batch);
        }
    }

    public Sprite getFront() {
        return front;
    }

    public Sprite getBack() {
        return back;
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }
}
