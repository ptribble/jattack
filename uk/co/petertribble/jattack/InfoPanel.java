/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at usr/src/OPENSOLARIS.LICENSE
 * or http://www.opensolaris.org/os/licensing.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at usr/src/OPENSOLARIS.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 * Copyright 2011-2024 Peter C Tribble <peter.tribble@gmail.com>
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
public class InfoPanel extends JPanel {

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
