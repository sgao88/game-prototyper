import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Game implements ActionListener{
	private JFrame frame;
	private Timer time;
	private boolean mode; //true == play mode, false == author mode
	private boolean editorMode = true; // default true == drawing mode, false == dragging mode
	private JMenuBar menuBar;
	private JMenu modeMenu;
	private JRadioButtonMenuItem authorModeMenuItem;
	private JRadioButtonMenuItem playModeMenuItem;
	private JLabel gameModeLabel;
	private ButtonGroup modeButtonGroup;
	private JPanel sidePanel;
	private JRadioButton drawingModeButton;
	private JRadioButton draggingModeButton;
	private JRadioButton animatingModeButton;
	private ButtonGroup sidePanelGroup;
	private JLabel scoreLabel;
	private JButton newPlatformButton;
	private JButton newEnemyButton;
	private JButton newRewardButton;
	private JButton newPenaltyButton;
	private GameBoard gameBoard;

	public Game() {
		frame = new JFrame();
		mode = false;
		menuBar = new JMenuBar();
		modeMenu = new JMenu("Game Mode");
		authorModeMenuItem = new JRadioButtonMenuItem("Author", true);
		playModeMenuItem = new JRadioButtonMenuItem("Play", false);
		modeButtonGroup = new ButtonGroup();

		gameBoard = new GameBoard(mode, editorMode);
		gameBoard.setSize(700, 300);
		gameBoard.setPreferredSize(new Dimension(700, 300));

		sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
		sidePanel.setPreferredSize(new Dimension(200, 300));
		sidePanel.setBackground(Color.white);
		sidePanel.setBorder(new EmptyBorder(10,10,10,10));
		gameModeLabel = new JLabel("AUTHOR");
		gameModeLabel.setFont(new Font("San Serif", Font.BOLD, 20));
		gameModeLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
		scoreLabel = new JLabel("Score: " + gameBoard.getScore());
		scoreLabel.setVisible(false);
		drawingModeButton = new JRadioButton("Drawing Mode", true);
		draggingModeButton = new JRadioButton("Dragging Mode", false);
		animatingModeButton = new JRadioButton("Animation Mode", false);
		animatingModeButton.setBorder(new EmptyBorder(0, 0, 10, 0));
		sidePanelGroup = new ButtonGroup();
		newPlatformButton = new JButton("Add Platform");
		newEnemyButton = new JButton("Add Enemy");
		newRewardButton = new JButton("Add Reward");
		newPenaltyButton = new JButton("Add Penalty");
		gameModeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		drawingModeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		draggingModeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		animatingModeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newPlatformButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newEnemyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newRewardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newPenaltyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		authorModeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				toggleMode();
				gameModeLabel.setText("AUTHOR");
				scoreLabel.setVisible(false);
				drawingModeButton.setVisible(true);
				draggingModeButton.setVisible(true);
				animatingModeButton.setVisible(true);
				newPlatformButton.setVisible(true);
				newEnemyButton.setVisible(true);
				newRewardButton.setVisible(true);
				newPenaltyButton.setVisible(true);
				System.out.println("1gameboard's mode is " + gameBoard.getMode());
				System.out.println("1gameboard's editormode is " + gameBoard.getEditorMode());
			}
		});
		playModeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				toggleMode();
				drawingModeButton.setVisible(false);
				draggingModeButton.setVisible(false);
				animatingModeButton.setVisible(false);
				newPlatformButton.setVisible(false);
				newEnemyButton.setVisible(false);
				newRewardButton.setVisible(false);
				newPenaltyButton.setVisible(false);
				gameModeLabel.setText("PLAY");
				scoreLabel.setVisible(true);
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
		animatingModeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				System.out.println("Need to implement this action listener for the animation mode");
			}
		});

		newPlatformButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				gameBoard.addPlatform();
			}
		});
		newEnemyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				gameBoard.addEnemy();
			}
		});
		newRewardButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				gameBoard.addEffect(true);
			}
		});
		newPenaltyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				gameBoard.addEffect(false);
			}
		});

		modeButtonGroup.add(authorModeMenuItem);
		modeButtonGroup.add(playModeMenuItem);
		modeMenu.add(authorModeMenuItem);
		modeMenu.add(playModeMenuItem);
		menuBar.add(modeMenu);

		sidePanelGroup.add(drawingModeButton);
		sidePanelGroup.add(draggingModeButton);
		sidePanelGroup.add(animatingModeButton);
		sidePanel.add(gameModeLabel);
		sidePanel.add(scoreLabel);
		sidePanel.add(drawingModeButton);
		sidePanel.add(draggingModeButton);
		sidePanel.add(animatingModeButton);
		sidePanel.add(newPlatformButton);
		sidePanel.add(newEnemyButton);
		sidePanel.add(newRewardButton);
		sidePanel.add(newPenaltyButton);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(900, 300));
		frame.setLayout(new FlowLayout());
		frame.setJMenuBar(menuBar);
		frame.add(gameBoard);
		frame.add(sidePanel);
		frame.pack();
		frame.setVisible(true);

		time = new Timer(5, this); //update image every 5 milliseconds
		time.start(); //runs methods for actionPerformed
	}

	public void actionPerformed(ActionEvent e) {
		scoreLabel.setText("Score: " + gameBoard.getScore());
		frame.repaint();
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