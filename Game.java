import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game {
	private JFrame frame;
	private boolean mode; //true == play mode, false == author mode
	private boolean editorMode = true; // default true == drawing mode, false == dragging mode
	private JMenuBar menuBar;
	private JMenu modeMenu;
	private JRadioButtonMenuItem authorModeMenuItem;
	private JRadioButtonMenuItem playModeMenuItem;
	private ButtonGroup modeButtonGroup;
	private JPanel sidePanel;
	private JRadioButton drawingModeButton;
	private JRadioButton draggingModeButton;
	private ButtonGroup sidePanelGroup;
	private GameBoard gameBoard;

	public Game() {
		frame = new JFrame();
		mode = false;
		menuBar = new JMenuBar();
		modeMenu = new JMenu("Prototyper Mode");
		authorModeMenuItem = new JRadioButtonMenuItem("Author", true);
		playModeMenuItem = new JRadioButtonMenuItem("Play", false);
		modeButtonGroup = new ButtonGroup();

		sidePanel = new JPanel();
		sidePanel.setPreferredSize(new Dimension(200, 300));
		sidePanel.setBackground(Color.GRAY);
		drawingModeButton = new JRadioButton("Drawing Mode", true);
		draggingModeButton = new JRadioButton("Dragging Mode", false);
		sidePanelGroup = new ButtonGroup();

		authorModeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				toggleMode();
				sidePanel.add(drawingModeButton);
				sidePanel.add(draggingModeButton);
				sidePanel.setBackground(Color.GRAY);
				System.out.println("1gameboard's mode is " + gameBoard.getMode());
				System.out.println("1gameboard's editormode is " + gameBoard.getEditorMode());
			}
		});
		playModeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				toggleMode();
				for (Component j: sidePanel.getComponents()) {
					sidePanel.remove(j);
				}
				sidePanel.setBackground(Color.BLACK);
				System.out.println("2gameboard's mode is " + gameBoard.getMode());
				System.out.println("2gameboard's editormode is " + gameBoard.getEditorMode());
			}
		});

		drawingModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				toggleEditorMode();
				System.out.println("3gameboard's mode is " + gameBoard.getMode());
				System.out.println("3gameboard's editormode is " + gameBoard.getEditorMode());
			}
		});
		draggingModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				toggleEditorMode();
				System.out.println("4gameboard's mode is " + gameBoard.getMode());
				System.out.println("4gameboard's editormode is " + gameBoard.getEditorMode());
			}
		});

		modeButtonGroup.add(authorModeMenuItem);
		modeButtonGroup.add(playModeMenuItem);
		modeMenu.add(authorModeMenuItem);
		modeMenu.add(playModeMenuItem);
		menuBar.add(modeMenu);

		sidePanelGroup.add(drawingModeButton);
		sidePanelGroup.add(draggingModeButton);
		sidePanel.add(drawingModeButton);
		sidePanel.add(draggingModeButton);

		gameBoard = new GameBoard(mode, editorMode);
		gameBoard.setSize(700, 300);
		gameBoard.setPreferredSize(new Dimension(700, 300));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(900, 300));
		frame.setLayout(new FlowLayout());
		frame.setJMenuBar(menuBar);
		frame.add(gameBoard);
		frame.add(sidePanel);
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

	private void toggleEditorMode() {
		if (editorMode) {
			editorMode = false;
			//toggle from play mode to author mode
			gameBoard.setEditorMode(editorMode);
		}
		else {
			editorMode = true;
			//toggle from author mode to play mode
			gameBoard.setEditorMode(editorMode);
		}
		frame.repaint();
	}
}