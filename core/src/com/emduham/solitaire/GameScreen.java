package com.emduham.solitaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Evan on 2017-01-08.
 */
public class GameScreen implements Screen {
    //game to setScreen(), batch, camera, etc...
    private SolitaireApp game;

    private TextureAtlas deckImgs;
    private ShapeRenderer shapeRenderer;

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
        shapeRenderer = new ShapeRenderer();
        Gdx.input.setInputProcessor(new InputHandler(this));
        deckImgs = new TextureAtlas("resizedcarddeck.pack"); //Cards are 115x159
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
        if(discard.size() == 1) {
            discard.get(discard.size() - 1).draw(game.getBatch(), 135f, 550f);
        } else if(discard.size() == 2) {
            discard.get(discard.size() - 2).draw(game.getBatch(), 135f, 550f);
            discard.get(discard.size() - 1).draw(game.getBatch(), 160f, 550f);
        }  else if(discard.size() > 2) {
            discard.get(discard.size() - 3).draw(game.getBatch(), 135f, 550f);
            discard.get(discard.size() - 2).draw(game.getBatch(), 160f, 550f);
            discard.get(discard.size() - 1).draw(game.getBatch(), 185f, 550f);
        }
        //Draw Final Piles
        if(!spades.isEmpty()) {
            spades.get(spades.size() - 1).draw(game.getBatch(), 700f, 550f);
        } else {
            Sprite fadedAce = deckImgs.createSprite("spades", 1);
            fadedAce.setAlpha(0.2f);
            fadedAce.setPosition(700f, 550f);
            fadedAce.draw(game.getBatch());
        }
        if(!clubs.isEmpty()) {
            clubs.get(clubs.size() - 1).draw(game.getBatch(), 850f, 550f);
        } else {
            Sprite fadedAce = deckImgs.createSprite("clubs", 1);
            fadedAce.setAlpha(0.2f);
            fadedAce.setPosition(850f, 550f);
            fadedAce.draw(game.getBatch());
        }
        if(!hearts.isEmpty()) {
            hearts.get(hearts.size() - 1).draw(game.getBatch(), 1000f, 550f);
        } else {
            Sprite fadedAce = deckImgs.createSprite("hearts", 1);
            fadedAce.setAlpha(0.2f);
            fadedAce.setPosition(1000f, 550f);
            fadedAce.draw(game.getBatch());
        }
        if(!diamonds.isEmpty()) {
            diamonds.get(diamonds.size() - 1).draw(game.getBatch(), 1150f, 550f);
        } else {
            Sprite fadedAce = deckImgs.createSprite("diamonds", 1);
            fadedAce.setAlpha(0.2f);
            fadedAce.setPosition(1150f, 550f);
            fadedAce.draw(game.getBatch());
        }
        //Draw Rows
        float xPos = 100f;
        for(int i = 0; i < rows.length; i++) {
            float yPos = 375f;
            for(Card c : rows[i]) {
                c.draw(game.getBatch(), xPos, yPos);
                yPos -= 30f;
            }
            xPos += 125f;
        }
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
        shapeRenderer.dispose();
    }

    private Card takeTopCard() {
        return stock.remove(stock.size()-1);
    }

    //TODO Fine tune bounding boxes
    public Rectangle getBounds(CardPosition cardPos) {
        float cardWidth = 115f;
        float cardHeight = 160f;

        Rectangle rec = null;
        switch(cardPos) {
            case SPADES:
                rec = new Rectangle(700f, 550f, cardWidth, cardHeight);
                break;
            case CLUBS:
                rec = new Rectangle(850f, 550f, cardWidth, cardHeight);
                break;
            case HEARTS:
                rec = new Rectangle(900f, 550f, cardWidth, cardHeight);
                break;
            case DIAMONDS:
                rec = new Rectangle(1150f, 550f, cardWidth, cardHeight);
                break;
            case STOCK:
                rec = new Rectangle(10f, 550f, cardWidth, cardHeight);
                break;
            case DISCARD:
                if(discard.size() == 1) {
                    rec = new Rectangle(135f, 550f, cardWidth, cardHeight);
                } else if(discard.size() == 2) {
                    rec = new Rectangle(160f, 550f, cardWidth, cardHeight);
                }  else if(discard.size() > 2) {
                    rec = new Rectangle(185f, 550f, cardWidth, cardHeight);
                }
                break;
            case ROW1:
                break;
            case ROW2:
                break;
            case ROW3:
                break;
            case ROW4:
                break;
            case ROW5:
                break;
            case ROW6:
                break;
            case ROW7:
                break;
        }
        return rec;
    }

    public void discard3() {
        if(stock.size() >= 3) {
            for (int i = 0; i < 3; i++) {
                Card tempCard = takeTopCard();
                tempCard.toggleFaceUp();
                discard.add(tempCard);
            }
        } else if(stock.size() > 0) {
            int toFlip = stock.size();
            for (int i = 0; i < toFlip; i++) {
                Card tempCard = takeTopCard();
                tempCard.toggleFaceUp();
                discard.add(tempCard);
            }
        } else {
            int toMove = discard.size();
            for (int i = 0; i < toMove; i++) {
                stock.add(0, discard.remove(0));
                stock.get(0).toggleFaceUp();
            }
        }
    }

    public SolitaireApp getGame() {
        return game;
    }
}
