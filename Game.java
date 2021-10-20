import javax.swing.*;
import java.awt.*;

public class Game {
	private JFrame frame;
	private boolean mode; //true == play mode, false == author mode
	private JLabel tempLabel; //label to show play/author mode

	public Game() {
		frame = new JFrame();
		mode = false;
		tempLabel = new JLabel("Author Mode");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(700, 250)); 
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
}

//QUESTIONS
//Should we initialize in author mode or play mode? or should we have an intro screen that lets them choose?
//What do you think the defualt/minimum size should be for the screen? 