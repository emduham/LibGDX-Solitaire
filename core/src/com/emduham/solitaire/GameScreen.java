package com.emduham.solitaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Scaling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Evan on 2017-01-08.
 */
public class GameScreen implements Screen {
    private static final int BACK = 5;

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

    private ArrayList<Card> cardBuffer;
    private Vector2 cardBufferLocation;
    private boolean dragging;
    private CardPosition failedDragPos;

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

        cardBuffer = new ArrayList<Card>();
        cardBufferLocation = new Vector2(0f, 0f);
        dragging = false;
    }

    private void initCards() {
        //Create 52 card deck in stock
        for(int i = 1; i <= 13; i++) {
            stock.add(new Card(deckImgs.createSprite("spades", i), deckImgs.createSprite("back", BACK), Suit.SPADES, i, false));
        }
        for(int i = 1; i <= 13; i++) {
            stock.add(new Card(deckImgs.createSprite("clubs", i), deckImgs.createSprite("back", BACK), Suit.CLUBS, i, false));
        }
        for(int i = 1; i <= 13; i++) {
            stock.add(new Card(deckImgs.createSprite("diamonds", i), deckImgs.createSprite("back", BACK), Suit.DIAMONDS, i, false));
        }
        for(int i = 1; i <= 13; i++) {
            stock.add(new Card(deckImgs.createSprite("hearts", i), deckImgs.createSprite("back", BACK), Suit.HEARTS, i, false));
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
        } else {
            Sprite fadedBack = deckImgs.createSprite("back", BACK);
            fadedBack.setPosition(10f, 550f);
            fadedBack.setAlpha(0.4f);
            fadedBack.draw(game.getBatch());
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
            if(!(rows[i].isEmpty())) {
                for (Card c : rows[i]) {
                    c.draw(game.getBatch(), xPos, yPos);
                    yPos -= 30f;
                }
            } else {
                Sprite fadedBackRow = deckImgs.createSprite("back", BACK);
                fadedBackRow.setAlpha(0.5f);
                fadedBackRow.setPosition(xPos, yPos);
                fadedBackRow.draw(game.getBatch());
            }
            xPos += 125f;
        }
        //Draw card buffer
        if(dragging && !(cardBuffer.isEmpty())) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            mousePos = game.getCamera().unproject(mousePos, game.getViewport().getScreenX(), game.getViewport().getScreenY(), game.getViewport().getScreenWidth(), game.getViewport().getScreenHeight());
            xPos = mousePos.x - (115f / 2f);
            float yPos = mousePos.y - 159f;
            for(Card c : cardBuffer) {
                c.draw(game.getBatch(), xPos, yPos);
                yPos -= 30f;
            }
        }
        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        game.getViewport().update(width, height);
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
                rec = new Rectangle(1000f, 550f, cardWidth, cardHeight);
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
                } else {
                    rec = new Rectangle(0f, 0f, 0f, 0f);
                }
                break;
            //height = ((rows[i].size() - 1) * 30) + 159f
            //y = 185 - ((rows[i].size() - 1) * 30) + 159f + 30f
            case ROW1:
                rec = new Rectangle(100f, 185 - ((rows[0].size() - 1) * 30) + 159f + 30f, cardWidth, ((rows[0].size() - 1) * 30) + 159f);
                break;
            case ROW2:
                rec = new Rectangle(225f, 185 - ((rows[1].size() - 1) * 30) + 159f + 30f, cardWidth, ((rows[1].size() - 1) * 30) + 159f);
                break;
            case ROW3:
                rec = new Rectangle(350f, 185 - ((rows[2].size() - 1) * 30) + 159f + 30f, cardWidth, ((rows[2].size() - 1) * 30) + 159f);
                break;
            case ROW4:
                rec = new Rectangle(475f, 185 - ((rows[3].size() - 1) * 30) + 159f + 30f, cardWidth, ((rows[3].size() - 1) * 30) + 159f);
                break;
            case ROW5:
                rec = new Rectangle(600f, 185 - ((rows[4].size() - 1) * 30) + 159f + 30f, cardWidth, ((rows[4].size() - 1) * 30) + 159f);
                break;
            case ROW6:
                rec = new Rectangle(725f, 185 - ((rows[5].size() - 1) * 30) + 159f + 30f, cardWidth, ((rows[5].size() - 1) * 30) + 159f);
                break;
            case ROW7:
                rec = new Rectangle(850f, 185 - ((rows[6].size() - 1) * 30) + 159f + 30f, cardWidth, ((rows[6].size() - 1) * 30) + 159f);
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

    private void replaceBufferAtOld() {
        switch (failedDragPos) {
            case DISCARD:
                discard.addAll(cardBuffer);
                break;
            case SPADES:
                spades.addAll(cardBuffer);
                break;
            case CLUBS:
                clubs.addAll(cardBuffer);
                break;
            case HEARTS:
                hearts.addAll(cardBuffer);
                break;
            case DIAMONDS:
                diamonds.addAll(cardBuffer);
                break;
            case ROW1:
                rows[0].addAll(cardBuffer);
                break;
            case ROW2:
                rows[1].addAll(cardBuffer);
                break;
            case ROW3:
                rows[2].addAll(cardBuffer);
                break;
            case ROW4:
                rows[3].addAll(cardBuffer);
                break;
            case ROW5:
                rows[4].addAll(cardBuffer);
                break;
            case ROW6:
                rows[5].addAll(cardBuffer);
                break;
            case ROW7:
                rows[6].addAll(cardBuffer);
                break;
            default:
                break;
        }
//        failedDragPos = null;
        cardBuffer.clear();
    }

    public SolitaireApp getGame() {
        return game;
    }

    public ArrayList<Card> getCardBuffer() {
        return cardBuffer;
    }

    public ArrayList<Card> getDiscard() {
        return discard;
    }

    public ArrayList<Card> getSpades() {
        return spades;
    }

    public ArrayList<Card> getClubs() {
        return clubs;
    }

    public ArrayList<Card> getDiamonds() {
        return diamonds;
    }

    public ArrayList<Card> getHearts() {
        return hearts;
    }

    public ArrayList<Card>[] getRows() {
        return rows;
    }

    public boolean getDragging() {
        return dragging;
    }

    public void setCardBufferLocation(float x, float y) {
        cardBufferLocation.set(x, y);
    }

    public void setFailedDragPos(CardPosition failedDragPos) {
        this.failedDragPos = failedDragPos;
    }

    public void startDragDiscard() {
        dragging = true;

        cardBuffer.add(discard.remove(discard.size() - 1));
    }

    public void stopDragging(float actualX, float actualY) {
        dragging = false;

        CardPosition droppedOn = null;

        if(getBounds(CardPosition.ROW1).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW1;
        } else if(getBounds(CardPosition.ROW2).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW2;
        } else if(getBounds(CardPosition.ROW3).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW3;
        } else if(getBounds(CardPosition.ROW4).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW4;
        } else if(getBounds(CardPosition.ROW5).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW5;
        } else if(getBounds(CardPosition.ROW6).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW6;
        } else if(getBounds(CardPosition.ROW7).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW7;
        } else if(getBounds(CardPosition.HEARTS).contains(actualX, actualY)) {
            droppedOn = CardPosition.HEARTS;
        } else if(getBounds(CardPosition.CLUBS).contains(actualX, actualY)) {
            droppedOn = CardPosition.CLUBS;
        } else if(getBounds(CardPosition.SPADES).contains(actualX, actualY)) {
            droppedOn = CardPosition.SPADES;
        } else if(getBounds(CardPosition.DIAMONDS).contains(actualX, actualY)) {
            droppedOn = CardPosition.DIAMONDS;
        }

        if(droppedOn != null) {
            switch (droppedOn) {
                case SPADES:
                    validFinalPile(CardPosition.SPADES);
                    break;
                case CLUBS:
                    validFinalPile(CardPosition.CLUBS);
                    break;
                case HEARTS:
                    validFinalPile(CardPosition.HEARTS);
                    break;
                case DIAMONDS:
                    validFinalPile(CardPosition.DIAMONDS);
                    break;
                case ROW1:
                    validRow(0);
                    break;
                case ROW2:
                    validRow(1);
                    break;
                case ROW3:
                    validRow(2);
                    break;
                case ROW4:
                    validRow(3);
                    break;
                case ROW5:
                    validRow(4);
                    break;
                case ROW6:
                    validRow(5);
                    break;
                case ROW7:
                    validRow(6);
                    break;
                default:
                    replaceBufferAtOld();
                    return;
            }
        } else {
            replaceBufferAtOld();
            return;
        }

        //If invalid or didn't drop on anything
        replaceBufferAtOld();
    }

    private boolean isValid(Card above, Card below) {
        if(above.getRank() == below.getRank() + 1) {
            switch(above.getSuit()) {
                case SPADES:
                case CLUBS:
                    if(below.getSuit() == Suit.DIAMONDS || below.getSuit() == Suit.HEARTS) {
                        return true;
                    }
                    break;
                case HEARTS:
                case DIAMONDS:
                    if(below.getSuit() == Suit.SPADES || below.getSuit() == Suit.CLUBS) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    private void validFinalPile(CardPosition pile) {
        if(cardBuffer.size() == 1) {
            if(pile == CardPosition.SPADES) {
                if (cardBuffer.get(0).getSuit() == Suit.SPADES) {
                    if (cardBuffer.get(0).getRank() == spades.size() + 1) {
                        spades.add(cardBuffer.get(0));
                        cardBuffer.clear();
                    } else {
                        replaceBufferAtOld();
                        return;
                    }
                } else {
                    replaceBufferAtOld();
                    return;
                }
            } else if(pile == CardPosition.CLUBS) {
                if (cardBuffer.get(0).getSuit() == Suit.CLUBS) {
                    if (cardBuffer.get(0).getRank() == clubs.size() + 1) {
                        clubs.add(cardBuffer.get(0));
                        cardBuffer.clear();
                    } else {
                        replaceBufferAtOld();
                        return;
                    }
                } else {
                    replaceBufferAtOld();
                    return;
                }
            } else if(pile == CardPosition.DIAMONDS) {
                if (cardBuffer.get(0).getSuit() == Suit.DIAMONDS) {
                    if (cardBuffer.get(0).getRank() == diamonds.size() + 1) {
                        diamonds.add(cardBuffer.get(0));
                        cardBuffer.clear();
                    } else {
                        replaceBufferAtOld();
                        return;
                    }
                } else {
                    replaceBufferAtOld();
                    return;
                }
            } else if(pile == CardPosition.HEARTS) {
                if (cardBuffer.get(0).getSuit() == Suit.HEARTS) {
                    if (cardBuffer.get(0).getRank() == hearts.size() + 1) {
                        hearts.add(cardBuffer.get(0));
                        cardBuffer.clear();
                    } else {
                        replaceBufferAtOld();
                        return;
                    }
                } else {
                    replaceBufferAtOld();
                    return;
                }
            }
        } else {
            replaceBufferAtOld();
        }
    }

    private void validRow(int index) {
        if(cardBuffer.size() > 1) {
            for (int i = 1; i < cardBuffer.size(); i++) {
                if (!isValid(cardBuffer.get(i - 1), cardBuffer.get(i))) {
                    replaceBufferAtOld();
                    return;
                }
            }
        }
        if(rows[index].isEmpty()) {
            if(cardBuffer.get(0).getRank() == 13) {
                rows[index].addAll(cardBuffer);
                cardBuffer.clear();
            } else {
                replaceBufferAtOld();
                return;
            }
        } else {
            if(isValid(rows[index].get(rows[index].size() - 1), cardBuffer.get(0))) {
                rows[index].addAll(cardBuffer);
                cardBuffer.clear();
            } else {
                replaceBufferAtOld();
                return;
            }
        }
    }

    public void startDragRow(CardPosition row, int index) {
        List<Card> tempList = null;

        switch(row) {
            case ROW1:
                if(rows[0].get(index).isFaceUp()) {
                    tempList = rows[0].subList(index, rows[0].size());
                    cardBuffer.addAll(tempList);
                    tempList.clear();
                } else {
                    return;
                }
                break;
            case ROW2:
                if(rows[1].get(index).isFaceUp()) {
                    tempList = rows[1].subList(index, rows[1].size());
                    cardBuffer.addAll(tempList);
                    tempList.clear();
                } else {
                    return;
                }
                break;
            case ROW3:
                if(rows[2].get(index).isFaceUp()) {
                    tempList = rows[2].subList(index, rows[2].size());
                    cardBuffer.addAll(tempList);
                    tempList.clear();
                } else {
                    return;
                }
                break;
            case ROW4:
                if(rows[3].get(index).isFaceUp()) {
                    tempList = rows[3].subList(index, rows[3].size());
                    cardBuffer.addAll(tempList);
                    tempList.clear();
                } else {
                    return;
                }
                break;
            case ROW5:
                if(rows[4].get(index).isFaceUp()) {
                    tempList = rows[4].subList(index, rows[4].size());
                    cardBuffer.addAll(tempList);
                    tempList.clear();
                } else {
                    return;
                }
                break;
            case ROW6:
                if(rows[5].get(index).isFaceUp()) {
                    tempList = rows[5].subList(index, rows[5].size());
                    cardBuffer.addAll(tempList);
                    tempList.clear();
                } else {
                    return;
                }
                break;
            case ROW7:
                if(rows[6].get(index).isFaceUp()) {
                    tempList = rows[6].subList(index, rows[6].size());
                    cardBuffer.addAll(tempList);
                    tempList.clear();
                } else {
                    return;
                }
                break;
        }
        dragging = true;
    }

    public void flipLastRowCards() {
        for(int i = 0; i < rows.length; i++) {
            if(!(rows[i].isEmpty())) {
                if (!(rows[i].get(rows[i].size() - 1).isFaceUp())) {
                    rows[i].get(rows[i].size() - 1).toggleFaceUp();
                }
            }
        }
    }

    public boolean isFinished() {
        if(hearts.size() == 13) {
            if(diamonds.size() == 13) {
                if(spades.size() == 13) {
                    if(clubs.size() == 13) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
