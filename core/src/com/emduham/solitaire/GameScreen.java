package com.emduham.solitaire;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Evan on 2017-01-08.
 * Most main game functions are located here.
 * Manages the table using rules.
 * Passes out input to InputHandler.
 */
class GameScreen implements Screen {
    //game to setScreen(), batch, camera, etc...
    private final SolitaireApp game;

    private TextureAtlas deckImgs;
    private ShapeRenderer shapeRenderer;

    //Stock Positions
    private final ArrayList<Card> stock;
    private final ArrayList<Card> discard;

    //Final Positions
    private final ArrayList<Card> spades;
    private final ArrayList<Card> clubs;
    private final ArrayList<Card> diamonds;
    private final ArrayList<Card> hearts;

    //Row Positions
    private final List<List<Card>> rows;

    private final ArrayList<Card> cardBuffer;
    private final Vector2 cardBufferLocation;
    private boolean dragging;
    private CardPosition failedDragPos;

    private long startMillis;

    GameScreen(SolitaireApp game) {
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
        rows = new ArrayList<List<Card>>();
        for (int i = 0; i < 7; i++) {
            rows.add(new ArrayList<Card>());
        }

        cardBuffer = new ArrayList<Card>();
        cardBufferLocation = new Vector2(0f, 0f);
        dragging = false;
    }

    private void initCards() {
        //Create 52 card deck in stock
        for (int i = 1; i <= 13; i++) {
            stock.add(new Card(deckImgs.createSprite("spades", i), deckImgs.createSprite("back", SolitaireApp.BACK), Suit.SPADES, i));
        }
        for (int i = 1; i <= 13; i++) {
            stock.add(new Card(deckImgs.createSprite("clubs", i), deckImgs.createSprite("back", SolitaireApp.BACK), Suit.CLUBS, i));
        }
        for (int i = 1; i <= 13; i++) {
            stock.add(new Card(deckImgs.createSprite("diamonds", i), deckImgs.createSprite("back", SolitaireApp.BACK), Suit.DIAMONDS, i));
        }
        for (int i = 1; i <= 13; i++) {
            stock.add(new Card(deckImgs.createSprite("hearts", i), deckImgs.createSprite("back", SolitaireApp.BACK), Suit.HEARTS, i));
        }

        //Shuffle the stock
        Collections.shuffle(stock);

        //Init rows
        for (int i = 0; i < rows.size(); i++) {
            rows.get(i).add(takeTopCard());
            rows.get(i).get(rows.get(i).size() - 1).toggleFaceUp();
            for (int x = i + 1; x < rows.size(); x++) {
                rows.get(x).add(takeTopCard());
            }
        }
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        Gdx.input.setInputProcessor(new InputHandler(this));
        deckImgs = new TextureAtlas("resizedcarddeck.pack"); //Cards are 115x159
        initCards();
        startMillis = TimeUtils.millis();
    }

