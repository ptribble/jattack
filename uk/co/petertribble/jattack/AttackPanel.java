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
 * Copyright 2011-2020 Peter C Tribble <peter.tribble@gmail.com>
 */

package uk.co.petertribble.jattack;

import javax.swing.JPanel;
import javax.swing.BoxLayout;

public class AttackPanel extends JPanel {

    private final GamePanel gpanel;

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
