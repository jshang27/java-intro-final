import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Main GUI that interacts with {@code Game}
 * 
 * @author Jason Shang
 */
public class Window extends JFrame implements ActionListener {

    // mages of card faces
    BufferedImage[] cards = new BufferedImage[15 * 4];
    final double CARD_WIDTH = 409.5;
    final int CARD_HEIGHT = 585;
    BufferedImage atlas = new BufferedImage(1, 1, 1);
    BufferedImage back;

    JPanel next;
    JLabel[] names;
    JLabel[] decksizes;

    JPanel current;
    JLabel currentName;
    JLabel currentDeckSize;

    JPanel deckDisplay;
    ArrayList<BufferedImage> deck;

    JPanel gameState;
    JButton draw;
    JLabel top;
    JLabel color;

    int numPlayers;
    Game g;

    public Window(int numPlayers, int startingDeckSize) {
        super("JUno");

        /* GETTING CARD FACES */

        // read image from ./img/atlas.png
        try {
            atlas = ImageIO.read(this.getClass().getResourceAsStream("./img/atlas.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // loops for each color and value
        for (UnoColor uc : UnoColor.values()) {
            for (UnoValue uv : UnoValue.values()) {

                // cards is a collapsed 2D array, each row being 15
                int index = uc.ordinal() * 15 + uv.ordinal();

                // gets a splice from the image atlas
                // the cards have a fractional width (409.5) so must be converted to an int.
                BufferedImage card = atlas.getSubimage((int) (CARD_WIDTH * uv.ordinal()), CARD_HEIGHT * uc.ordinal(),
                        (int) CARD_WIDTH, CARD_HEIGHT);

                // image scaling from: https://stackoverflow.com/a/9417836
                Image img = card.getScaledInstance((int) (CARD_WIDTH / 3), CARD_HEIGHT / 3, BufferedImage.SCALE_SMOOTH);
                cards[index] = new BufferedImage((int) (CARD_WIDTH / 3), CARD_HEIGHT / 3,
                        BufferedImage.TYPE_4BYTE_ABGR);
                Graphics g = cards[index].getGraphics();
                g.drawImage(img, 0, 0, null);
                g.dispose();
            }
        }

        back = getBack();

        // initialize underlying Game
        this.numPlayers = numPlayers;
        g = new Game(numPlayers);
        g.deal(startingDeckSize);

        setSize(1400, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        Container content = getContentPane();

        // containers with all the players/decksizes
        next = new JPanel(new GridLayout(2, numPlayers - 1));
        names = new JLabel[numPlayers - 1];
        decksizes = new JLabel[numPlayers - 1];

        // adding all the player names and deck amounts to above containers
        for (int i = 0; i < numPlayers - 1; i++) {
            names[i] = new JLabel();
            names[i].setHorizontalAlignment(SwingConstants.CENTER);
            names[i].setAlignmentX(CENTER_ALIGNMENT);
            next.add(names[i]);
        }
        for (int i = 0; i < numPlayers - 1; i++) {
            decksizes[i] = new JLabel();
            decksizes[i].setHorizontalAlignment(SwingConstants.CENTER);
            decksizes[i].setAlignmentX(CENTER_ALIGNMENT);
            next.add(decksizes[i]);
        }

        // alignment
        next.setPreferredSize(new Dimension(getWidth(), 50));
        next.setAlignmentX(CENTER_ALIGNMENT);
        content.add(next, BorderLayout.NORTH);

        // adding text to the above containers
        updateDeckSizes();

        // contains stuff visible to every player
        gameState = new JPanel(new FlowLayout());
        gameState.setPreferredSize(new Dimension(getWidth(), CARD_HEIGHT / 3));

        // an image of a card back that allows the player the draw cards
        draw = new JButton(new ImageIcon(back));
        draw.setBorder(BorderFactory.createEmptyBorder());
        draw.setActionCommand(Integer.toString(Game.DRAW));
        draw.addActionListener(this);

        // the most recently played card
        top = new JLabel();

        // display the color
        color = new JLabel();
        color.setPreferredSize(new Dimension(80, 10));

        // add and update
        gameState.add(draw);
        gameState.add(top);
        gameState.add(color);
        content.add(gameState);
        updateTop();

        // display for the current player's name and deck size
        current = new JPanel(new GridLayout(2, 1));
        currentName = new JLabel();
        current.add(currentName);
        currentDeckSize = new JLabel();
        current.setBorder(BorderFactory.createEmptyBorder(50, 600, 0, 600));

        // add and update
        current.add(currentDeckSize);
        content.add(current);
        updateCurrent();

        // current player's deck
        deckDisplay = new JPanel(new FlowLayout());
        deckDisplay.setPreferredSize(new Dimension(1200, CARD_HEIGHT + 30));
        deckDisplay.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        // add and update
        content.add(deckDisplay, BorderLayout.SOUTH);
        updateNextButton();

        // so all images are visible
        repaint();
    }

    /**
     * updates the value of the Color display text
     */
    public void updateColor() {
        String[] clrnames = new String[] {
                "Red", "Yellow", "Blue", "Green"
        };
        Color[] clrs = new Color[] {
                new Color(0xbc271a), new Color(0xf4cd44), new Color(0x4e7ef7), new Color(0x589e61)
        };

        int clr = g.top().color().ordinal();
        color.setText(clrnames[clr]);
        color.setForeground(clrs[clr]);
    }

    /**
     * gets a {@code BufferedImage} of the back of an UnoCard from
     * {@code /img/back.png}.
     * 
     * @return {@code BufferedImage} of the back of an UnoCard.
     */
    public BufferedImage getBack() {
        BufferedImage back = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);

        // read image from ./img/back.png
        try {
            back = ImageIO.read(this.getClass().getResource("./img/back.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // resizing the image. from: https://stackoverflow.com/a/9417836
        BufferedImage tmp = new BufferedImage((int) (CARD_WIDTH / 3), CARD_HEIGHT / 3, BufferedImage.TYPE_4BYTE_ABGR);
        Image img = back.getScaledInstance((int) (CARD_WIDTH / 3), CARD_HEIGHT / 3, BufferedImage.SCALE_SMOOTH);
        Graphics g = tmp.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return tmp;
    }

    /**
     * updates the display for the most recently played card
     */
    public void updateTop() {
        Card c = g.top();
        top.setIcon(new ImageIcon(cards[c.color().ordinal() * 15 + c.value().ordinal()]));
        updateColor();
    }

    /**
     * updates the deck display to have a "Ready" button so the previous player
     * doesn't see the next player's deck
     */
    public void updateNextButton() {
        deckDisplay.removeAll();
        JButton jb = new JButton("Ready");
        draw.removeActionListener(this);

        Window win = this;

        jb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                win.updateCurrentDeck();
                draw.addActionListener(win);
                win.revalidate();
            }
        });

        deckDisplay.add(jb);
        win.repaint();
    }

    /**
     * updates the display for the current players hand
     */
    public void updateCurrentDeck() {
        deckDisplay.removeAll();

        int index = 0;

        // loops for each card in the player's hand
        for (Card c : g.currentPlayer().hand) {
            // get the image from atlas
            BufferedImage tmp = cards[c.color().ordinal() * 15 + c.value().ordinal()];
            ImageIcon i = new ImageIcon(tmp);

            // make the button with an ImageIcon of the card
            JButton b = new JButton(i);
            b.setBorder(BorderFactory.createEmptyBorder());

            // if the card can be played, add a yellow border and make it a button that
            // works
            if (c.canPlay(g.top())) {
                b.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0xffff00)));
                b.setEnabled(true);
                b.setActionCommand(Integer.toString(index));
                b.addActionListener(this);
            }

            deckDisplay.add(b);
            index++;
        }
    }

