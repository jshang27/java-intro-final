import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

/**
 * prompt for the starting settings of the JUno game
 */
public class SettingsScreen extends JFrame implements ActionListener {

    JSlider numPlayers, numCards;

    /**
     * constructor
     */
    public SettingsScreen() {
        super("JUno Launcher");

        // set up
        setSize(200, 300);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container ctx = getContentPane();
        ctx.setLayout(new BoxLayout(ctx, BoxLayout.PAGE_AXIS));

        // "JUno" at the top of page
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("JUno");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font(getFont().getName(), getFont().getStyle(), 30));
        titlePanel.add(title);
        ctx.add(titlePanel);

        // slider + labels for deciding the number of players
        JPanel npPanel = new JPanel();
        npPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel npLabel = new JLabel("Number of players:");
        npPanel.add(npLabel);
        JLabel npNum = new JLabel("2");
        npNum.setPreferredSize(new Dimension(20, 20));
        numPlayers = new JSlider(2, 10, 2);
        numPlayers.setPreferredSize(new Dimension(140, 20));
        numPlayers.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // sets the label next to it to the value
                npNum.setText(Integer.toString(numPlayers.getValue()));

                // changes the other slider's maximum so user cannot choose more cards to deal
                // that are in the deck
                numCards.setMaximum(106 / numPlayers.getValue());
            }
        });

        // adding
        npPanel.add(numPlayers);
        npPanel.add(npNum);
        ctx.add(npPanel);

        // slider + labels for deciding the number of cards each player starts with
        JPanel ncPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel ncLabel = new JLabel("Number of starting cards:");
        ncPanel.add(ncLabel);
        JLabel ncNum = new JLabel("3");
        ncNum.setPreferredSize(new Dimension(20, 20));
        numCards = new JSlider(3, 15, 3);
        numCards.setPreferredSize(new Dimension(140, 20));
        numCards.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                // sets the label next to it to the value
                ncNum.setText(Integer.toString(numCards.getValue()));
            }
        });

        // adding
        ncPanel.add(numCards);
        ncPanel.add(ncNum);
        ctx.add(ncPanel);

        // idk if this works tbh
        ctx.add(Box.createVerticalGlue());

        // start button
        JPanel buttonPanel = new JPanel();
        JButton jb = new JButton("START");
        jb.addActionListener(this);

        buttonPanel.add(jb);
        ctx.add(buttonPanel);

        revalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
        new Window(numPlayers.getValue(), numCards.getValue());
    }
}
