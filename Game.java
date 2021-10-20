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
	private JLabel tempLabel; //label to show play/author mode

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

		tempLabel = new JLabel("Author Mode");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(700, 250)); 
		frame.setJMenuBar(menuBar);
		frame.add(tempLabel);
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
			tempLabel.setText("Author Mode");
		}
		else {
			mode = true;
			tempLabel.setText("Play Mode");
		}
		frame.repaint();
	}
}

//QUESTIONS
//Should we initialize in author mode or play mode? or should we have an intro screen that lets them choose?
//What do you think the defualt/minimum size should be for the screen? 