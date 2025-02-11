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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * The main Frame to display a Jattack game. Implements the menu bar and holds
 * the game in a panel.
 */
public final class AttackFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

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
    /**
     * The default number of columns. Override with the -c cli flag.
     */
    private static final int DEFAULT_COLUMNS = 6;
    /**
     * The default number of rows. Override with the -r cli flag.
     */
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

	addWindowListener(new WindowExit());

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

	setIconImage(new ImageIcon(this.getClass().getClassLoader()
				   .getResource("pixmaps/jattack.png"))
		     .getImage());
	pack();
	setVisible(true);
    }

    class WindowExit extends WindowAdapter {
	@Override
	public void windowClosing(WindowEvent we) {
	    System.exit(0);
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (exitItem.equals(e.getSource())) {
	    System.exit(0);
	} else if (newItem.equals(e.getSource())) {
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
	    int chosenrows = DEFAULT_ROWS;
	    int chosencolumns = DEFAULT_COLUMNS;
	    while (i < args.length) {
		if ("-r".equals(args[i])) {
		    ++i;
		    if (i < args.length) {
			try {
			    chosenrows = Integer.parseInt(args[i]);
			} catch (NumberFormatException ex) {
			    bailOut("Invalid rows!");
			}
			if (chosenrows < DEFAULT_ROWS) {
			    bailOut("Too few rows!");
			}
		    } else {
			bailOut("Expecting an argument to -c!");
		    }
		} else if ("-c".equals(args[i])) {
		    ++i;
		    if (i < args.length) {
			try {
			    chosencolumns = Integer.parseInt(args[i]);
			} catch (NumberFormatException ex) {
			    bailOut("Invalid columns!");
			}
			if (chosencolumns < DEFAULT_COLUMNS) {
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
	    new AttackFrame(chosencolumns, chosenrows);
	} else {
	    new AttackFrame();
	}
    }
}