    @Override
    public void render(float delta) {
        game.getBatch().setProjectionMatrix(game.getCamera().combined);
        game.getBatch().begin();
        //Draw Stock
        if (!stock.isEmpty()) {
            stock.get(0).getBack().setPosition(10f, 550f);
            stock.get(0).getBack().draw(game.getBatch());
        } else {
            Sprite fadedBack = deckImgs.createSprite("back", SolitaireApp.BACK);
            fadedBack.setPosition(10f, 550f);
            fadedBack.setAlpha(0.4f);
            fadedBack.draw(game.getBatch());
        }
        //Draw Discard
        if (discard.size() == 1) {
            discard.get(discard.size() - 1).draw(game.getBatch(), 135f, 550f);
        } else if (discard.size() == 2) {
            discard.get(discard.size() - 2).draw(game.getBatch(), 135f, 550f);
            discard.get(discard.size() - 1).draw(game.getBatch(), 160f, 550f);
        } else if (discard.size() > 2) {
            discard.get(discard.size() - 3).draw(game.getBatch(), 135f, 550f);
            discard.get(discard.size() - 2).draw(game.getBatch(), 160f, 550f);
            discard.get(discard.size() - 1).draw(game.getBatch(), 185f, 550f);
        }
        //Draw Final Piles
        if (!spades.isEmpty()) {
            spades.get(spades.size() - 1).draw(game.getBatch(), 700f, 550f);
        } else {
            Sprite fadedAce = deckImgs.createSprite("spades", 1);
            fadedAce.setAlpha(0.2f);
            fadedAce.setPosition(700f, 550f);
            fadedAce.draw(game.getBatch());
        }
        if (!clubs.isEmpty()) {
            clubs.get(clubs.size() - 1).draw(game.getBatch(), 850f, 550f);
        } else {
            Sprite fadedAce = deckImgs.createSprite("clubs", 1);
            fadedAce.setAlpha(0.2f);
            fadedAce.setPosition(850f, 550f);
            fadedAce.draw(game.getBatch());
        }
        if (!hearts.isEmpty()) {
            hearts.get(hearts.size() - 1).draw(game.getBatch(), 1000f, 550f);
        } else {
            Sprite fadedAce = deckImgs.createSprite("hearts", 1);
            fadedAce.setAlpha(0.2f);
            fadedAce.setPosition(1000f, 550f);
            fadedAce.draw(game.getBatch());
        }
        if (!diamonds.isEmpty()) {
            diamonds.get(diamonds.size() - 1).draw(game.getBatch(), 1150f, 550f);
        } else {
            Sprite fadedAce = deckImgs.createSprite("diamonds", 1);
            fadedAce.setAlpha(0.2f);
            fadedAce.setPosition(1150f, 550f);
            fadedAce.draw(game.getBatch());
        }
        //Draw Rows
        float xPos = 100f;
        for (List<Card> row : rows) {
            float yPos = 375f;
            if (!(row.isEmpty())) {
                for (Card c : row) {
                    c.draw(game.getBatch(), xPos, yPos);
                    yPos -= 30f;
                }
            } else {
                Sprite fadedBackRow = deckImgs.createSprite("back", SolitaireApp.BACK);
                fadedBackRow.setAlpha(0.5f);
                fadedBackRow.setPosition(xPos, yPos);
                fadedBackRow.draw(game.getBatch());
            }
            xPos += 125f;
        }
        //Draw card buffer
        if (dragging && !(cardBuffer.isEmpty())) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            mousePos = game.getCamera().unproject(mousePos, game.getViewport().getScreenX(), game.getViewport().getScreenY(), game.getViewport().getScreenWidth(), game.getViewport().getScreenHeight());
            xPos = mousePos.x - (115f / 2f);
            float yPos = mousePos.y - 159f;
            for (Card c : cardBuffer) {
                c.draw(game.getBatch(), xPos, yPos);
                yPos -= 30f;
            }
        }
        //Draw delta time in seconds
        float deltaTime = TimeUtils.timeSinceMillis(startMillis) / 1000f;
        game.getFont16().draw(game.getBatch(), "Time: " + deltaTime, 10f, 20f);
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
        return stock.remove(stock.size() - 1);
    }

    Rectangle getBounds(CardPosition cardPos) {
        float cardWidth = 115f;
        float cardHeight = 160f;

        Rectangle rec = null;
        switch (cardPos) {
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
                if (discard.size() == 1) {
                    rec = new Rectangle(135f, 550f, cardWidth, cardHeight);
                } else if (discard.size() == 2) {
                    rec = new Rectangle(160f, 550f, cardWidth, cardHeight);
                } else if (discard.size() > 2) {
                    rec = new Rectangle(185f, 550f, cardWidth, cardHeight);
                } else {
                    rec = new Rectangle(0f, 0f, 0f, 0f);
                }
                break;
            //height = ((rows[i].size() - 1) * 30) + 159f
            //y = 185 - ((rows[i].size() - 1) * 30) + 159f + 30f
            case ROW1:
                rec = new Rectangle(100f, 185 - ((rows.get(0).size() - 1) * 30) + 159f + 30f, cardWidth, ((rows.get(0).size() - 1) * 30) + 159f);
                break;
            case ROW2:
                rec = new Rectangle(225f, 185 - ((rows.get(1).size() - 1) * 30) + 159f + 30f, cardWidth, ((rows.get(1).size() - 1) * 30) + 159f);
                break;
            case ROW3:
                rec = new Rectangle(350f, 185 - ((rows.get(2).size() - 1) * 30) + 159f + 30f, cardWidth, ((rows.get(2).size() - 1) * 30) + 159f);
                break;
            case ROW4:
                rec = new Rectangle(475f, 185 - ((rows.get(3).size() - 1) * 30) + 159f + 30f, cardWidth, ((rows.get(3).size() - 1) * 30) + 159f);
                break;
            case ROW5:
                rec = new Rectangle(600f, 185 - ((rows.get(4).size() - 1) * 30) + 159f + 30f, cardWidth, ((rows.get(4).size() - 1) * 30) + 159f);
                break;
            case ROW6:
                rec = new Rectangle(725f, 185 - ((rows.get(5).size() - 1) * 30) + 159f + 30f, cardWidth, ((rows.get(5).size() - 1) * 30) + 159f);
                break;
            case ROW7:
                rec = new Rectangle(850f, 185 - ((rows.get(6).size() - 1) * 30) + 159f + 30f, cardWidth, ((rows.get(6).size() - 1) * 30) + 159f);
                break;
        }
        return rec;
    }

    void discard3() {
        if (stock.size() >= 3) {
            for (int i = 0; i < 3; i++) {
                Card tempCard = takeTopCard();
                tempCard.toggleFaceUp();
                discard.add(tempCard);
            }
        } else if (stock.size() > 0) {
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
                rows.get(0).addAll(cardBuffer);
                break;
            case ROW2:
                rows.get(1).addAll(cardBuffer);
                break;
            case ROW3:
                rows.get(2).addAll(cardBuffer);
                break;
            case ROW4:
                rows.get(3).addAll(cardBuffer);
                break;
            case ROW5:
                rows.get(4).addAll(cardBuffer);
                break;
            case ROW6:
                rows.get(5).addAll(cardBuffer);
                break;
            case ROW7:
                rows.get(6).addAll(cardBuffer);
                break;
            default:
                break;
        }
//        failedDragPos = null;
        cardBuffer.clear();
    }

    SolitaireApp getGame() {
        return game;
    }

    List<List<Card>> getRows() {
        return rows;
    }

    boolean getDragging() {
        return dragging;
    }

    void setCardBufferLocation(float x, float y) {
        cardBufferLocation.set(x, y);
    }

    void setFailedDragPos(CardPosition failedDragPos) {
        this.failedDragPos = failedDragPos;
    }

    void startDragDiscard() {
        dragging = true;

        cardBuffer.add(discard.remove(discard.size() - 1));
    }

    void stopDragging(float actualX, float actualY, boolean isClick) {
        dragging = false;

        CardPosition droppedOn = null;

        if (getBounds(CardPosition.ROW1).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW1;
        } else if (getBounds(CardPosition.ROW2).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW2;
        } else if (getBounds(CardPosition.ROW3).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW3;
        } else if (getBounds(CardPosition.ROW4).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW4;
        } else if (getBounds(CardPosition.ROW5).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW5;
        } else if (getBounds(CardPosition.ROW6).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW6;
        } else if (getBounds(CardPosition.ROW7).contains(actualX, actualY)) {
            droppedOn = CardPosition.ROW7;
        } else if (getBounds(CardPosition.HEARTS).contains(actualX, actualY)) {
            droppedOn = CardPosition.HEARTS;
        } else if (getBounds(CardPosition.CLUBS).contains(actualX, actualY)) {
            droppedOn = CardPosition.CLUBS;
        } else if (getBounds(CardPosition.SPADES).contains(actualX, actualY)) {
            droppedOn = CardPosition.SPADES;
        } else if (getBounds(CardPosition.DIAMONDS).contains(actualX, actualY)) {
            droppedOn = CardPosition.DIAMONDS;
        }

        if (droppedOn != null && !isClick) {
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
        if (above.getRank() == below.getRank() + 1) {
            switch (above.getSuit()) {
                case SPADES:
                case CLUBS:
                    if (below.getSuit() == Suit.DIAMONDS || below.getSuit() == Suit.HEARTS) {
                        return true;
                    }
                    break;
                case HEARTS:
                case DIAMONDS:
                    if (below.getSuit() == Suit.SPADES || below.getSuit() == Suit.CLUBS) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    private void validFinalPile(CardPosition pile) {
        if (cardBuffer.size() == 1) {
            if (pile == CardPosition.SPADES) {
                if (cardBuffer.get(0).getSuit() == Suit.SPADES) {
                    if (cardBuffer.get(0).getRank() == spades.size() + 1) {
                        spades.add(cardBuffer.get(0));
                        cardBuffer.clear();
                    } else {
                        replaceBufferAtOld();
//                        return;
                    }
                } else {
                    replaceBufferAtOld();
//                    return;
                }
            } else if (pile == CardPosition.CLUBS) {
                if (cardBuffer.get(0).getSuit() == Suit.CLUBS) {
                    if (cardBuffer.get(0).getRank() == clubs.size() + 1) {
                        clubs.add(cardBuffer.get(0));
                        cardBuffer.clear();
                    } else {
                        replaceBufferAtOld();
//                        return;
                    }
                } else {
                    replaceBufferAtOld();
//                    return;
                }
            } else if (pile == CardPosition.DIAMONDS) {
                if (cardBuffer.get(0).getSuit() == Suit.DIAMONDS) {
                    if (cardBuffer.get(0).getRank() == diamonds.size() + 1) {
                        diamonds.add(cardBuffer.get(0));
                        cardBuffer.clear();
                    } else {
                        replaceBufferAtOld();
//                        return;
                    }
                } else {
                    replaceBufferAtOld();
//                    return;
                }
            } else if (pile == CardPosition.HEARTS) {
                if (cardBuffer.get(0).getSuit() == Suit.HEARTS) {
                    if (cardBuffer.get(0).getRank() == hearts.size() + 1) {
                        hearts.add(cardBuffer.get(0));
                        cardBuffer.clear();
                    } else {
                        replaceBufferAtOld();
//                        return;
                    }
                } else {
                    replaceBufferAtOld();
//                    return;
                }
            }
        } else {
            replaceBufferAtOld();
        }
    }

    private void validRow(int index) {
        if (cardBuffer.size() > 1) {
            for (int i = 1; i < cardBuffer.size(); i++) {
                if (!isValid(cardBuffer.get(i - 1), cardBuffer.get(i))) {
                    replaceBufferAtOld();
                    return;
                }
            }
        }
        if (rows.get(index).isEmpty()) {
            if (cardBuffer.get(0).getRank() == 13) {
                rows.get(index).addAll(cardBuffer);
                cardBuffer.clear();
            } else {
                replaceBufferAtOld();
//                return;
            }
        } else {
            if (isValid(rows.get(index).get(rows.get(index).size() - 1), cardBuffer.get(0))) {
                rows.get(index).addAll(cardBuffer);
                cardBuffer.clear();
            } else {
                replaceBufferAtOld();
//                return;
            }
        }
    }

    void startDragRow(CardPosition row, int index) {
        List<Card> tempList;

        switch (row) {
            case ROW1:
                if (!(rows.get(0).isEmpty())) {
                    if (rows.get(0).get(index).isFaceUp()) {
                        tempList = rows.get(0).subList(index, rows.get(0).size());
                        cardBuffer.addAll(tempList);
                        tempList.clear();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                break;
            case ROW2:
                if (!(rows.get(1).isEmpty())) {
                    if (rows.get(1).get(index).isFaceUp()) {
                        tempList = rows.get(1).subList(index, rows.get(1).size());
                        cardBuffer.addAll(tempList);
                        tempList.clear();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                break;
            case ROW3:
                if (!(rows.get(2).isEmpty())) {
                    if (rows.get(2).get(index).isFaceUp()) {
                        tempList = rows.get(2).subList(index, rows.get(2).size());
                        cardBuffer.addAll(tempList);
                        tempList.clear();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                break;
            case ROW4:
                if (!(rows.get(3).isEmpty())) {
                    if (rows.get(3).get(index).isFaceUp()) {
                        tempList = rows.get(3).subList(index, rows.get(3).size());
                        cardBuffer.addAll(tempList);
                        tempList.clear();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                break;
            case ROW5:
                if (!(rows.get(4).isEmpty())) {
                    if (rows.get(4).get(index).isFaceUp()) {
                        tempList = rows.get(4).subList(index, rows.get(4).size());
                        cardBuffer.addAll(tempList);
                        tempList.clear();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                break;
            case ROW6:
                if (!(rows.get(5).isEmpty())) {
                    if (rows.get(5).get(index).isFaceUp()) {
                        tempList = rows.get(5).subList(index, rows.get(5).size());
                        cardBuffer.addAll(tempList);
                        tempList.clear();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                break;
            case ROW7:
                if (!(rows.get(6).isEmpty())) {
                    if (rows.get(6).get(index).isFaceUp()) {
                        tempList = rows.get(6).subList(index, rows.get(6).size());
                        cardBuffer.addAll(tempList);
                        tempList.clear();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
                break;
        }
        dragging = true;
    }

    void flipLastRowCards() {
        for (List<Card> row : rows) {
            if (!(row.isEmpty())) {
                if (!(row.get(row.size() - 1).isFaceUp())) {
                    row.get(row.size() - 1).toggleFaceUp();
                }
            }
        }
    }

    boolean isFinished() {
        if (hearts.size() == 13) {
            if (diamonds.size() == 13) {
                if (spades.size() == 13) {
                    if (clubs.size() == 13) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    void handleClick() {
        if (!(cardBuffer.isEmpty())) {

            int index = -1;

            if (cardBuffer.size() == 1) {
                switch (cardBuffer.get(0).getSuit()) {
                    case SPADES:
                        if (cardBuffer.get(0).getRank() == spades.size() + 1) {
                            spades.add(cardBuffer.remove(0));
                            return;
                        }
                        break;
                    case HEARTS:
                        if (cardBuffer.get(0).getRank() == hearts.size() + 1) {
                            hearts.add(cardBuffer.remove(0));
                            return;
                        }
                        break;
                    case DIAMONDS:
                        if (cardBuffer.get(0).getRank() == diamonds.size() + 1) {
                            diamonds.add(cardBuffer.remove(0));
                            return;
                        }
                        break;
                    case CLUBS:
                        if (cardBuffer.get(0).getRank() == clubs.size() + 1) {
                            clubs.add(cardBuffer.remove(0));
                            return;
                        }
                        break;
                }
            }

            switch (failedDragPos) {
                case ROW1:
                    for (int i = 0; i < rows.size(); i++) {
                        if (i != 0) {
                            if (!(rows.get(i).isEmpty())) {
                                if (isValid(rows.get(i).get(rows.get(i).size() - 1), cardBuffer.get(0))) {
                                    index = i;
                                }
                            } else {
                                if (cardBuffer.get(0).getRank() == 13) {
                                    index = i;
                                }
                            }
                        }
                    }
                    break;
                case ROW2:
                    for (int i = 0; i < rows.size(); i++) {
                        if (i != 1) {
                            if (!(rows.get(i).isEmpty())) {
                                if (isValid(rows.get(i).get(rows.get(i).size() - 1), cardBuffer.get(0))) {
                                    index = i;
                                }
                            } else {
                                if (cardBuffer.get(0).getRank() == 13) {
                                    index = i;
                                }
                            }
                        }
                    }
                    break;
                case ROW3:
                    for (int i = 0; i < rows.size(); i++) {
                        if (i != 2) {
                            if (!(rows.get(i).isEmpty())) {
                                if (isValid(rows.get(i).get(rows.get(i).size() - 1), cardBuffer.get(0))) {
                                    index = i;
                                }
                            } else {
                                if (cardBuffer.get(0).getRank() == 13) {
                                    index = i;
                                }
                            }
                        }
                    }
                    break;
                case ROW4:
                    for (int i = 0; i < rows.size(); i++) {
                        if (i != 3) {
                            if (!(rows.get(i).isEmpty())) {
                                if (isValid(rows.get(i).get(rows.get(i).size() - 1), cardBuffer.get(0))) {
                                    index = i;
                                }
                            } else {
                                if (cardBuffer.get(0).getRank() == 13) {
                                    index = i;
                                }
                            }
                        }
                    }
                    break;
                case ROW5:
                    for (int i = 0; i < rows.size(); i++) {
                        if (i != 4) {
                            if (!(rows.get(i).isEmpty())) {
                                if (isValid(rows.get(i).get(rows.get(i).size() - 1), cardBuffer.get(0))) {
                                    index = i;
                                }
                            } else {
                                if (cardBuffer.get(0).getRank() == 13) {
                                    index = i;
                                }
                            }
                        }
                    }
                    break;
                case ROW6:
                    for (int i = 0; i < rows.size(); i++) {
                        if (i != 5) {
                            if (!(rows.get(i).isEmpty())) {
                                if (isValid(rows.get(i).get(rows.get(i).size() - 1), cardBuffer.get(0))) {
                                    index = i;
                                }
                            } else {
                                if (cardBuffer.get(0).getRank() == 13) {
                                    index = i;
                                }
                            }
                        }
                    }
                    break;
                case ROW7:
                    for (int i = 0; i < rows.size(); i++) {
                        if (i != 6) {
                            if (!(rows.get(i).isEmpty())) {
                                if (isValid(rows.get(i).get(rows.get(i).size() - 1), cardBuffer.get(0))) {
                                    index = i;
                                }
                            } else {
                                if (cardBuffer.get(0).getRank() == 13) {
                                    index = i;
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }

            if (index == -1) {
                return;
            }

            rows.get(index).addAll(cardBuffer);
            cardBuffer.clear();
        }
    }
}
