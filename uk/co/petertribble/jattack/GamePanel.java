package uk.co.petertribble.jattack;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener, MouseListener {
    private static final int START_DELAY = 401;
    private static final int START_LEVEL = 1;

    private int ncolumns;
    private int nrows;
    private int cellsize = 24;
    private int cellp = cellsize - 2;
    private int[][] cells;
    private int toprow;
    private int partrow;
    private int level = START_LEVEL;
    private int delay = START_DELAY;
    private Timer timer;
    private int rowclick = -2;
    private int colclick = -2;
    private int score;
    private boolean active;
    private int iwarn;
    private int multimatch;

    // the original has 5: diamonds, circles, triangles, hearts, stars
    private Color[] Colours = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK};
    private Color bgcolor = Color.BLACK;

    private InfoPanel ipanel;

    public GamePanel(InfoPanel ipanel, int ncolumns, int nrows) {
	this.ipanel = ipanel;
	this.ncolumns = ncolumns;
	this.nrows = nrows;
	cells = new int[ncolumns][nrows];
	Dimension gdim = new Dimension(ncolumns*cellsize, nrows*cellsize);
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
	cells[2][nrows-itop] = newCell();
	cells[3][nrows-itop] = newCell();
	cells[1][nrows-itop+1] = newCell();
	cells[2][nrows-itop+1] = newCell();
	cells[3][nrows-itop+1] = newCell();
	cells[4][nrows-itop+1] = newCell();
	for (int j = nrows-itop+2; j < nrows; j++) {
	    fill(j);
	}
    }

    private int newCell() {
	double dl = (double) Colours.length - 2;
	return (int) Math.floor(dl*Math.random() + 1.5d);
    }

    Action mstep = new AbstractAction() {
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
    private void step() {
	multimatch = 0;
	if (partrow == 0) {
	    fill(toprow);
	}
	partrow++;
	if (partrow == cellsize) {
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
	    if (iwarn >= Colours.length) {
		iwarn = 0;
	    }
	    bgcolor = Colours[iwarn];
	} else {
	    bgcolor = Colours[0];
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
	    iscore *=2;
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
	level++;
	delay -= 3;
	timer.setDelay(delay);
	ipanel.setSpeed(level);
    }

    private void fill(int irow) {
	for (int i = 0; i < ncolumns; i++) {
	    cells[i][irow] = newCell();
	}
    }

    public void newGame() {
	stopLoop();
	level = START_LEVEL;
	delay = START_DELAY;
	bgcolor = Colours[0];
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
	for (int i = 0; i < ncolumns-2; i++) {
	    for (int j = 0; j < nrows; j++) {
		if (j == toprow) {
		    continue;
		}
		if (cells[i][j] != 0 &&
			    cells[i][j] == cells[i+1][j] &&
			    cells[i][j] == cells[i+2][j]) {
		    clearHmatch(i, j, 10);
		}
	    }
	}
	// vertical rows
	for (int i = 0; i < ncolumns; i++) {
	    for (int j = 0; j < nrows; j++) {
		// for (int j = nrows - 1; j >= 0; j--) {
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
		if (cells[i][j] != 0 &&
			    cells[i][j] == cells[i][row1] &&
			    cells[i][j] == cells[i][row2]) {
		    clearVmatch(i, j, 10);
		}
	    }
	}
    }

    private void addScore(int i) {
	score += i*(multimatch + 1);
	multimatch++;
	ipanel.setScore(score);
    }

    // MouseListener

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
	if (!active) {
	    return;
	}
	int colnew = -4;
	int rownew = -4;
	int x = e.getX();
	int y = e.getY();
	for (int i = 0; i < ncolumns; i++) {
	    int istart = i*cellsize;
	    if (x >= istart && x < (istart+cellsize)) {
		colnew = i;
	    }
	    for (int j = 0; j < nrows; j++) {
		int jstart = (j-toprow)*cellsize - partrow;
		if (jstart < 0) {
		    jstart += nrows*cellsize;
		}
		if (y >= jstart && y < (jstart+cellsize)) {
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
	if (rowclick == rownew) {
	    if (colclick == colnew+1 || colclick == colnew-1 ) {
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
	}
	// save this one for next time
	rowclick = rownew;
	colclick = colnew;
    }

    // ActionListener

    public void actionPerformed(ActionEvent e) {
	if (active) {
	    step();
	}
    }

    @Override
    public void paint(Graphics g) {
	Graphics2D g2 = (Graphics2D) g;
	Dimension d = getSize();
	g2.setPaint(bgcolor);
	g2.fill(new Rectangle2D.Double(0.0d, 0.0d, d.width, d.height));
	for (int i = 0; i < ncolumns; i++) {
	    int istart = i*cellsize + 1;
	    for (int j = 0; j < nrows; j++) {
		if (cells[i][j] > 0) {
		    int jstart = (j-toprow)*cellsize - partrow;
		    if (jstart < 0) {
			jstart += nrows*cellsize;
		    }
		    jstart++;
		    // the partially visible row is shaded
		    g2.setPaint((j == toprow) ? Colours[cells[i][j]].darker() : Colours[cells[i][j]]);
		    g2.fill(new Rectangle2D.Double(istart, jstart, cellp, cellp));
		}
	    }
	}
    }
}