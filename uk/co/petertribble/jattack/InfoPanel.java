package uk.co.petertribble.jattack;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

public class InfoPanel extends JPanel {

    private JLabel scoreLabel;
    private JLabel speedLabel;
    private JLabel msgLabel;

    public InfoPanel() {
	setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	JLabel tscoreLabel = new JLabel("Score:");
	tscoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	add(tscoreLabel);
	scoreLabel = new JLabel("0");
	scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	add(scoreLabel);
	JLabel tspeedLabel = new JLabel("Speed:");
	tspeedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	add(tspeedLabel);
	speedLabel = new JLabel("0");
	speedLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	add(speedLabel);
	setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	msgLabel = new JLabel();
	msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	add(msgLabel);
	add(Box.createRigidArea(new Dimension(80,1)));
	setSize(400, 400);
    }

    public void setSpeed(int speed) {
	speedLabel.setText(Integer.toString(speed));
    }

    public void setScore(int score) {
	scoreLabel.setText(Integer.toString(score));
    }

    public void setMessage(String s) {
	msgLabel.setText(s);
    }
}
