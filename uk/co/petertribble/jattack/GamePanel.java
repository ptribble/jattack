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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 * The visible part of the game.
 */
public final class GamePanel extends JPanel implements ActionListener,
						       MouseListener {

    private static final long serialVersionUID = 1L;

    /**
     * The starting delay, inverse of speed.
     */
    private static final int START_DELAY = 401;
    /**
     * The starting level.
     */
    private static final int START_LEVEL = 1;

    /**
     * The number of columns in the game.
     */
    private final int ncolumns;
    /**
     * The number of rows in the game.
     */
    private final int nrows;
    /**
     * The size of each cell.
     */
    private static final int CELLSIZE = 24;
    /**
     * The size of the cell body, allowing a single pixel border.
     */
    private static final int CELLP = CELLSIZE - 2;
    /**
     * An array to hold the cells.
     */
    private int[][] cells;
    /**
     * The row currently at the top of the game.
     */
    private int toprow;
    /**
     * The row coming into view and partly visible.
     */
    private int partrow;
    /**
     * The current level.
     */
    private int level = START_LEVEL;
    /**
     * The current delay, inverse to speed.
     */
    private int delay = START_DELAY;
    /**
     * A Timer to update the game.
     */
    private Timer timer;
    /**
     * The row last clicked on.
     */
    private int rowclick = -2;
    /**
     * The column last clicked on.
     */
    private int colclick = -2;
    /**
     * The current score.
     */
    private int score;
    /**
     * Is the game active (as opposed to stopped).
     */
    boolean active;
    /**
     * The index of the current warning color.
     */
    private int iwarn;
    /**
     * A counter for mutliple matches in a single step.
     */
    private int multimatch;

    /**
     * The possible colors of each cell.
     * The original has 5: diamonds, circles, triangles, hearts, stars
     */
    private static final Color[] COLOURS = {Color.RED,
					    Color.BLUE,
					    Color.GREEN,
					    Color.YELLOW,
					    Color.ORANGE,
					    Color.PINK};
    /**
     * The background color.
     */
    private Color bgcolor = Color.BLACK;
    /**
     * The color of a border put around a clicked cell..
     */
    private static final Color CLICKCOLOR = Color.WHITE;

    /**
     * An InfoPanel to be updated with the game's progress.
     */
    private final InfoPanel ipanel;

    /**
     * Create a game panel.
     *
     * @param nipanel and InfoPanel showing information on game progress
     * @param nncolumns the desired number of rows
     * @param nnrows the desired number of rows
     */
    public GamePanel(InfoPanel nipanel, int nncolumns, int nnrows) {
	ipanel = nipanel;
	ncolumns = nncolumns;
	nrows = nnrows;
	cells = new int[ncolumns][nrows];
	Dimension gdim = new Dimension(ncolumns * CELLSIZE, nrows * CELLSIZE);
	setMinimumSize(gdim);
	setPreferredSize(gdim);
	setMaximumSize(gdim);
	addMouseListener(this);
	getInputMap().put(KeyStroke.getKeyStroke("SPACE"),
			"pressed");
	getActionMap().put("pressed", mstep);
	newGame();
    }

    /*
     * If a cell contains 0, it's empty and isn't shown. If it is > 0, then
     * the appropriate pattern for that cell is shown.
     */
    private void populate(int itop) {
	// make the cells at the top of the board empty
	for (int i = 0; i < ncolumns; i++) {
	    for (int j = 0; j < nrows - itop + 2; j++) {
		cells[i][j] = 0;
	    }
	}
	// then populate the bottom itop rows
	cells[2][nrows - itop] = newCell();
	cells[3][nrows - itop] = newCell();
	cells[1][nrows - itop + 1] = newCell();
	cells[2][nrows - itop + 1] = newCell();
	cells[3][nrows - itop + 1] = newCell();
	cells[4][nrows - itop + 1] = newCell();
	for (int j = nrows - itop + 2; j < nrows; j++) {
	    fill(j);
	}
    }

    private int newCell() {
	double dl = (double) COLOURS.length - 2;
	return (int) Math.floor(dl * Math.random() + 1.5d);
    }

    /**
     * A multiple step.
     */
    AbstractAction mstep = new AbstractAction() {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (active) {
		step();
		step();
		step();
		step();
		step();
		step();
	    }
	}
    };

    /*
     * Every time round the loop, we step up the screen. If a row reaches the
     * top, we check to see if it contains any valid cells. If so, the game
     * is lost. If not, we move the row counter down one, reset the partial
     * row, populate the next row, and increase the speed.
     */
    void step() {
	multimatch = 0;
	if (partrow == 0) {
	    fill(toprow);
	}
	partrow++;
	if (partrow == CELLSIZE) {
	    partrow = 0;
	    toprow++;
	    if (toprow == nrows) {
		toprow = 0;
	    }
	    endCheck();
	    checkAll();
	    upspeed();
	}
	if (active) {
	    warnCheck();
	}
	repaint();
    }

    /*
     * This checks if any cells in the very top row are populated. If so,
     * the game is lost.
     */
    private void endCheck() {
	int lost = 0;
	for (int i = 0; i < ncolumns; i++) {
	    lost += cells[i][toprow];
	}
	if (lost > 0) {
	    ipanel.setMessage("Game Over!");
	    bgcolor = Color.BLACK;
	    active = false;
	    stopLoop();
	}
    }

    /*
     * This checks whether we have any cells within 1 row of hitting the top.
     * If so, the background flashes.
     */
    private void warnCheck() {
	int nrow = toprow + 1;
	if (nrow == nrows) {
	    nrow = 0;
	}
	int lost = 0;
	for (int i = 0; i < ncolumns; i++) {
	    lost += cells[i][nrow];
	}
	if (lost > 0) {
	    iwarn++;
	    if (iwarn >= COLOURS.length) {
		iwarn = 0;
	    }
	    bgcolor = COLOURS[iwarn].darker();
	} else {
	    bgcolor = COLOURS[0];
	}
    }

    /*
     * Clear a horizontal line, and move down all the rows above it.
     */
    private void clearHmatch(int i, int srow, int nscore) {
	int iscore = nscore;
	// we know it starts at i, but where does it finish
	int imax = i;
	while (imax < ncolumns && cells[i][srow] == cells[imax][srow]) {
	    imax++;
	}
	// imax is one extra, but this is accounted for in the loop
	// below
	for (int irow = i; irow < imax; irow++) {
	    shuffleDown(irow, srow);
	    iscore *= 2;
	}
	addScore(iscore);
	checkAll();
    }

    /*
     * Clear a vertical line.
     */
    private void clearVmatch(int i, int j, int nscore) {
	int val = cells[i][j];
	int iscore = nscore;
	while (cells[i][j] == val) {
	    shuffleDown(i, j);
	    iscore *= 2;
	}
	addScore(iscore);
	checkAll();
    }

    /*
     * Shuffle a row down 1 place, starting at row srow of column i.
     * Used when clearing a horizontal line.
     */
    private void shuffleDown(int i, int srow) {
	int irow = srow;
	int trow = srow - 1;
	if (irow < 0) {
	    irow += nrows;
	}
	if (trow < 0) {
	    trow += nrows;
	}
	while (trow != toprow) {
	    cells[i][irow] = cells[i][trow];
	    irow--;
	    trow--;
	    if (irow < 0) {
		irow += nrows;
	    }
	    if (trow < 0) {
		trow += nrows;
	    }
	}
	cells[i][irow] = 0;
    }

    private void upspeed() {
	delay -= 3;
	timer.setDelay(delay);
	ipanel.setSpeed(++level);
    }

    private void fill(int irow) {
	for (int i = 0; i < ncolumns; i++) {
	    cells[i][irow] = newCell();
	}
    }

    /**
     * Request a new game.
     */
    public void newGame() {
	stopLoop();
	level = START_LEVEL;
	delay = START_DELAY;
	bgcolor = COLOURS[0];
	toprow = 0;
	partrow = 0;
	populate(3);
	active = true;
	startLoop();
	ipanel.setSpeed(level);
	score = 0;
	ipanel.setScore(score);
	ipanel.setMessage("");
    }

    private void startLoop() {
	if (timer == null) {
	    timer = new Timer(delay, this);
	}
	timer.start();
    }

    private void stopLoop() {
	if (timer != null) {
	    timer.stop();
	}
    }

    private void checkAll() {
	// horizontal rows
	for (int i = 0; i < ncolumns - 2; i++) {
	    for (int j = 0; j < nrows; j++) {
		if (j == toprow) {
		    continue;
		}
		if (cells[i][j] != 0
			    && cells[i][j] == cells[i + 1][j]
			    && cells[i][j] == cells[i + 2][j]) {
		    clearHmatch(i, j, 10);
		}
	    }
	}
	// vertical rows
	for (int i = 0; i < ncolumns; i++) {
	    //for (int j = 0; j < nrows; j++) {
	    for (int j = nrows - 1; j >= 0; j--) {
		if (j == toprow) {
		    continue;
		}
		// FIXME handle more than 3
		int row1 = j - 1;
		if (row1 == -1) {
		    row1 = nrows - 1;
		}
		int row2 = row1 - 1;
		if (row2 == -1) {
		    row2 = nrows - 1;
		}
		if (cells[i][j] != 0
			    && cells[i][j] == cells[i][row1]
			    && cells[i][j] == cells[i][row2]) {
		    clearVmatch(i, j, 10);
		}
	    }
	}
    }

    private void addScore(int i) {
	multimatch++;
	score += i * multimatch;
	ipanel.setScore(score);
    }

    // MouseListener

    @Override
    public void mouseClicked(MouseEvent e) {
	/* Empty implementation for MouseListener interface. */
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	/* Empty implementation for MouseListener interface. */
    }

    @Override
    public void mouseEntered(MouseEvent e) {
	/* Empty implementation for MouseListener interface. */
    }

    @Override
    public void mouseExited(MouseEvent e) {
	/* Empty implementation for MouseListener interface. */
    }

    @Override
    public void mousePressed(MouseEvent e) {
	if (!active) {
	    return;
	}
	int colnew = -4;
	int rownew = -4;
	int x = e.getX();
	int y = e.getY();
	for (int i = 0; i < ncolumns; i++) {
	    int istart = i * CELLSIZE;
	    if (x >= istart && x < (istart + CELLSIZE)) {
		colnew = i;
	    }
	    for (int j = 0; j < nrows; j++) {
		int jstart = (j - toprow) * CELLSIZE - partrow;
		if (jstart < 0) {
		    jstart += nrows * CELLSIZE;
		}
		if (y >= jstart && y < (jstart + CELLSIZE)) {
		    rownew = j;
		}
	    }
	}
	// don't allow moving blocks in the partially visible row
	if (rownew == toprow) {
	    return;
	}
	// so, we know where we are. if the previous click was valid, and
	// we're on the same row, and we're on neighbouring columns, swap the
	// cells
	if (rowclick == rownew
	        && (colclick == colnew + 1 || colclick == colnew - 1)) {
	    // swap cells
	    int itmp = cells[colclick][rowclick];
	    cells[colclick][rowclick] = cells[colnew][rownew];
	    cells[colnew][rownew] = itmp;
	    // if an empty cell, shuffle down anything above it
	    if (cells[colclick][rowclick] == 0) {
		shuffleDown(colclick, rowclick);
	    }
	    if (cells[colnew][rownew] == 0) {
		shuffleDown(colnew, rownew);
	    }
	    // if we end up over an empty cell, drop down
	    int rowbelow = rownew + 1;
	    if (rowbelow == nrows) {
		rowbelow = 0;
	    }
	    while (rowbelow != toprow && cells[colnew][rowbelow] == 0) {
		cells[colnew][rowbelow] = cells[colnew][rownew];
		cells[colnew][rownew] = 0;
		rowbelow++;
		rownew++;
		if (rowbelow == nrows) {
		    rowbelow = 0;
		}
		if (rownew == nrows) {
		    rownew = 0;
		}
	    }
	    rowbelow = rowclick + 1;
	    if (rowbelow == nrows) {
		rowbelow = 0;
	    }
	    while (rowbelow != toprow && cells[colclick][rowbelow] == 0) {
		cells[colclick][rowbelow] = cells[colclick][rowclick];
		cells[colclick][rowclick] = 0;
		rowbelow++;
		rowclick++;
		if (rowbelow == nrows) {
		    rowbelow = 0;
		}
		if (rowclick == nrows) {
		    rowclick = 0;
		}
	    }
	    repaint();
	    rowclick = -2;
	    colclick = -2;
	    checkAll();
	    return;
	}
	// save this one for next time
	rowclick = rownew;
	colclick = colnew;
    }

    // ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
	if (active) {
	    step();
	}
    }

    @Override
    public void paint(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;
	Dimension d = getSize();
	g2.setColor(bgcolor);
	g2.fillRect(0, 0, d.width, d.height);
	for (int i = 0; i < ncolumns; i++) {
	    int istart = i * CELLSIZE + 1;
	    for (int j = 0; j < nrows; j++) {
		if (cells[i][j] > 0) {
		    int jstart = (j - toprow) * CELLSIZE - partrow;
		    if (jstart < 0) {
			jstart += nrows * CELLSIZE;
		    }
		    jstart++;
		    // highlight the currently clicked cell with a border
		    if (i == colclick && j == rowclick) {
			g2.setColor(CLICKCOLOR);
			g2.fillRect(istart - 1, jstart - 1, CELLSIZE, CELLSIZE);
		    }
		    // the partially visible row is shaded
		    g2.setColor((j == toprow) ? COLOURS[cells[i][j]].darker()
				: COLOURS[cells[i][j]]);
		    g2.fill3DRect(istart, jstart, CELLP, CELLP, true);
		}
	    }
	}
    }
}
