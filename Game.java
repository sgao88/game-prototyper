import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.border.*;

public class Game implements ActionListener{
	private JFrame frame;
	private Timer time;
	private boolean mode; //true == play mode, false == author mode
	private int editorMode; // 0 = drawing, 1 = dragging, 2 = animating
	private JMenuBar menuBar;
	private JMenu modeMenu;
	private JRadioButtonMenuItem authorModeMenuItem;
	private JRadioButtonMenuItem playModeMenuItem;
	private JLabel statusBar;
	private JPanel gamePanel;
	private JLabel gameModeLabel;
	private ButtonGroup modeButtonGroup;
	private JPanel sidePanel;
	private ButtonGroup sidePanelGroup;
	private JLabel scoreLabel;
	private JButton newPlatformButton;
	private JButton newEnemyButton;
	private JButton newRewardButton;
	private JButton newPenaltyButton;
	private JRadioButton rewardRadioButton;
	private JRadioButton penaltyRadioButton;
	private ButtonGroup effectSwitchGroup;
	private JPanel costPanel;
	private JLabel costLabel;
	private static JTextField costTextField;
	private JButton deleteButton;
	private GameBoard gameBoard;

	public Game() {
		frame = new JFrame();
		mode = false;
		menuBar = new JMenuBar();
		modeMenu = new JMenu("Game Mode");
		authorModeMenuItem = new JRadioButtonMenuItem("Author", true);
		playModeMenuItem = new JRadioButtonMenuItem("Play", false);
		modeButtonGroup = new ButtonGroup();
		statusBar = new JLabel("");
		statusBar.setHorizontalAlignment(JLabel.CENTER);
		gamePanel = new JPanel();

		gameBoard = new GameBoard(mode, editorMode);
		gameBoard.setSize(700, 300);
		gameBoard.setPreferredSize(new Dimension(700, 300));
		gameBoard.setMinimumSize(new Dimension(700, 300));

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
		sidePanelGroup = new ButtonGroup();
		newPlatformButton = new JButton("Add Platform");
		newEnemyButton = new JButton("Add Enemy");
		newRewardButton = new JButton("Add Reward");
		newPenaltyButton = new JButton("Add Penalty");
		rewardRadioButton = new JRadioButton("Reward");
		penaltyRadioButton = new JRadioButton("Penalty");
		effectSwitchGroup = new ButtonGroup();
		costPanel = new JPanel();
		costPanel.setBackground(Color.white);
		costLabel = new JLabel("Cost");
		costTextField = new JTextField("0", 5);
		costTextField.setHorizontalAlignment(JTextField.CENTER);
		deleteButton = new JButton("Delete Shape");
		deleteButton.setBackground(Color.red);
		deleteButton.setOpaque(true);
		deleteButton.setBorderPainted(false);
		deleteButton.setForeground(Color.white);
		rewardRadioButton.setVisible(false);
		penaltyRadioButton.setVisible(false);
		costLabel.setVisible(false);
		costTextField.setVisible(false);
		deleteButton.setVisible(false);
		gameModeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		newPlatformButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newEnemyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newRewardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newPenaltyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		rewardRadioButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		penaltyRadioButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		costPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		authorModeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				toggleMode();
				gameModeLabel.setText("AUTHOR");
				scoreLabel.setVisible(false);
				newPlatformButton.setVisible(true);
				newEnemyButton.setVisible(true);
				newRewardButton.setVisible(true);
				newPenaltyButton.setVisible(true);
				rewardRadioButton.setVisible(false);
				penaltyRadioButton.setVisible(false);
				costLabel.setVisible(false);
				costTextField.setVisible(false);
				deleteButton.setVisible(false);
			}
		});
		playModeMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				toggleMode();
				newPlatformButton.setVisible(false);
				newEnemyButton.setVisible(false);
				newRewardButton.setVisible(false);
				newPenaltyButton.setVisible(false);
				gameModeLabel.setText("PLAY");
				scoreLabel.setVisible(true);
				rewardRadioButton.setVisible(false);
				penaltyRadioButton.setVisible(false);
				costLabel.setVisible(false);
				costTextField.setVisible(false);
				deleteButton.setVisible(false);
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
		rewardRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				if (!((Effect)gameBoard.getSelectedObject()).getEffect()) {
					gameBoard.switchEffect((Effect)gameBoard.getSelectedObject());
					penaltyRadioButton.setSelected(false);
					rewardRadioButton.setSelected(true);
					((Effect)gameBoard.getSelectedObject()).setEffect(true);
					frame.repaint();
				}
			}
		});
		penaltyRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				if (((Effect)gameBoard.getSelectedObject()).getEffect()) {
					gameBoard.switchEffect((Effect)gameBoard.getSelectedObject());
					penaltyRadioButton.setSelected(true);
					rewardRadioButton.setSelected(false);
					((Effect)gameBoard.getSelectedObject()).setEffect(false);
					frame.repaint();
				}
			}
		});
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent c) {
				gameBoard.deleteShape(gameBoard.getSelectedObject());
				deleteButton.setVisible(false);
				frame.repaint();
			}
		});

		costTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("triggered");
				((Effect)gameBoard.getSelectedObject()).setCost(Integer.parseInt(costTextField.getText()));
				gameBoard.curr = null;
				gameBoard.points = new ArrayList<>();
				frame.repaint();
			}
		});

		modeButtonGroup.add(authorModeMenuItem);
		modeButtonGroup.add(playModeMenuItem);
		modeMenu.add(authorModeMenuItem);
		modeMenu.add(playModeMenuItem);
		menuBar.add(modeMenu);

		sidePanel.add(gameModeLabel);
		sidePanel.add(scoreLabel);
		sidePanel.add(newPlatformButton);
		sidePanel.add(newEnemyButton);
		sidePanel.add(newRewardButton);
		sidePanel.add(newPenaltyButton);
		effectSwitchGroup.add(rewardRadioButton);
		effectSwitchGroup.add(penaltyRadioButton);
		sidePanel.add(rewardRadioButton);
		sidePanel.add(penaltyRadioButton);
		costPanel.add(costLabel);
		costPanel.add(costTextField);
		sidePanel.add(costPanel);
		sidePanel.add(deleteButton);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(900, 300));
		gamePanel.setLayout(new FlowLayout());
		gamePanel.add(gameBoard);
		gamePanel.add(sidePanel);
		frame.setLayout(new BorderLayout());
		frame.setJMenuBar(menuBar);
		frame.add(gamePanel, BorderLayout.CENTER);
		frame.add(statusBar, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);

		time = new Timer(5, this); //update image every 5 milliseconds
		time.start(); //runs methods for actionPerformed
	}

	public void actionPerformed(ActionEvent e) {
		scoreLabel.setText("Score: " + gameBoard.getScore());
		statusBar.setText("Status: " + gameBoard.getStatusUpdate());
		Object curr = gameBoard.getSelectedObject();
		if (!mode && curr != null && !(curr instanceof MainCharacter)) {
			deleteButton.setVisible(true);
			if (curr instanceof Effect) {
				if (((Effect)curr).getEffect()) {
					rewardRadioButton.setSelected(true);
				}
				else {
					penaltyRadioButton.setSelected(true);
				}
				rewardRadioButton.setVisible(true);
				penaltyRadioButton.setVisible(true);
				costLabel.setVisible(true);
				costTextField.setVisible(true);
			}
			else {
				rewardRadioButton.setVisible(false);
				penaltyRadioButton.setVisible(false);
				costLabel.setVisible(false);
				costTextField.setVisible(false);
			}
		} else {
			rewardRadioButton.setVisible(false);
			penaltyRadioButton.setVisible(false);
			costLabel.setVisible(false);
			costTextField.setVisible(false);
			deleteButton.setVisible(false);
		}
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
			gameBoard.setStatusUpdate("Game Set To Author Mode");
		}
		else {
			mode = true;
			//toggle from author mode to play mode
			gameBoard.setStatusUpdate("Game Set To Play Mode");
		}
		gameBoard.setMode(mode);
		frame.repaint();
	}

	public static void setCostTextField(int cost) {
		costTextField.setText(Integer.toString(cost));
	}
}