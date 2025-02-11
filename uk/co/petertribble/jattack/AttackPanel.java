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

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * A Panel holding the game panel and an information panel.
 */
public final class AttackPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * The panel containing the game.
     */
    private final GamePanel gpanel;

    /**
     * Create an AttackPanel of the requested size.
     *
     * @param ncolumns the desired number of rows
     * @param nrows the desired number of rows
     */
    public AttackPanel(int ncolumns, int nrows) {
	setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	InfoPanel ipanel = new InfoPanel();
	gpanel = new GamePanel(ipanel, ncolumns, nrows);
	add(gpanel);
	add(ipanel);
    }

    /**
     * Request a new game.
     */
    public void newGame() {
	gpanel.newGame();
    }
}
