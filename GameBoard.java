import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;

public class GameBoard extends JPanel implements ActionListener{
    MainCharacter character;
    Timer time;
    Timer animTime;
    boolean mode;
    int editorMode; //0 = drawing, 1 = dragging, 2 == animating, 3 = dragging to resize
    int resizeCorner = -1; //0 = top left, 1 = top right, 2 = bottom right, 3 = bottom left
    dollar.DollarRecognizer dr;
    ArrayList<Point2D> currentStroke;
    ArrayList<DrawnObject> allObjects;
    ArrayList<Effect> hitEffects;
    ArrayList<Rectangle> points;
    boolean canMove;
    boolean dragging;
    Point currPoint;
    Object curr;
    int score;
    int boundaryX;
    boolean draggingAndScrolling;
    boolean jumping;
    double angle = 0.0;
    String statusUpdate;
    Object temp;
    int movementDirection = -1; // 1 = right, 0 = left
    Point prevDraggingPoint;

	public GameBoard(boolean mode, int editorMode) {
        character = new MainCharacter();
        this.mode = mode;
        this.editorMode = editorMode;
        dr = new dollar.DollarRecognizer();
        currentStroke = null;
        allObjects = new ArrayList<>();
        hitEffects = new ArrayList<>();
        points = new ArrayList<>();
        canMove = true;
        score = 0;
        boundaryX = 680;
        draggingAndScrolling = false;
        jumping = false;
        statusUpdate = "Game Opened in Author Mode";

        this.addKeyListener(new KeyActionListener());
        this.addMouseListener(new MousePressReleaseListener());
        this.addMouseMotionListener(new MouseDragListener());
        setFocusable(true); //lets you move left/right when you press keys
        this.requestFocus();
        time = new Timer(5, this); //update image every 5 milliseconds
        time.start(); //runs methods for actionPerformed
    }

    public void actionPerformed(ActionEvent e) {
	    if (mode) { checkCollisions(true); }
        if (!jumping) {
            if (character.getY() < 238) {
                character.moveY(-1);
            }
        }
	    repaint();
    }

