import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class GameBoard extends JPanel implements ActionListener{
    MainCharacter character;
    Timer time;
    boolean mode;
    boolean editorMode;
    dollar.DollarRecognizer dr;
    ArrayList<Point2D> currentStroke;
    ArrayList<Enemy> enemies;
    ArrayList<Platform> platforms;
    ArrayList<Effect> effects;
    ArrayList<DrawnObject> allObjects;
    boolean canMove;
    boolean dragging;
    Point currPoint;
    DrawnObject curr;

	public GameBoard(boolean mode, boolean editorMode) {
        character = new MainCharacter();
        this.mode = mode;
        this.editorMode = editorMode;
        dr = new dollar.DollarRecognizer();
        currentStroke = null;
        enemies = new ArrayList<>();
        platforms = new ArrayList<>();
        effects = new ArrayList<>();
        allObjects = new ArrayList<>();
        canMove = true;

        this.addKeyListener(new KeyActionListener());
        this.addMouseListener(new MousePressReleaseListener());
        this.addMouseMotionListener(new MouseDragListener());
        setFocusable(true); //lets you move left/right when you press keys
        this.requestFocus();
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
        if (m = true) {
            this.requestFocus();
        }
    }

    public void setEditorMode(boolean m) {
        editorMode = m;
    }

    public boolean getMode() { return mode; }

    public boolean getEditorMode() { return editorMode; }

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

    private class KeyActionListener extends KeyAdapter {
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
            for (DrawnObject obj : allObjects) {
                obj.move(dx);
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
            for (DrawnObject obj : allObjects) {
                obj.move(dx);
            }
        }
    }

    private boolean within(DrawnObject obj, Point p) {
        if (obj.getBoundingBox().contains(p)) {
            return true;
        } else {
            return false;
        }
    }

    private class MousePressReleaseListener implements MouseListener {
	    public void mousePressed(MouseEvent e) {
	        if (!mode && editorMode) {
	            currentStroke = new ArrayList<>();
	            repaint();
            } else if (!mode) {
                // mouse press to enter dragging object mode, !editorMode implied
                currPoint = e.getPoint();
                if (curr == null && allObjects.size() > 0) {
                    for (DrawnObject obj : allObjects) {
                        if (within(obj, e.getPoint())) {
                            // existing event drag
                            curr = obj;
                            return;
                        }
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
	        if (!mode && editorMode && currentStroke.size() > 0) {
                //pass current stroke to the recognizer
                dollar.Result r = dr.recognize(currentStroke);
                String name = r.getName();
                //act on whatever the recognized template is
                if (name.equals("triangle")) {
                    Effect temp = new Effect(r.getBoundingBox(), true);
                    effects.add(temp);
                    allObjects.add(temp);
                } else if (name.equals("circle")) {
                    Enemy temp = new Enemy(r.getBoundingBox());
                    enemies.add(temp);
                    allObjects.add(temp);
                } else if (name.equals("rectangle")) {
                    Platform temp = new Platform(r.getBoundingBox());
                    platforms.add(temp);
                    allObjects.add(temp);
                }
                currentStroke = null;
                repaint();
            } else if (!mode && !editorMode && curr != null && dragging) {
                // mouse released to exit dragging object mode
                dragging = false;
                currPoint = e.getPoint();
                // final update for end location
                int xDiff = curr.getBoundingBox().x - currPoint.x;
                int yDiff = curr.getBoundingBox().y - currPoint.y;
                curr.move(xDiff);
                curr.moveY(yDiff);
                curr = null;
                currPoint = null;
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
            if (!mode && editorMode) {
                currentStroke.add(e.getPoint());
                repaint();
            } else if (!mode && curr != null) {
                // mouse dragging for moving objects, !editorMode implied
                dragging = true;
                // work off of curr value
                currPoint = e.getPoint();
                int xDiff = curr.getBoundingBox().x - currPoint.x;
                int yDiff = curr.getBoundingBox().y - currPoint.y;
                curr.move(xDiff);
                curr.moveY(yDiff);
                repaint();
            }
        }

        public void mouseMoved(MouseEvent e) {
        }
    }
}