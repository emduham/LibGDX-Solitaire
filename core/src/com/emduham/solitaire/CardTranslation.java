package com.emduham.solitaire;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Created by Evan on 1/17/2017.
 * Temporally store card(s) and their destination
 * Move card(s) position by a rate towards the destination until it is 1 rate's distance from the destination
 * Have a boolean isFinished()
 * Pass card(s) back to GameScreen
 */
class CardTranslation {
    private static final float RATE = 30f;

    private List<Card> cards;
    private Vector2 dest;
    private Vector2 loc;
    private Vector2 speed;

    CardTranslation(List<Card> cards, float destX, float destY) {
        this.cards = cards;
        this.dest = new Vector2(destX, destY);

        this.loc = this.cards.get(0).getPosition();

        float theta = dest.angleRad(loc);

        speed = new Vector2(RATE * MathUtils.sin(theta), RATE * MathUtils.cos(theta));
    }

    boolean isFinished() {
        if (loc.dst(dest) < RATE) {
            return true;
        } else {
            return false;
        }
    }
}