    public void paint(Graphics g) {
	    super.paint(g);
	    Graphics2D g2d = (Graphics2D) g;

        //draw the enemies, platforms, and effects (doesn't matter what mode we're in)
        for (DrawnObject obj : allObjects) {
            boolean tempHasRotation = false;
            Shape tempRotationImage = null;
            if (obj.hasAnimation() && mode) {
                obj.step();
                if (obj.getMotionTypes()[6]) {
                    // has rotation
                    tempHasRotation = true;
                    tempRotationImage = rotate(obj);
                }
            }
            Rectangle b = obj.getBoundingBox();
            if (obj instanceof Enemy) {
                if (tempHasRotation) {
                    if (curr != null && curr.equals(obj)) {
                        g2d.setColor(Color.gray);
                        double tempWidth = Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX());
                        double tempHeight = Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY());
                        if (tempWidth > tempHeight) {
                            ((DrawnObject)curr).getBoundingBox().setRect(points.get(0).getCenterX(), points.get(0).getCenterY(), Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX()), Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX()));
                        }
                        else {
                            ((DrawnObject)curr).getBoundingBox().setRect(points.get(0).getCenterX(), points.get(0).getCenterY(), Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY()), Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY()));
                        }
                        g2d.drawRect((int)b.getX(), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight());
                    }
                    g2d.setColor(obj.getColor());
                    Point circleCenter = new Point((int)(obj.getBoundingBox().getX() + (obj.getBoundingBox().getWidth()/2.0)), (int)(obj.getBoundingBox().getY() + (obj.getBoundingBox().getHeight()/2.0)));
                    Point rotationCenter = new Point((int)obj.getBoundingBox().getX(), (int)obj.getBoundingBox().getY());
                    Point newCenter = rotatePoint(circleCenter, rotationCenter);
                    g2d.fillOval((int)(newCenter.x - (obj.getBoundingBox().getWidth()/2.0)), (int)(newCenter.y - (obj.getBoundingBox().getY()/2.0)), (int)obj.getBoundingBox().getWidth(), (int)obj.getBoundingBox().getHeight());
                } else {
                    if (curr != null && curr.equals(obj)) {
                        g2d.setColor(Color.gray);
                        double tempWidth = Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX());
                        double tempHeight = Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY());
                        if (tempWidth > tempHeight) {
                            ((DrawnObject)curr).getBoundingBox().setRect(points.get(0).getCenterX(), points.get(0).getCenterY(), Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX()), Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX()));
                        }
                        else {
                            ((DrawnObject)curr).getBoundingBox().setRect(points.get(0).getCenterX(), points.get(0).getCenterY(), Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY()), Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY()));
                        }
                        g2d.drawRect((int)b.getX(), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight());
                    }
                    g2d.setColor(obj.getColor());
                    g2d.fillOval((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
                }
            }
            else if (obj instanceof Platform) {
                if (tempHasRotation) {
                    if (curr != null && curr.equals(obj)) {
                        g2d.setColor(Color.gray);
                        ((DrawnObject)curr).getBoundingBox().setRect(points.get(0).getCenterX(), points.get(0).getCenterY(), Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX()), Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY()));
                        g2d.drawRect((int)b.getX(), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight());
                    }
                    g2d.setColor(obj.getColor());
                    g2d.fill(tempRotationImage);
                } else {
                    if (curr != null && curr.equals(obj)) {
                        g2d.setColor(Color.gray);
                        ((DrawnObject)curr).getBoundingBox().setRect(points.get(0).getCenterX(), points.get(0).getCenterY(), Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX()), Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY()));
                        g2d.drawRect((int)b.getX(), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight());
                    }
                    g2d.setColor(obj.getColor());
                    g2d.fillRect((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());
                }
            }
            else if (obj instanceof Effect) {
                Effect e = (Effect) obj;
                if (e.getVisible()) {
                    if (tempHasRotation) {
                        if (curr != null && curr.equals(obj)) {
                            g2d.setColor(Color.gray);
                            ((DrawnObject)curr).getBoundingBox().setRect(points.get(0).getCenterX(), points.get(0).getCenterY(), Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX()), Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY()));
                            g2d.drawRect((int)b.getX(), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight());
                        }

                        g2d.setColor(e.getColor());
                        Point center = new Point((int)b.getX(), (int)b.getY());
                        Point one = new Point((int)b.getX(), (int)(b.getY() + b.getHeight()));
                        Point two = new Point((int)(b.getX() + b.getWidth()), (int)(b.getY() + b.getHeight()));
                        Point three = new Point((int)(b.getX() + (b.getWidth()/2.0)), (int)b.getY());
                        one = rotatePoint(one, center);
                        two = rotatePoint(two, center);
                        three = rotatePoint(three, center);
                        //bottom left, bottom right, top midpoint
                        int[] xPoints = new int[] {one.x, two.x, three.x};
                        int[] yPoints = new int[] {one.y, two.y, three.y};
                        g2d.fillPolygon(xPoints, yPoints, 3);
                    } else {
                        if (curr != null && curr.equals(obj)) {
                            g2d.setColor(Color.gray);
                            ((DrawnObject)curr).getBoundingBox().setRect(points.get(0).getCenterX(), points.get(0).getCenterY(), Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX()), Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY()));
                            g2d.drawRect((int)b.getX(), (int)b.getY(), (int)b.getWidth(), (int)b.getHeight());
                        }

                        g2d.setColor(e.getColor());
                        //bottom left, bottom right, top midpoint
                        int[] xPoints = new int[] {(int)b.getX(), (int)(b.getX() + b.getWidth()), (int)(b.getX() + (b.getWidth()/2.0))};
                        int[] yPoints = new int[] {(int)(b.getY() + b.getHeight()), (int)(b.getY() + b.getHeight()), (int)b.getY()};
                        g2d.fillPolygon(xPoints, yPoints, 3);
                    }
                }

            }
        }

        g2d.setColor(Color.gray);
        if (curr != null && !character.equals(curr)) {
            for (Rectangle point : points) {
                g2d.fillRect((int)point.getX(), (int)point.getY(), (int)point.getWidth(), (int)point.getHeight());
            }
        }
        else if (points != null && points.size() > 0){
            g2d.fillRect((int)points.get(1).getX(), (int)points.get(1).getY(), (int)points.get(1).getWidth(), (int)points.get(1).getHeight());
        }

        //draw main character
        if (curr != null && curr.equals(character)) {
            g2d.setColor(Color.gray);
            double tempWidth = Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX());
            double tempHeight = Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY());
            if (tempWidth > tempHeight) {
                character.getBounds().setRect(points.get(0).getCenterX(), points.get(0).getCenterY(), Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX()), Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX()));
            }
            else {
                character.getBounds().setRect(points.get(0).getCenterX(), points.get(0).getCenterY(), Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY()), Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY()));
            }
            g2d.drawRect((int)character.getBounds().getX(), (int)character.getBounds().getY(), (int)character.getBounds().getWidth(), (int)character.getBounds().getHeight());
        }
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

    private Shape rotate(DrawnObject o) {
        AffineTransform tx = new AffineTransform();
        angle += (Math.PI/128);
        tx.rotate(angle, o.getBoundingBox().getX(), o.getBoundingBox().getY());
        Shape newShape = tx.createTransformedShape(o.getBoundingBox());
        return newShape;
    }

    private Point rotatePoint(Point p, Point center) {
        angle += 0.01;
        int x = (int)(Math.cos(angle) * (p.x-center.x) - Math.sin(angle) * (p.y -  center.y) + center.x);
        int y = (int)(Math.sin(angle) * (p.x - center.x) + Math.cos(angle) * (p.y - center.y) + center.y);
        return new Point(x, y);
    }

    public void setMode(boolean m) {
	    mode = m;
        if (m) {
            this.requestFocus();
        }
    }

    public int getScore() { return score; }

    public Object getSelectedObject() { return curr; }

    public void setStatusUpdate(String u) { statusUpdate = u; }

    public String getStatusUpdate() { return statusUpdate; }

    public void addPlatform() {
	    Rectangle boundingBox = new Rectangle(640, 238, 50, 50);
        Platform p = new Platform(boundingBox);
        p.setColor(new Color(150, 75, 0));
        allObjects.add(p);
        statusUpdate = "New Platform Added";
    }

    public void addEnemy() {
        Rectangle boundingBox = new Rectangle(640, 238, 50, 50);
        Enemy e = new Enemy(boundingBox);
        e.setColor(Color.red);
        allObjects.add(e);
        statusUpdate = "New Enemy Added";
    }

    public void addEffect(boolean isReward) {
        Rectangle boundingBox = new Rectangle( 640, 238, 50, 50);
        Effect e = new Effect(boundingBox, isReward, 0);
        e.setColor(Color.yellow);
        allObjects.add(e);
        statusUpdate = "New Reward Added";
    }

    public void deleteShape(Object shape) {
	    if (!(shape instanceof MainCharacter) && allObjects.contains((DrawnObject)shape)) {
	        Color start = ((DrawnObject)shape).getColor();
	        Color end = Color.gray;
	        animTime = new Timer(5, new ColorChangeAnim(start, end, true));
	        animTime.start();
        }
	    repaint();
    }

    public void switchEffect(Effect e) {
        Color start = e.getColor();
        Color end;
	    if (e.getEffect()) {
            end = Color.black;
        }
	    else {
	        end = Color.yellow;
        }
	    animTime = new Timer(5, new ColorChangeAnim(start, end, false));
	    animTime.start();
	    repaint();
    }

    public void checkCollisions(boolean isGravity) {
	    Rectangle item;
	    for (DrawnObject obj : allObjects) {
	        item = obj.getBoundingBox();
	        if (bump(character.getBounds(), item)) {
	            if (obj instanceof Effect && !hitEffects.contains(obj)) {
	                hitEffects.add((Effect)obj);
	                ((Effect)obj).setVisible(false);

	                boolean isReward = ((Effect)obj).getEffect();
	                int cost = ((Effect)obj).getCost();
	                if (isReward) {
	                    score += cost;
	                    statusUpdate = "Hit Reward! " + cost + " Points Added";
                    }
	                else {
	                    score -= cost;
	                    statusUpdate = "Hit Penalty :( " + cost + " Points Lost";
                    }
                }
	            else if (canMove && (obj instanceof Platform)) {
                    if (isGravity && !jumping) {
                        character.setY(obj.getBoundingBox().y + character.getBounds().height + 1);
                    } else {
                        //canMove = false;
                        statusUpdate = "Blocked by a Platform";
                    }
                    if (movementDirection == 1) {
                        for (DrawnObject obj1 : allObjects) {
                            obj1.move(-50);
                        }
                    } else if (movementDirection == 0) {
                        for (DrawnObject obj1 : allObjects) {
                            obj1.move(50);
                        }
                    }
                } else if (canMove && (obj instanceof Enemy)) {
                    if (isGravity && !jumping) {
                        character.setY(obj.getBoundingBox().y + character.getBounds().height + 1);
                    } else {
                        //canMove = false;
                        statusUpdate = "Blocked by an Enemy";
                    }
                    if (movementDirection == 1) {
                        obj.move(-50);
                    } else if (movementDirection == 0) {
                        obj.move(50);
                    }
                }
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
            int dy = 0;
            if ((key == KeyEvent.VK_LEFT || movementDirection == 0) && canMove) {
                movementDirection = 0;
                dx = -4; //This can be changed to speed up/slow down game. Larger dx == faster scrolling
            }
            else if ((key == KeyEvent.VK_RIGHT || movementDirection == 1) && canMove) {
                movementDirection = 1;
                dx = 4;
            }
            if ((key == KeyEvent.VK_UP || jumping) && canMove) {
                jumping = true;
                dy = 10;
            }
            //move each enemy, platform, and effect based on the key press
            for (DrawnObject obj : allObjects) {
                obj.move(dx);
            }
            character.moveY(dy);
        }

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            int dx = 0;
            int dy = 0;
            if (key == KeyEvent.VK_LEFT) {
                dx = 0;
                movementDirection = -1;
            } else if (key == KeyEvent.VK_RIGHT) {
                dx = 0;
                movementDirection = -1;
            }
            if (key == KeyEvent.VK_UP && canMove) {
                jumping = false;
                dy = 15;
            }
            //stop moving all enemies, platforms, and effects
            for (DrawnObject obj : allObjects) {
                obj.move(dx);
            }
            character.moveY(dy);
        }
    }

    private boolean within(Object obj, Point p) {
	    if (obj instanceof MainCharacter) {
	        return ((MainCharacter)obj).getBounds().contains(p);
        }
	    else {
	        return ((DrawnObject)obj).getBoundingBox().contains(p);
        }
    }

    private class MousePressReleaseListener implements MouseListener {
	    public void mousePressed(MouseEvent e) {
            if (!mode) { //author mode
                currPoint = e.getPoint();
                if (e.isControlDown()) { //selecting a shape
                    //check if the control click is on a shape
                    if (allObjects.size() > 0) {
                        for (DrawnObject obj : allObjects) {
                            if (within(obj, currPoint)) {
                                if (curr != null) {
                                    points = new ArrayList<>();
                                }
                                curr = obj;
                                editorMode = -1;
                                Rectangle b = ((DrawnObject)curr).getBoundingBox();
                                Rectangle topLeftCorner = new Rectangle((int)b.getX() - 5, (int)b.getY() - 5, 10, 10);
                                Rectangle topRightCorner = new Rectangle((int)b.getX()  + (int)b.getWidth() - 5, (int)b.getY() - 5, 10, 10);
                                Rectangle bottomRightCorner = new Rectangle((int)b.getX() + (int)b.getWidth() - 5, (int)b.getY() + (int)b.getHeight() - 5, 10, 10);
                                Rectangle bottomLeftCorner = new Rectangle((int)b.getX() - 5, (int)b.getY() + (int)b.getHeight() - 5, 10, 10);
                                points.add(topLeftCorner);
                                points.add(topRightCorner);
                                points.add(bottomRightCorner);
                                points.add(bottomLeftCorner);
                                statusUpdate = "Object Selected";
                            }
                        }
                    }
                    if (within(character, currPoint)) {
                        if (curr != null) {
                            points = new ArrayList<>();
                        }

                        curr = character;
                        editorMode = -1;
                        Rectangle b = character.getBounds();
                        Rectangle topLeftCorner = new Rectangle((int)b.getX() - 5, (int)b.getY() - 5, 10, 10);
                        Rectangle topRightCorner = new Rectangle((int)b.getX()  + (int)b.getWidth() - 5, (int)b.getY() - 5, 10, 10);
                        Rectangle bottomRightCorner = new Rectangle((int)b.getX() + (int)b.getWidth() - 5, (int)b.getY() + (int)b.getHeight() - 5, 10, 10);
                        Rectangle bottomLeftCorner = new Rectangle((int)b.getX() - 5, (int)b.getY() + (int)b.getHeight() - 5, 10, 10);
                        points.add(topLeftCorner);
                        points.add(topRightCorner);
                        points.add(bottomRightCorner);
                        points.add(bottomLeftCorner);
                        statusUpdate = "Main Character Selected";
                    }
                    repaint();
                } else {
                    if (curr != null) {
                        if (points.get(0).contains(currPoint)) { //resizing
                            statusUpdate = "Resizing Object";
                            editorMode = 3;
                            resizeCorner = 0;
                            repaint();
                        }
                        else if (points.get(1).contains(currPoint)) {
                            statusUpdate = "Resizing Object";
                            editorMode = 3;
                            resizeCorner = 1;
                            repaint();
                        }
                        else if (points.get(2).contains(currPoint)) {
                            statusUpdate = "Resizing Object";
                            editorMode = 3;
                            resizeCorner = 2;
                            repaint();
                        }
                        else if (points.get(3).contains(currPoint)) {
                            statusUpdate = "Resizing Object";
                            editorMode = 3;
                            resizeCorner = 3;
                            repaint();
                        }
                        else if (!curr.equals(character) && within(curr, currPoint)){ //dragging
                            statusUpdate = "Dragging Object";
                            editorMode = 1;
                            prevDraggingPoint = e.getPoint();
                            repaint();
                        }
                        else { // animating
                            editorMode = 2;
                            currentStroke = new ArrayList<>();
                            statusUpdate = "Drawing Animation";
                            repaint();
                        }
                    } else {
                        //drawing new shape
                        editorMode = 0;
                        currentStroke = new ArrayList<>();
                        statusUpdate = "Drawing New Shape";
                        repaint();
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
	        if (!mode && editorMode == 0 && currentStroke != null && currentStroke.size() > 0) {
                //pass current stroke to the recognizer
                dollar.Result r = dr.recognize(currentStroke);
                if (r.getMatchedTemplate() != null) {
                    String name = r.getName();
                    //act on whatever the recognized template is
                    if (name.equals("triangle")) {
                        Effect temp = new Effect(r.getBoundingBox(), true, 0);
                        temp.setColor(Color.yellow);
                        allObjects.add(temp);
                        statusUpdate = "New Reward Added";
                    }
                    else if (name.equals("circle")) {
                        Rectangle newBounds;
                        if (r.getBoundingBox().getWidth() >= r.getBoundingBox().getHeight()) {
                            newBounds = new Rectangle((int)r.getBoundingBox().getX(), (int)r.getBoundingBox().getY(), (int)r.getBoundingBox().getWidth(), (int)r.getBoundingBox().getWidth());
                        }
                        else {
                            newBounds = new Rectangle((int)r.getBoundingBox().getX(), (int)r.getBoundingBox().getY(), (int)r.getBoundingBox().getHeight(), (int)r.getBoundingBox().getHeight());
                        }
                        Enemy temp = new Enemy(newBounds);
                        temp.setColor(Color.red);
                        allObjects.add(temp);
                        statusUpdate = "New Enemy Added";
                    }
                    else if (name.equals("rectangle")) {
                        Platform temp = new Platform(r.getBoundingBox());
                        temp.setColor(new Color(150, 75, 0));
                        allObjects.add(temp);
                        statusUpdate = "New Platform Added";
                    }
                    else {
                        statusUpdate = "Shape Not Recognized. Please Redraw.";
                    }
                }
                currentStroke = null;
                curr = null;
                repaint();
            }
	        else if (!mode && editorMode == 1) {
                currPoint = e.getPoint();
                // final update for end location
                int xDiff = prevDraggingPoint.x - currPoint.x;
                int yDiff = prevDraggingPoint.y - currPoint.y;
                ((DrawnObject)curr).move(xDiff);
                ((DrawnObject)curr).moveY(yDiff);
                curr = null;
                points = new ArrayList<>();
                statusUpdate = "Dragging Completed";
                prevDraggingPoint = null;
                repaint();
            }
	        else if (!mode && editorMode == 2 && currentStroke != null && currentStroke.size() > 0) {
                dollar.Result r = dr.recognize(currentStroke);
                if (r.getMatchedTemplate() != null) {
                    String name = r.getName();
                    //act on whatever the recognized template is
                    if (name.equals("right square bracket")) { //forward motion
                        ((DrawnObject)curr).setHasAnimation(true);
                        ((DrawnObject)curr).getMotionTypes()[1] = true;
                        ((DrawnObject)curr).getEndpoints()[1] = r.getBoundingBox().getLocation();
                        if (((DrawnObject)curr).getMotionTypes()[0]) { //left motion also selected
                            ((DrawnObject)curr).calculateLR();
                            statusUpdate = "Set Back And Forth Animation";
                        } else {
                            ((DrawnObject)curr).calculateX(r.getBoundingBox().getLocation(), 1);
                            statusUpdate = "Set Forward Animation";
                        }
                    } else if (name.equals("left square bracket")) { //backward motion
                        ((DrawnObject)curr).setHasAnimation(true);
                        ((DrawnObject)curr).getMotionTypes()[0] = true;
                        ((DrawnObject)curr).getEndpoints()[0] = r.getBoundingBox().getLocation();
                        if (((DrawnObject)curr).getMotionTypes()[1]) { //right motion also selected
                            ((DrawnObject)curr).calculateLR();
                            statusUpdate = "Set Back and Forth Animation";
                        } else {
                            ((DrawnObject)curr).calculateX(r.getBoundingBox().getLocation(), 0);
                            statusUpdate = "Set Backward Animation";
                        }
                    } else if (name.equals("circle")) { //rotation
                        ((DrawnObject)curr).setHasAnimation(true);
                        ((DrawnObject)curr).getMotionTypes()[6] = true;
                        statusUpdate = "Set Rotation";
                    } else if (name.equals("caret")) { //going up from current location
                        ((DrawnObject)curr).setHasAnimation(true);
                        ((DrawnObject)curr).getMotionTypes()[2] = true;
                        ((DrawnObject)curr).getEndpoints()[2] = r.getBoundingBox().getLocation();
                        if (((DrawnObject)curr).getMotionTypes()[3]) {
                            ((DrawnObject)curr).calculateUD();
                            statusUpdate = "Set Up and Down Animation";
                        } else {
                            ((DrawnObject)curr).calculateY(r.getBoundingBox().getLocation(), 2);
                            statusUpdate = "Set Up Animation";
                        }
                    } else if (name.equals("v")) { // dropping
                        ((DrawnObject)curr).setHasAnimation(true);
                        ((DrawnObject)curr).getMotionTypes()[3] = true;
                        ((DrawnObject)curr).getEndpoints()[3] = r.getBoundingBox().getLocation();
                        if (((DrawnObject)curr).getMotionTypes()[2]) {
                            ((DrawnObject)curr).calculateUD();
                            statusUpdate = "Set Up and Down Animation";
                        } else {
                            ((DrawnObject)curr).calculateY(r.getBoundingBox().getLocation(), 3);
                            statusUpdate = "Set Down Animation";
                        }
                    } else if (name.equals("x")) { // clearing
                        statusUpdate = "Animation Cleared";
                        ((DrawnObject)curr).clearAnimation();
                    } else {
                        statusUpdate = "Animation Not Recognized. Please Redraw.";
                    }
                    curr = null;
                    points = new ArrayList<>();
                }
                currentStroke = null;
                repaint();
            }
	        else if (!mode && editorMode == 3) { //resizing
                statusUpdate = "Resize Completed";
                resizeCorner = -1;
                curr = null;
                points = new ArrayList<>();
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
            if (!mode && (editorMode == 0 || editorMode == 2)) {
                currentStroke.add(e.getPoint());
                statusUpdate = "Drawing New Stroke";
                repaint();
            }
            else if (!mode && editorMode == 1) {
                // work off of curr value
                currPoint = e.getPoint();
                int xDiff = prevDraggingPoint.x - currPoint.x;
                int yDiff = prevDraggingPoint.y - currPoint.y;
                if ((currPoint.x >= boundaryX || currPoint.x <= 10) && !draggingAndScrolling) {
                    //start scrolling
                    draggingAndScrolling = true;
                    for (DrawnObject obj : allObjects) {
                        obj.move( -1 * xDiff);
                    }
                }
                else if ((currPoint.x >= boundaryX || currPoint.x <= 10) && draggingAndScrolling) {
                    //keep scrolling
                    int dx = 0;
                    if (currPoint.x >= boundaryX) { //move objects left
                        dx = 4;
                    }
                    else { //move objects right
                        dx = -4;
                    }
                    for (DrawnObject obj : allObjects) {
                        if (!obj.equals(curr)) {
                            obj.move(dx);
                        }
                    }
                }
                else {
                    //stop scrolling if necessary, just drag normally
                    if (draggingAndScrolling) {
                        draggingAndScrolling = false;
                    }
                    points.get(0).setRect(points.get(0).getX() - xDiff, points.get(0).getY(), points.get(0).getWidth(), points.get(0).getHeight());
                    points.get(1).setRect(points.get(1).getX() - xDiff, points.get(1).getY(), points.get(1).getWidth(), points.get(1).getHeight());
                    points.get(2).setRect(points.get(2).getX() - xDiff, points.get(2).getY(), points.get(2).getWidth(), points.get(2).getHeight());
                    points.get(3).setRect(points.get(3).getX() - xDiff, points.get(3).getY(), points.get(3).getWidth(), points.get(3).getHeight());
                    //((DrawnObject)curr).move(xDiff);
                }
                //((DrawnObject)curr).moveY(yDiff);
                points.get(0).setRect(points.get(0).getX(), points.get(0).getY() - yDiff, points.get(0).getWidth(), points.get(0).getHeight());
                points.get(1).setRect(points.get(1).getX(), points.get(1).getY() - yDiff, points.get(1).getWidth(), points.get(1).getHeight());
                points.get(2).setRect(points.get(2).getX(), points.get(2).getY() - yDiff, points.get(2).getWidth(), points.get(2).getHeight());
                points.get(3).setRect(points.get(3).getX(), points.get(3).getY() - yDiff, points.get(3).getWidth(), points.get(3).getHeight());
                statusUpdate = "Dragging Object";
                prevDraggingPoint = currPoint;
                repaint();
            }
            else if (!mode && editorMode == 3) {
                currPoint = e.getPoint();
                if (resizeCorner == 0) {
                    points.get(1).setRect(currPoint.x, points.get(1).getY(), points.get(resizeCorner).getWidth(), points.get(resizeCorner).getHeight());
                    points.get(3).setRect(points.get(3).getX(), currPoint.y, points.get(resizeCorner).getWidth(), points.get(resizeCorner).getHeight());
                }
                else if (resizeCorner == 1) {
                    points.get(0).setRect(points.get(0).getX(), currPoint.y, points.get(resizeCorner).getWidth(), points.get(resizeCorner).getHeight());
                    points.get(2).setRect(currPoint.x, points.get(3).getY(), points.get(resizeCorner).getWidth(), points.get(resizeCorner).getHeight());
                }
                else if (resizeCorner == 2) {
                    points.get(1).setRect(currPoint.x, points.get(1).getY(), points.get(resizeCorner).getWidth(), points.get(resizeCorner).getHeight());
                    points.get(3).setRect(points.get(3).getX(), currPoint.y, points.get(resizeCorner).getWidth(), points.get(resizeCorner).getHeight());
                }
                else if (resizeCorner == 3) {
                    points.get(0).setRect(currPoint.x, points.get(0).getY(), points.get(resizeCorner).getWidth(), points.get(resizeCorner).getHeight());
                    points.get(2).setRect(points.get(2).getX(), currPoint.y, points.get(resizeCorner).getWidth(), points.get(resizeCorner).getHeight());
                }
                else {
                    return;
                }
                points.get(resizeCorner).setRect(currPoint.x, currPoint.y, points.get(resizeCorner).getWidth(), points.get(resizeCorner).getHeight());

                //reset for the circle's endpoints
                double tempWidth = Math.abs(points.get(2).getCenterX() - points.get(0).getCenterX());
                double tempHeight = Math.abs(points.get(2).getCenterY() - points.get(0).getCenterY());
                double tempRadius;
                if (tempWidth > tempHeight) {
                    tempRadius = tempWidth;
                } else {
                    tempRadius = tempHeight;
                }
                if (curr instanceof MainCharacter || curr instanceof Enemy) { //maintain the shape of the circle if the length and width aren't equal
                    if (resizeCorner == 0) {
                        points.get(1).setRect(points.get(1).getX(), points.get(2).getY() - tempRadius, points.get(1).getWidth(), points.get(1).getHeight());
                        points.get(3).setRect(points.get(2).getX() - tempRadius, points.get(3).getY(), points.get(3).getWidth(), points.get(3).getHeight());
                        points.get(0).setRect(points.get(2).getX() - tempRadius, points.get(2).getY() - tempRadius, points.get(0).getWidth(), points.get(0).getHeight());
                    } else if (resizeCorner == 1) {
                        points.get(0).setRect(points.get(0).getX(), points.get(3).getY() - tempRadius, points.get(0).getWidth(), points.get(0).getHeight());
                        points.get(2).setRect(points.get(3).getX() + tempRadius, points.get(2).getY(), points.get(2).getWidth(), points.get(2).getHeight());
                        points.get(1).setRect(points.get(3).getX() + tempRadius, points.get(3).getY() - tempRadius, points.get(1).getWidth(), points.get(1).getHeight());
                    } else if (resizeCorner == 2) {
                        points.get(3).setRect(points.get(3).getX(), points.get(0).getY() + tempRadius, points.get(3).getWidth(), points.get(3).getHeight());
                        points.get(1).setRect(points.get(0).getX() + tempRadius, points.get(1).getY(), points.get(1).getWidth(), points.get(1).getHeight());
                        points.get(2).setRect(points.get(0).getX() + tempRadius, points.get(0).getY() + tempRadius, points.get(2).getWidth(), points.get(2).getHeight());
                    } else if (resizeCorner == 3) {
                        points.get(0).setRect(points.get(1).getX() - tempRadius, points.get(0).getY(), points.get(0).getWidth(), points.get(0).getHeight());
                        points.get(2).setRect(points.get(2).getX(), points.get(1).getY() + tempRadius, points.get(2).getWidth(), points.get(2).getHeight());
                        points.get(3).setRect(points.get(1).getX() - tempRadius, points.get(1).getY() + tempRadius, points.get(3).getWidth(), points.get(3).getHeight());
                    }
                }

                statusUpdate = "Resizing Object";
                repaint();
            }
        }

        public void mouseMoved(MouseEvent e) {
        }
    }

    public class ColorChangeAnim implements ActionListener {
	    double steps[];
	    int percentageDone = 0;
        Color start;
        Color end;
        boolean deletion;

        public ColorChangeAnim(Color start, Color end, boolean deletion) {
            this.start = start;
            this.end = end;
            this.deletion = deletion;
            steps = new double[]{(end.getRed() - start.getRed())/100.0, (end.getGreen() - start.getGreen())/100.0, (end.getBlue() - start.getBlue())/100.0};
        }

        public void actionPerformed(ActionEvent c) {
            if (percentageDone == 100) {
                if (deletion) {
                    allObjects.remove(curr);
                }
                curr = null;
                points = new ArrayList<>();
                animTime.stop();
            } else {
                double red = start.getRed() + (percentageDone * steps[0]);
                double green = start.getGreen() + (percentageDone * steps[1]);
                double blue = start.getBlue() + (percentageDone * steps[2]);

                if (red < 0) {
                    red = 0;
                } else if (red > 255) {
                    red = 255;
                }
                if (green < 0) {
                    green = 0;
                } else if (green > 255) {
                    green = 255;
                }
                if (blue < 0) {
                    blue = 0;
                } else if (blue > 255) {
                    blue = 255;
                }
                //System.out.println("Red: " + red + " Green: " + green + " Blue: " + blue);
                ((DrawnObject)curr).setColor(new Color((int)red, (int)green, (int)blue));
                percentageDone += 1;
            }
        }
    }
}