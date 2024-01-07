import java.util.ArrayList;
import java.util.Collections;

/**
 * underlying game API
 */
public class Game {
    // value to signal that the player wants to draw a card rather than play one
    public static final int DRAW = -1;

    public ArrayList<Card> deck;
    public ArrayList<Card> played;
    public Player[] players;
    public int turn;
    public int direction = 1;

    /**
     * constructor
     * 
     * @param numPlayers the number of players to start with
     */
    public Game(int numPlayers) {
        // initializing the arrays
        players = new Player[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            players[i] = new Player();
        }

        deck = Card.makeNewDeck();
        played = new ArrayList<Card>();
    }

    /**
     * swaps the played cards and the deck, then shuffles the deck
     */
    public void reshuffleDeck() {
        Card t = top();
        deck = played;
        deck.remove(deck.size() - 1);
        Collections.shuffle(deck);
        played = new ArrayList<Card>();
        played.add(t);
    }

    /**
     * deals cards to the players
     * 
     * @param numCards number of cards to deal
     */
    public void deal(int numCards) {
        for (int i = 0; i < numCards; i++) {
            for (Player p : players) {
                p.hand.add(deck.get(deck.size() - 1));
                deck.remove(deck.size() - 1);
            }
        }
        played.add(deck.get(deck.size() - 1));
        deck.remove(deck.size() - 1);
    }

    /**
     * gets the most recently played card
     * 
     * @return the most recently played card
     */
    public Card top() {
        return played.get(played.size() - 1);
    }

    /**
     * gets the current player
     * 
     * @return current player
     */
    public Player currentPlayer() {
        return players[turn];
    }

    /**
     * makes a play for the current player when the card is an Any or +4
     * 
     * @param index  index of the card to be played
     * @param choice color the player chose
     */
    public void play(int index, UnoColor choice) {
        Player p = currentPlayer();
        Card c = p.hand.get(index);

        // swaps the Any/+4's color to the chosen color
        c = new Card(choice, c.value());

        // move card from hand to played
        p.hand.remove(index);
        played.add(c);

        // add four cards to the next player's hand
        if (c.value() == UnoValue.PLUS_FOUR) {
            for (int i = 0; i < 4; i++) {
                players[(turn + direction + players.length) %
                        players.length].hand.add(deck.get(deck.size() - 1));
                deck.remove(deck.size() - 1);
            }
        }

        // increment / decrement turn, taking into account looping
        turn = (turn + direction + players.length) % players.length;
    }

    /**
     * makes a play for the current player when the card is not an Any or +4
     * 
     * @param index index of the card. {@code DRAW} means the player draws rather
     *              than playing a card
     */
    public void play(int index) {
        Player p = currentPlayer();

        // if DRAW, adds a card to the hand and increments the turn
        if (index == DRAW) {
            p.hand.add(deck.get(deck.size() - 1));
            deck.remove(deck.size() - 1);
            turn = (turn + direction + players.length) % players.length;
            return;
        }

        Card c = p.hand.get(index);

        // move card from hand to played
        p.hand.remove(index);
        played.add(c);

        // cards with special values: +2, reverse, skip
        switch (c.value()) {
            case PLUS_TWO: {
                // add two cards to the next players hand
                for (int i = 0; i < 2; i++) {
                    players[(turn + direction + players.length) % players.length].hand.add(deck.get(deck.size() - 1));
                    deck.remove(deck.size() - 1);
                }
                break;
            }
            case REVERSE: {
                // with two players, reverse acts like a skip
                if (players.length == 2) {
                    return;
                }

                // swap the direction
                direction *= -1;
                break;
            }
            case SKIP: {
                // go twice as far
                turn = (turn + direction * 2 + players.length) % players.length;
                return;
            }
            default:
                break;
        }

        // increment the turn
        turn = (turn + direction + players.length) % players.length;
    }
}