    /**
     * update the display for the current player information
     */
    public void updateCurrent() {
        currentName.setText("Player " + (g.turn + 1));
        currentDeckSize.setText(g.currentPlayer().hand.size() + " cards");
    }

    /**
     * update the displays for the player information of other players
     */
    public void updateDeckSizes() {
        // iterate through every player up to the current player
        for (int i = 0; i < g.turn; i++) {
            names[i].setText("Player " + (i + 1));
            names[i].setBorder(BorderFactory.createEmptyBorder());
            decksizes[i].setText(g.players[i].hand.size() + " cards");
        }
        // iterate through every player after the current player
        for (int i = g.turn + 1; i < numPlayers; i++) {
            names[i - 1].setText("Player " + (i + 1));
            names[i - 1].setBorder(BorderFactory.createEmptyBorder());
            decksizes[i - 1].setText(g.players[i].hand.size() + " cards");
        }

        // add a border for the next person, determined by the direction of Game
        if (g.direction > 0) {
            names[g.turn % (numPlayers - 1)]
                    .setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0xffff00)));
        } else {
            names[(g.turn - 2 + numPlayers) % (numPlayers - 1)]
                    .setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(0xffff00)));
        }
    }

    /**
     * promps the user for a color using a window
     * 
     * @return {@code UnoColor} of the color chosen
     */
    public UnoColor getColor() {
        ColorChoose cc = new ColorChoose();
        return cc.color;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // card played
        int play = Integer.parseInt(e.getActionCommand());

        // savng the current player data
        Player p = g.currentPlayer();
        int playerNum = g.turn;

        // Any and +4 have to use getColor to pass a color in aswell
        if (play > 0 && p.hand.get(play).value().compareTo(UnoValue.ANY) >= 0) {
            g.play(play, getColor());
        } else {
            g.play(play);
        }

        // if no cards left (i.e played all), win game
        if (p.hand.isEmpty()) {
            this.dispose();
            new Winscreen(playerNum);
        }

        // update every GUI
        updateDeckSizes();
        updateCurrent();
        updateTop();
        updateNextButton();
    }
}
