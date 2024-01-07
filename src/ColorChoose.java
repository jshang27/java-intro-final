import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * promps the user for a color
 */
public class ColorChoose extends JFrame implements ActionListener {
    UnoColor color;

    /**
     * constructor
     */
    public ColorChoose() {
        super("Color Chooser");

        // set up. modal = stops all other windows
        JDialog jd = new JDialog(this, true);
        jd.setUndecorated(true);
        jd.setLayout(new GridLayout(2, 2));
        jd.setSize(400, 400);
        jd.setAlwaysOnTop(true);
        jd.setAutoRequestFocus(true);

        // red button
        JButton red = new JButton();
        red.setBackground(new Color(0xbc271a));
        red.addActionListener(this);
        red.setActionCommand("0");

        // yellow button
        JButton yellow = new JButton();
        yellow.setBackground(new Color(0xf4cd44));
        yellow.addActionListener(this);
        yellow.setActionCommand("1");

        // blue button
        JButton blue = new JButton();
        blue.setBackground(new Color(0x4e7ef7));
        blue.addActionListener(this);
        blue.setActionCommand("2");

        // green button
        JButton green = new JButton();
        green.setBackground(new Color(0x589e61));
        green.addActionListener(this);
        green.setActionCommand("3");

        // container
        Container content = jd.getContentPane();
        content.add(red);
        content.add(yellow);
        content.add(blue);
        content.add(green);

        // jd.setContentPane(content);
        jd.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        color = UnoColor.values()[e.getActionCommand().charAt(0) - '0'];
        this.dispose();
    }
}
