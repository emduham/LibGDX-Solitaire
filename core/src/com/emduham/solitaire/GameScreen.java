package com.emduham.solitaire;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Evan on 2017-01-08.
 */
public class GameScreen implements Screen {
    //game to setScreen(), batch, camera, etc...
    private SolitaireApp game;

    private TextureAtlas deckImgs;

    //Stock Positions
    private ArrayList<Card> stock;
    private ArrayList<Card> discard;

    //Final Positions
    private ArrayList<Card> spades;
    private ArrayList<Card> clubs;
    private ArrayList<Card> diamonds;
    private ArrayList<Card> hearts;

    //Row Positions
    private ArrayList<Card>[] rows;


    public GameScreen(SolitaireApp game) {
        this.game = game;

        //Stock Positions
        stock = new ArrayList<Card>(52);
        discard = new ArrayList<Card>();

        //Final Positions
        spades = new ArrayList<Card>();
        clubs = new ArrayList<Card>();
        diamonds = new ArrayList<Card>();
        hearts = new ArrayList<Card>();

        //Row Positions
        rows = new ArrayList[7];
        for(int i = 0; i < rows.length; i++) {
            rows[i] = new ArrayList<Card>();
        }
    }

    private void initCards() {
        //Create 52 card deck in stock
        for(int i = 1; i <= 13; i++) {
            stock.add(new Card(deckImgs.createSprite("spades", i), deckImgs.createSprite("back", 5), Suit.SPADES, i, false));
        }
        for(int i = 1; i <= 13; i++) {
            stock.add(new Card(deckImgs.createSprite("clubs", i), deckImgs.createSprite("back", 5), Suit.CLUBS, i, false));
        }
        for(int i = 1; i <= 13; i++) {
            stock.add(new Card(deckImgs.createSprite("diamonds", i), deckImgs.createSprite("back", 5), Suit.DIAMONDS, i, false));
        }
        for(int i = 1; i <= 13; i++) {
            stock.add(new Card(deckImgs.createSprite("hearts", i), deckImgs.createSprite("back", 5), Suit.HEARTS, i, false));
        }

        //Shuffle the stock
        Collections.shuffle(stock);

        //Init rows
        for(int i = 0; i < rows.length; i++) {
            rows[i].add(takeTopCard());
            rows[i].get(rows[i].size() - 1).toggleFaceUp();
            for(int x = i + 1; x < rows.length; x++) {
                rows[x].add(takeTopCard());
            }
        }
    }

    @Override
    public void show() {
        deckImgs = new TextureAtlas("resizedcarddeck.pack");
        initCards();
    }

    @Override
    public void render(float delta) {
        game.getBatch().setProjectionMatrix(game.getCamera().combined);
        game.getBatch().begin();
        //Draw Stock
        if(!stock.isEmpty()) {
            stock.get(0).getBack().setPosition(10f, 550f);
            stock.get(0).getBack().draw(game.getBatch());
        }
        //Draw Discard
        if(!discard.isEmpty()) {
            discard.get(discard.size() - 1).draw(game.getBatch(), 135f, 550f);
        }
        //Draw Final Piles
        //Draw Rows
        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {

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
        deckImgs.dispose();
    }

    private Card takeTopCard() {
        return stock.remove(stock.size()-1);
    }
}
