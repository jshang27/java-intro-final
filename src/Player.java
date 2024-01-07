import java.util.ArrayList;

/**
 * a player of Uno
 */
public class Player {
    public ArrayList<Card> hand;

    /**
     * constructor
     */
    public Player() {
        hand = new ArrayList<Card>();
    }

    /**
     * returns all valid plays with a certain card
     * 
     * @param top the most recently played card (or any card to check)
     * @return an {@code int[]} containing every valid play
     */
    public int[] getValidPlays(Card top) {
        int[] v = new int[hand.size()]; // worst case, all plays are valid
        int trueSize = 0;

        // checks each card's canPlay() method
        for (Card c : (Card[]) hand.toArray()) {
            if (c.canPlay(top)) {
                v[trueSize] = trueSize;
                trueSize++;
            }
        }

        // resizing the list to the correct size
        int[] ret = new int[trueSize];
        for (int i = 0; i < trueSize; i++) {
            ret[i] = v[i];
        }

        return ret;
    }
}
