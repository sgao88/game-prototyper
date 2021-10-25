import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameBoard extends JPanel implements ActionListener{
    MainCharacter character;
    Timer time;

	public GameBoard() {
        character = new MainCharacter();
        this.addKeyListener(new BoardActionListener());
        setFocusable(true); //let's you move left/right when you press keys
        time = new Timer(5, this); //update image every 5 milliseconds
        time.start(); //runs methods for actionPerformed
    }

    public void actionPerformed(ActionEvent e) {
	    character.move();
	    repaint();
    }

    public void paint(Graphics g) {
	    super.paint(g);
	    Graphics2D g2d = (Graphics2D) g;
	    int x = character.getX();
	    int y = character.getY();
	    int radius = character.getRadius();
        x = x-(radius/2);
        y = y-(radius/2);
        g2d.setColor(Color.BLUE);
        g2d.fillOval(x, y, radius, radius);
    }

    private class BoardActionListener extends KeyAdapter {
	    public void keyReleased(KeyEvent e) {
            character.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            character.keyPressed(e);
        }
    }
}