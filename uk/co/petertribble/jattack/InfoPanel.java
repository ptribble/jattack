/*
 * SPDX-License-Identifier: CDDL-1.0
 *
 * CDDL HEADER START
 *
 * This file and its contents are supplied under the terms of the
 * Common Development and Distribution License ("CDDL"), version 1.0.
 * You may only use this file in accordance with the terms of version
 * 1.0 of the CDDL.
 *
 * A full copy of the text of the CDDL should have accompanied this
 * source. A copy of the CDDL is also available via the Internet at
 * http://www.illumos.org/license/CDDL.
 *
 * CDDL HEADER END
 *
 * Copyright 2025 Peter Tribble
 *
 */

package uk.co.petertribble.jattack;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The information panel, displayed to the right of the game, showing
 * the current speed and score.
 */
public final class InfoPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * The JLabel to display the current score.
     */
    private final JLabel scoreLabel;
    /**
     * The JLabel to display the current speed.
     */
    private final JLabel speedLabel;
    /**
     * The JLabel to display an arbitrary message.
     */
    private final JLabel msgLabel;

    /**
     * Create a new InfoPanel.
     */
    public InfoPanel() {
	setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	JLabel tscoreLabel = new JLabel("Score:");
	tscoreLabel.setAlignmentX(CENTER_ALIGNMENT);
	add(tscoreLabel);
	scoreLabel = new JLabel("0");
	scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
	add(scoreLabel);
	JLabel tspeedLabel = new JLabel("Speed:");
	tspeedLabel.setAlignmentX(CENTER_ALIGNMENT);
	add(tspeedLabel);
	speedLabel = new JLabel("0");
	speedLabel.setAlignmentX(CENTER_ALIGNMENT);
	add(speedLabel);
	setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	msgLabel = new JLabel();
	msgLabel.setAlignmentX(CENTER_ALIGNMENT);
	add(msgLabel);
	add(Box.createRigidArea(new Dimension(80, 1)));
	setSize(400, 400);
    }

    /**
     * Update the speed label.
     *
     * @param speed the current speed
     */
    public void setSpeed(int speed) {
	speedLabel.setText(Integer.toString(speed));
    }

    /**
     * Update the score label.
     *
     * @param score the current score
     */
    public void setScore(int score) {
	scoreLabel.setText(Integer.toString(score));
    }

    /**
     * Update the displayed message.
     *
     * @param s the message text to display
     */
    public void setMessage(String s) {
	msgLabel.setText(s);
    }
}
