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
 * Copyright 2011-2021 Peter C Tribble <peter.tribble@gmail.com>
 */

package uk.co.petertribble.jattack;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import java.awt.event.*;

/**
 * The main Frame to display a Jattack game. Implements the menu bar and holds
 * the game in a panel.
 */
public class AttackFrame extends JFrame implements ActionListener {

    /**
     * A menu item for Exit.
     */
    private JMenuItem exitItem;
    /**
     * A menu item for New game.
     */
    private JMenuItem newItem;
    /**
     * The panel holding the game.
     */
    private AttackPanel apanel;

    /*
     * Default sizes.
     */
    private static final int DEFAULT_COLUMNS = 6;
    private static final int DEFAULT_ROWS = 9;

    /**
     * Create a Frame containing the game at the default size.
     */
    public AttackFrame() {
	this(DEFAULT_COLUMNS, DEFAULT_ROWS);
    }

    /**
     * Create a Frame containing the game at the requested size.
     *
     * @param ncolumns the desired number of rows
     * @param nrows the desired number of rows
     */
    public AttackFrame(int ncolumns, int nrows) {
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

	apanel = new AttackPanel(ncolumns, nrows);
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

    private static void bailOut(String s) {
	System.err.println(s); //NOPMD
	System.exit(1);
    }
    
    /**
     * Run a new JAttack game. Allows the number of rows to be
     * specified with -r, and the number of columns with -c.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
	if (args.length > 0) {
	    int i = 0;
	    int NROWS = DEFAULT_ROWS;
	    int NCOLUMNS = DEFAULT_COLUMNS;
	    while (i < args.length) {
		if ("-r".equals(args[i])) {
		    ++i;
		    if (i < args.length) {
			try {
			    NROWS = Integer.parseInt(args[i]);
			} catch (NumberFormatException ex) {
			    bailOut("Invalid rows!");
			}
			if (NROWS < DEFAULT_ROWS) {
			    bailOut("Too few rows!");
			}
		    } else {
			bailOut("Expecting an argument to -c!");
		    }
		} else if ("-c".equals(args[i])) {
		    ++i;
		    if (i < args.length) {
			try {
			    NCOLUMNS = Integer.parseInt(args[i]);
			} catch (NumberFormatException ex) {
			    bailOut("Invalid columns!");
			}
			if (NCOLUMNS < DEFAULT_COLUMNS) {
			    bailOut("Too few columns!");
			}
		    } else {
			bailOut("Expecting an argument to -c!");
		    }
		} else {
		    break;
		}
		++i;
	    }
	    new AttackFrame(NCOLUMNS, NROWS);
	} else {
	    new AttackFrame();
	}
    }
}
