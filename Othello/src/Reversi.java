
package Lecture_21.Reversi_game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Lecture_13.Queues.QueueUsingLinkedLists;
import Lecture_13.common.IQueue;

public class Reversi extends JFrame implements ActionListener {
	public static final int BOARD_SIZE = 8;
	public static final int B_WINS = 0;
	public static final int W_WINS = 1;
	public static final int TIE = 2;
	public static final int ALLOW = 3;
	public static final int PASSABLE = 4;
	public static final String HIGHLIGHTER = (char) 42 + "";
	public static final Color DEFAULT_COLOR = (new JButton().getBackground());

	private boolean passed;
	private boolean blackTurn = true;
	private JButton[][] buttonsArray;
	private ArrayList<JButton> dottedButtons;

	public Reversi() {
		super.setTitle("REVERSI");

		super.setSize(600, 600);
		super.setResizable(false);

		GridLayout layout = new GridLayout(8, 8);
		super.setLayout(layout);
		this.buttonsArray = new JButton[BOARD_SIZE][BOARD_SIZE];
		this.dottedButtons = new ArrayList<>();

		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				JButton button = new JButton();

				if ((row == BOARD_SIZE / 2 && col == BOARD_SIZE / 2)
						|| (row == BOARD_SIZE / 2 - 1 && col == BOARD_SIZE / 2 - 1)) {
					button.setBackground(Color.WHITE);
				} else if ((row == BOARD_SIZE / 2 && col == BOARD_SIZE / 2 - 1)
						|| (row == BOARD_SIZE / 2 - 1 && col == BOARD_SIZE / 2)) {
					button.setBackground(Color.BLACK);
				} else {
					button.setBackground(DEFAULT_COLOR);
				}

				button.setForeground(Color.ORANGE);
				button.setFont(new Font(null, 1, 50));
				button.addActionListener(this);

				this.buttonsArray[row][col] = button;
				super.add(button);
			}
		}

		this.getGameStatus();
		super.setVisible(true);
		JOptionPane.showMessageDialog(null, "Welcome to Reversi", "Welcome", JOptionPane.INFORMATION_MESSAGE);
	}

	public void startGame() {
		this.passed = false;
		this.blackTurn = true;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JButton button = (JButton) arg0.getSource();

		if (button.getBackground() == DEFAULT_COLOR && button.getText().equals(HIGHLIGHTER)) {
			this.removeDots();
			this.color(button);
			this.blackTurn = !this.blackTurn;
			int gameStatus = this.getGameStatus();

			if (gameStatus == PASSABLE) {
				JOptionPane.showMessageDialog(null, "NO MOVE... Passed");
				this.blackTurn = !this.blackTurn;
				gameStatus = this.getGameStatus();
			}
			if (gameStatus == ALLOW) {
				this.passed = false;
			} else {
				this.declareWinner(gameStatus);
				super.dispose();
			}

		} else {
			JOptionPane.showMessageDialog(null, "Invalid Move");
		}

	}

	private void removeDots() {
		while (this.dottedButtons.size() != 0) {
			this.dottedButtons.remove(this.dottedButtons.size() - 1).setText("");
		}
	}

	private void color(JButton button) {
		int row = 0, col = 0;
		for (; row < this.buttonsArray.length; row++) {
			for (col = 0; col < this.buttonsArray.length; col++) {
				if (this.buttonsArray[row][col] == button) {
					break;
				}
			}
			if (col != this.buttonsArray.length) {
				break;
			}
		}

		if (blackTurn) {
			this.colorinALLdirections(row, col, Color.BLACK);
			button.setBackground(Color.BLACK);
		} else {
			this.colorinALLdirections(row, col, Color.WHITE);
			button.setBackground(Color.WHITE);
		}
	}

	private void colorinALLdirections(int row, int col, Color c) {
		this.colorinSINGLEdirections(row, col + 1, 0, 1, c);
		this.colorinSINGLEdirections(row + 1, col + 1, 1, 1, c);
		this.colorinSINGLEdirections(row + 1, col, 1, 0, c);
		this.colorinSINGLEdirections(row + 1, col - 1, 1, -1, c);
		this.colorinSINGLEdirections(row, col - 1, 0, -1, c);
		this.colorinSINGLEdirections(row - 1, col - 1, -1, -1, c);
		this.colorinSINGLEdirections(row - 1, col, -1, 0, c);
		this.colorinSINGLEdirections(row - 1, col + 1, -1, 1, c);
	}

	private boolean colorinSINGLEdirections(int row, int col, int rowinc, int colinc, Color c) {
		if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
			return false;
		}
		if (this.buttonsArray[row][col].getBackground() == DEFAULT_COLOR) {
			return false;
		}
		if (this.buttonsArray[row][col].getBackground() == c) {
			return true;
		}

		boolean ans = this.colorinSINGLEdirections(row + rowinc, col + colinc, rowinc, colinc, c);
		if (ans) {
			this.buttonsArray[row][col].setBackground(c);
		}

		return ans;
	}

	private int getGameStatus() {
		Color color;
		if (this.blackTurn) {
			color = Color.BLACK;
		} else {
			color = Color.WHITE;
		}

		int Bcount = 0, Wcount = 0;

		for (int row = 0; row < this.buttonsArray.length; row++) {
			for (int col = 0; col < this.buttonsArray.length; col++) {

				if (this.buttonsArray[row][col].getBackground() == color) {
					this.putdotinALLdirection(row, col, color);
				}

				if (this.buttonsArray[row][col].getBackground() == Color.BLACK) {
					Bcount++;
				}
				if (this.buttonsArray[row][col].getBackground() == Color.WHITE) {
					Wcount++;
				}
			}
		}

		if (this.dottedButtons.size() != 0) {
			return ALLOW;
		}

		if (!passed) {
			if (Bcount + Wcount != BOARD_SIZE * BOARD_SIZE) {
				return PASSABLE;
			}
		}

		if (Bcount > Wcount) {
			return B_WINS;
		} else if (Bcount < Wcount) {
			return W_WINS;
		} else {
			return TIE;
		}
	}

	private void putdotinALLdirection(int row, int col, Color c) {
		this.putdotinSINGLEdirection(row, col + 1, 0, 1, c);
		this.putdotinSINGLEdirection(row + 1, col + 1, 1, 1, c);
		this.putdotinSINGLEdirection(row + 1, col, 1, 0, c);
		this.putdotinSINGLEdirection(row + 1, col - 1, 1, -1, c);
		this.putdotinSINGLEdirection(row, col - 1, 0, -1, c);
		this.putdotinSINGLEdirection(row - 1, col - 1, -1, -1, c);
		this.putdotinSINGLEdirection(row - 1, col, -1, 0, c);
		this.putdotinSINGLEdirection(row - 1, col + 1, -1, 1, c);
	}

	private void putdotinSINGLEdirection(int row, int col, int rowinc, int colinc, Color c) {
		if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
			return;
		}
		if (this.buttonsArray[row][col].getBackground() == c) {
			return;
		}

		if (this.buttonsArray[row][col].getBackground() == DEFAULT_COLOR) {
			if (this.buttonsArray[row - rowinc][col - colinc].getBackground() == c) {
				return;
			}
			if(this.blackTurn){
				this.buttonsArray[row][col].setForeground(Color.DARK_GRAY);
			}
			else{
				this.buttonsArray[row][col].setForeground(Color.LIGHT_GRAY);
			}
			this.buttonsArray[row][col].setText(HIGHLIGHTER);
			this.dottedButtons.add(this.buttonsArray[row][col]);
			return;
		}

		this.putdotinSINGLEdirection(row + rowinc, col + colinc, rowinc, colinc, c);
	}

	private void declareWinner(int gameStatus) {
		if (gameStatus == B_WINS) {
			JOptionPane.showMessageDialog(null, "Black Wins.");
		} else if (gameStatus == W_WINS) {
			JOptionPane.showMessageDialog(null, "White Wins.");
		} else {
			JOptionPane.showMessageDialog(null, "TIE");
		}
	}

}
