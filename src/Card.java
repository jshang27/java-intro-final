import java.util.ArrayList;
import java.util.Collections;

/**
 * Uno Card containing an UnoColor and UnoValue. UnoColor on Any and +4 cards
 * should be ignored.
 */
public record Card(UnoColor color, UnoValue value) {

    /**
     * Creates a new, shuffled Uno deck with 108 cards.
     * 
     * @return Uno deck
     */
    public static ArrayList<Card> makeNewDeck() {
        ArrayList<Card> cards = new ArrayList<Card>(108);

        // goes through each value and color:
        for (UnoValue uv : UnoValue.values()) {
            for (UnoColor uc : UnoColor.values()) {

                // adds a Card for each color, for each value
                cards.add(new Card(uc, uv));

                // 1-9 have two for each color, other values only have one card.
                if (uv.compareTo(UnoValue.ONE) >= 0 && uv.compareTo(UnoValue.REVERSE) <= 0) {
                    cards.add(new Card(uc, uv));
                }
            }
        }

        Collections.shuffle(cards);

        return cards;
    }

    /**
     * Returns if this could be played on {@code c}. If this is an Any or +4, always
     * returns true. Otherwise, returns if the color is equal OR the value is equal.
     * 
     * @param c {@code Card} to check this with.
     * @return {@code true} if can be played; {@code false} otherwise.
     */
    public boolean canPlay(Card c) {
        // same color
        if (this.color.equals(c.color)) {
            return true;
        }

        // same value
        if (this.value.equals(c.value)) {
            return true;
        }

        // Any or +4
        if (this.value.equals(UnoValue.ANY) || this.value.equals(UnoValue.PLUS_FOUR)) {
            return true;
        }
        return false;
    }
};