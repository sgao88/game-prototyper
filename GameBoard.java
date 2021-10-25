import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;



public class GameBoard extends JPanel implements ActionListener{
    MainCharacter character;
    Timer time;
    boolean mode;
    dollar.DollarRecognizer dr;
    ArrayList<Point2D> currentStroke;

	public GameBoard(boolean mode) {
        character = new MainCharacter();
        this.mode = mode;
        dr = new dollar.DollarRecognizer();
        currentStroke = null;
        this.addKeyListener(new KeyActionListener());
        this.addMouseListener(new MousePressReleaseListener());
        this.addMouseMotionListener(new MouseDragListener());
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
        g2d.setColor(new Color(0, 100, 0));
        g2d.fillOval(x, y, radius, radius);

        //keep at end of the method so stroke is drawn on top of the other objects
        g2d.setColor(Color.black);
        if (currentStroke != null && !mode) { //only draw and recognize the stroke if we are in author mode
            //iterate over the point array and draw lines between each points
            for (int i = 0; i < currentStroke.size() - 1; i++) {
                g2d.drawLine((int)currentStroke.get(i).getX(), (int)currentStroke.get(i).getY(), (int)currentStroke.get(i + 1).getX(), (int)currentStroke.get(i + 1).getY());
            }
        }
    }

    public void setMode(boolean m) {
	    mode = m;
    }

    private class KeyActionListener extends KeyAdapter{
	    public void keyReleased(KeyEvent e) {
	        character.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            character.keyPressed(e);
        }
    }

    private class MousePressReleaseListener implements MouseListener {
	    public void mousePressed(MouseEvent e) {
	        currentStroke = new ArrayList<>();
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
	        //pass current stroke to the recognizer
            dollar.Result r = dr.recognize(currentStroke);
            String name = r.getName();
            //act on whatever the recognized template is
            if (name.equals("triangle")) {
                System.out.println("Triangle Detected!!");
            }
            else if (name.equals("circle")) {
                System.out.println("Circle Detected!!");
            }
            else if (name.equals("rectangle")) {
                System.out.println("Rectangle Detected!!");
            }
            currentStroke = null;
            repaint();
        }

        public void mouseClicked(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }
    }

    public class MouseDragListener implements MouseMotionListener {
        public void mouseDragged(MouseEvent e) {
            currentStroke.add(e.getPoint());
            repaint();
        }

        public void mouseMoved(MouseEvent e) {

        }
    }
}