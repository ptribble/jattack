package uk.co.petertribble.jattack;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.*;

public class AttackFrame extends JFrame implements ActionListener {

    /*
     * Menu buttons.
     */
    private JMenuItem exitItem;
    private JMenuItem newItem;
    private AttackPanel apanel;

    public AttackFrame() {
	super("JAttack");

	addWindowListener(new winExit());

	JMenu jmf = new JMenu("File");
	jmf.setMnemonic(KeyEvent.VK_F);
	newItem = new JMenuItem("New Game", KeyEvent.VK_N);
	newItem.addActionListener(this);
	exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
	exitItem.addActionListener(this);
	jmf.add(newItem);
	jmf.addSeparator();
	jmf.add(exitItem);

	JMenuBar jm = new JMenuBar();
	jm.add(jmf);
	setJMenuBar(jm);

	// default size is 6 columns and 9 rows
	apanel = new AttackPanel(6, 9);
	setContentPane(apanel);

	setIconImage(new ImageIcon(this.getClass().getClassLoader().getResource("pixmaps/jattack.png")).getImage());
	pack();
	setVisible(true);
    }

    class winExit extends WindowAdapter {
	public void windowClosing(WindowEvent we) {
	    System.exit(0);
	}
    }

    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == exitItem) {
	    System.exit(0);
	} else if (e.getSource() == newItem) {
	    apanel.newGame();
	}
    }

    public static void main (String[] args) {
	new AttackFrame();
    }
}
