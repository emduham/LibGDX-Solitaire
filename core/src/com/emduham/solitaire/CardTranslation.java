package com.emduham.solitaire;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 1/17/2017.
 * Temporally store card(s) and their destination
 * Move card(s) position by a rate towards the destination until it is 1 rate's distance from the destination
 * Have a boolean isFinished()
 * Pass card(s) back to GameScreen
 */
class CardTranslation {
    private static final float SPEED = 1280f;

    private List<Card> cards;
    private CardPosition destination;
    private Vector2 start;
    private Vector2 dest;
    private Vector2 loc;
    private Vector2 direction;
    private float distance;
    private boolean moving;

    CardTranslation(List<Card> cards, CardPosition destination, float destX, float destY) {
        this.cards = new ArrayList<Card>(cards);
        this.destination = destination;
        this.dest = new Vector2(destX, destY);

        this.loc = this.cards.get(0).getPosition();
        this.start = loc.cpy();

        distance = start.dst(dest);

        direction = new Vector2(dest.sub(loc)).nor();

        moving = true;
    }

    void update(float delta) {
        if (moving) {
            //loc.add(direction.x * SPEED * delta, direction.y * SPEED * delta);
            loc.add(direction.cpy().scl(SPEED).scl(delta));
            if (start.dst(loc) >= distance) {
                loc.set(dest.x, dest.y);
                moving = false;
            }
        }
    }

    void draw(Batch batch) {
        float tempY = loc.y;
        for (Card c : cards) {
            c.draw(batch, loc.x, tempY);
            tempY -= 30f;
        }
    }

    boolean isFinished() {
        return !moving;
    }

    List<Card> getCards() {
        return cards;
    }

    CardPosition getDestination() {
        return destination;
    }
}
