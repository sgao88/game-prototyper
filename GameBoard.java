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
    boolean canMove;

	public GameBoard(boolean mode) {
        character = new MainCharacter();
        this.mode = mode;
        dr = new dollar.DollarRecognizer();
        currentStroke = null;
        enemies = new ArrayList<>();
        platforms = new ArrayList<>();
        effects = new ArrayList<>();
        canMove = true;

        this.addKeyListener(new KeyActionListener());
        this.addMouseListener(new MousePressReleaseListener());
        this.addMouseMotionListener(new MouseDragListener());
        setFocusable(true); //let's you move left/right when you press keys
        time = new Timer(5, this); //update image every 5 milliseconds
        time.start(); //runs methods for actionPerformed
    }

    public void actionPerformed(ActionEvent e) {
	    if (mode) { checkCollisions(); }
	    //if (canMove) { character.move(); }
	    repaint();
    }

    public void paint(Graphics g) {
	    super.paint(g);
	    Graphics2D g2d = (Graphics2D) g;

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
            if (effects.get(i).getVisible()) {
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
        }
        //draw main character
        g2d.setColor(new Color(0, 100, 0));
        g2d.fillOval((int)character.getBounds().getX(), (int)character.getBounds().getY(), (int)character.getBounds().getWidth(), (int)character.getBounds().getHeight());

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

    public void checkCollisions() {
	    Rectangle item;
	    for (int i = 0; i < enemies.size(); i++) {
	        item = enemies.get(i).getBoundingBox();
	        if (bump(character.getBounds(), item)) {
	            canMove = false;
            }
        }
	    for (int i = 0; i < platforms.size(); i++) {
	        item = platforms.get(i).getBoundingBox();
            if (bump(character.getBounds(), item)) {
                canMove = false;
            }
        }
	    for (int i = 0; i < effects.size(); i++) {
	        item = effects.get(i).getBoundingBox();
            if (bump(character.getBounds(), item)) {
                effects.get(i).setVisible(false);
            }
        }
    }

    public boolean bump(Rectangle one, Rectangle two) {
        //right edge of character is within bounds of the item's box
        if (two.getX() <= one.getX() + one.getWidth() && one.getX() + one.getWidth() <= two.getX() + two.getWidth()) {
            //top edge of character is within bounds of item's box
            if (two.getY() <= one.getY() + one.getHeight() && one.getY() + one.getHeight() <= two.getY() + two.getHeight()) {
                return true;
            }
            //bottom edge of character is within bounds of item's box
            if (two.getY() <= one.getY() && one.getY() <= two.getY() + two.getHeight()) {
                return true;
            }
            //top edge of character is above item and bottom edge of character is below item
            if (one.getY() <= two.getY() && one.getY() + one.getHeight() >= two.getY() + two.getHeight()) {
                return true;
            }
        }
        return false;
    }

    private class KeyActionListener extends KeyAdapter{
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            int dx = 0;
            if (key == KeyEvent.VK_LEFT && canMove) {
                dx = -4; //This can be changed to speed up/slow down game. Larger dx == faster scrolling
            }
            else if (key == KeyEvent.VK_RIGHT && canMove) {
                dx = 4;
            }
            //move each enemy, platform, and effect based on the key press
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).move(dx);
            }
            for (int i = 0; i < platforms.size(); i++) {
                platforms.get(i).move(dx);
            }
            for (int i = 0; i < effects.size(); i++) {
                effects.get(i).move(dx);
            }
        }

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            int dx = 0;
            if (key == KeyEvent.VK_LEFT) {
                dx = 0;
            } else if (key == KeyEvent.VK_RIGHT) {
                dx = 0;
            }
            //stop moving all enemies, platforms, and effects
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).move(dx);
            }
            for (int i = 0; i < platforms.size(); i++) {
                platforms.get(i).move(dx);
            }
            for (int i = 0; i < effects.size(); i++) {
                effects.get(i).move(dx);
            }
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
                    effects.add(new Effect(r.getBoundingBox(), true));
                } else if (name.equals("circle")) {
                    enemies.add(new Enemy(r.getBoundingBox()));
                } else if (name.equals("rectangle")) {
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