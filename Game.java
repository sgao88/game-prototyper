import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game {
	private JFrame frame;
	private boolean mode; //true == play mode, false == author mode
	private JMenuBar menuBar;
	private JMenu modeMenu;
	private JRadioButtonMenuItem authorModeMenuItem;
	private JRadioButtonMenuItem playModeMenuItem;
	private ButtonGroup modeButtonGroup;
	private GameBoard gameBoard;

	public Game() {
		frame = new JFrame();
		mode = false;
		menuBar = new JMenuBar();
		modeMenu = new JMenu("Mode");
		authorModeMenuItem = new JRadioButtonMenuItem("Author", true);
		playModeMenuItem = new JRadioButtonMenuItem("Play", false);
		modeButtonGroup = new ButtonGroup();

		authorModeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				toggleMode();
			}
		});
		playModeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				toggleMode();
			}
		});

		modeButtonGroup.add(authorModeMenuItem);
		modeButtonGroup.add(playModeMenuItem);
		modeMenu.add(authorModeMenuItem);
		modeMenu.add(playModeMenuItem);
		menuBar.add(modeMenu);

		gameBoard = new GameBoard(mode);
		gameBoard.setSize(700, 250);
		gameBoard.setPreferredSize(new Dimension(700, 250));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(700, 250)); 
		frame.setJMenuBar(menuBar);
		frame.add(gameBoard);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Game game = new Game();
			}
		});
	}

	private void toggleMode() {
		if (mode) {
			mode = false;
			//toggle from play mode to author mode
			gameBoard.setMode(mode);
		}
		else {
			mode = true;
			//toggle from author mode to play mode
			gameBoard.setMode(mode);
		}
		frame.repaint();
	}
}