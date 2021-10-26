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
    ArrayList<Enemy> enemies;
    ArrayList<Platform> platforms;
    ArrayList<Effect> effects;

	public GameBoard(boolean mode) {
        character = new MainCharacter();
        this.mode = mode;
        dr = new dollar.DollarRecognizer();
        currentStroke = null;
        enemies = new ArrayList<>();
        platforms = new ArrayList<>();
        effects = new ArrayList<>();

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

        //draw the enemies, platforms, and effects (doesn't matter what mode we're in)
        g2d.setColor(Color.red);
        for (int i = 0; i < enemies.size(); i++) {
            Rectangle b = enemies.get(i).getBoundingBox();
            if (b.getWidth() >= b.getHeight()) {
                g2d.fillOval((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getWidth());
            }
            else {
                g2d.fillOval((int) b.getX(), (int) b.getY(), (int) b.getHeight(), (int) b.getHeight());
            }
        }

        g2d.setColor(new Color(150, 75, 0)); //brown
        for (int i = 0; i < platforms.size(); i++) {
            Rectangle b = platforms.get(i).getBoundingBox();
            g2d.fillRect((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
        }

        for (int i = 0; i < effects.size(); i++) {
            if (effects.get(i).getEffect()) {
                g2d.setColor(Color.yellow);
            }
            else {
                g2d.setColor(Color.black);
            }
            Rectangle b = effects.get(i).getBoundingBox();
            //bottom left, bottom right, top midpoint
            int[] xPoints = new int[] {(int)b.getX(), (int)(b.getX() + b.getWidth()), (int)(b.getX() + (b.getWidth()/2.0))};
            int[] yPoints = new int[] {(int)(b.getY() + b.getHeight()), (int)(b.getY() + b.getHeight()), (int)b.getY()};
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
        g2d.setColor(Color.black);

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
	        if (!mode) {
	            currentStroke = new ArrayList<>();
	            repaint();
            }
        }

        public void mouseReleased(MouseEvent e) {
	        if (!mode && currentStroke.size() > 0) {
                //pass current stroke to the recognizer
                dollar.Result r = dr.recognize(currentStroke);
                String name = r.getName();
                //act on whatever the recognized template is
                if (name.equals("triangle")) {
                    System.out.println("Triangle Detected!!");
                    effects.add(new Effect(r.getBoundingBox(), true));
                } else if (name.equals("circle")) {
                    System.out.println("Circle Detected!!");
                    enemies.add(new Enemy(r.getBoundingBox()));
                } else if (name.equals("rectangle")) {
                    System.out.println("Rectangle Detected!!");
                    platforms.add(new Platform(r.getBoundingBox()));
                }
                currentStroke = null;
                repaint();
            }
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
            if (!mode) {
                currentStroke.add(e.getPoint());
                repaint();
            }
        }

        public void mouseMoved(MouseEvent e) {

        }
    }
}