package uk.co.petertribble.jattack;

import javax.swing.JPanel;
import javax.swing.BoxLayout;

public class AttackPanel extends JPanel {

    private GamePanel gpanel;

    public AttackPanel(int ncolumns, int nrows) {
	setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	InfoPanel ipanel = new InfoPanel();
	gpanel = new GamePanel(ipanel, ncolumns, nrows);
	add(gpanel);
	add(ipanel);
    }

    public void newGame() {
	gpanel.newGame();
    }
}
