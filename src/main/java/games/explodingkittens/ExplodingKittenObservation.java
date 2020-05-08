package games.explodingkittens;

import components.Deck;
import components.IDeck;
import observations.IPrintable;
import components.IPartialObservableDeck;
import components.PartialObservableDeck;
import games.explodingkittens.cards.ExplodingKittenCard;
import observations.Observation;

import java.util.List;

public class ExplodingKittenObservation implements IPrintable, Observation {

    private List<PartialObservableDeck<ExplodingKittenCard>> playerHandCards;
    private PartialObservableDeck<ExplodingKittenCard> drawPile;
    private Deck<ExplodingKittenCard> discardPile;
    private int currentPlayer;

    public ExplodingKittenObservation(List<PartialObservableDeck<ExplodingKittenCard>> playerDecks,
                                      PartialObservableDeck<ExplodingKittenCard> drawPile,
                                      Deck<ExplodingKittenCard> discardPile,
                                      int currentPlayer){
        playerHandCards = playerDecks;
        this.drawPile = drawPile;
        this.discardPile = discardPile;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void PrintToConsole() {
        for (int i = 0; i < playerHandCards.size(); i++){
            if (currentPlayer == i)
                System.out.print(">>> Player " + i + ":");
            else
                System.out.print("Player " + i + ":");
            printDeck(playerHandCards.get(i));
        }

        System.out.print("DrawPile" + ":");
        printDeck(drawPile);

        System.out.print("DiscardPile" + ":");
        printDeck(discardPile);
    }


    public void printDeck(IPartialObservableDeck<ExplodingKittenCard> deck){
        StringBuilder sb = new StringBuilder();
        for (ExplodingKittenCard card : deck.getVisibleCards(currentPlayer)){
            if (card == null)
                sb.append("UNKNOWN");
            else
                sb.append(card.cardType.toString());
            sb.append(",");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length()-1);
        System.out.println(sb.toString());
    }


    public void printDeck(IDeck<ExplodingKittenCard> deck){
        StringBuilder sb = new StringBuilder();
        for (ExplodingKittenCard card : deck.getCards()){
            sb.append(card.cardType.toString());
            sb.append(",");
        }
        if (sb.length() > 0) sb.deleteCharAt(sb.length()-1);
        System.out.println(sb.toString());
    }


}