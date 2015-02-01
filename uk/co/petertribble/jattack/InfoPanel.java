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
 * Copyright 2011-2015 Peter C Tribble <peter.tribble@gmail.com>
 */

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
