import java.awt.*;
import javax.swing.*;

/**
 * displays the winner
 */
public class Winscreen extends JFrame {

    /**
     * constructor
     * 
     * @param winner the number of the player who won
     */
    public Winscreen(int winner) {
        super("Winner!");

        // set up
        setSize(200, 300);
        setVisible(true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Container ctx = getContentPane();

        // "The winner is..." text above the winner number
        JLabel jl = new JLabel("The winner is...");
        jl.setPreferredSize(new Dimension(200, 20));
        jl.setHorizontalAlignment(SwingConstants.CENTER);
        ctx.add(jl, BorderLayout.NORTH);

        // winner display
        JLabel win = new JLabel("Player " + (winner + 1) + "!");
        win.setPreferredSize(new Dimension(200, 30));
        win.setHorizontalAlignment(SwingConstants.CENTER);
        win.setFont(new Font(getFont().getName(), Font.PLAIN, 30));
        win.setForeground(getBackground());
        ctx.add(win, BorderLayout.CENTER);
    }
}