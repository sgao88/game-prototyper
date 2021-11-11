import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class GameBoard extends JPanel implements ActionListener{
    MainCharacter character;
    Timer time;
    boolean mode;
    int editorMode;
    dollar.DollarRecognizer dr;
    ArrayList<Point2D> currentStroke;
    ArrayList<DrawnObject> allObjects;
    ArrayList<Effect> hitEffects;
    boolean canMove;
    boolean dragging;
    Point currPoint;
    DrawnObject curr;
    DrawnObject currAnim;
    int score;
    int boundaryX;
    boolean draggingAndScrolling;
    boolean jumping;
    dollar.Result lastAnimStroke;

    JDialog dialog;
    JPanel fieldPanel;
    JPanel widthPanel;
    JLabel widthLabel;
    JTextField widthInput;
    JPanel heightPanel;
    JLabel heightLabel;
    JTextField heightInput;
    JPanel radioButtonPanel;
    ButtonGroup buttonGroup;
    JRadioButton rewardButton;
    JRadioButton penaltyButton;
    JPanel costPanel;
    JLabel costLabel;
    JTextField costTextField;
    JPanel buttonPanel;
    JButton deleteButton;
    JButton saveButton;

	public GameBoard(boolean mode, int editorMode) {
        character = new MainCharacter();
        this.mode = mode;
        this.editorMode = editorMode;
        dr = new dollar.DollarRecognizer();
        currentStroke = null;
        allObjects = new ArrayList<>();
        hitEffects = new ArrayList<>();
        canMove = true;
        score = 0;
        boundaryX = 680;
        draggingAndScrolling = false;
        jumping = false;

        dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setMinimumSize(new Dimension(250, 300));

        fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));

        widthPanel = new JPanel();
        widthLabel = new JLabel("Width");
        widthInput = new JTextField("xx", 5);
        widthInput.setHorizontalAlignment(JTextField.CENTER);
        widthPanel.add(widthLabel);
        widthPanel.add(widthInput);
        fieldPanel.add(widthPanel);

        heightPanel = new JPanel();
        heightLabel = new JLabel("Height");
        heightInput = new JTextField("xx", 5);
        heightInput.setHorizontalAlignment(JTextField.CENTER);
        heightPanel.add(heightLabel);
        heightPanel.add(heightInput);
        fieldPanel.add(heightPanel);

        radioButtonPanel = new JPanel();
        rewardButton = new JRadioButton("Reward");
        rewardButton.setSelected(true);
        penaltyButton = new JRadioButton("Penalty");
        buttonGroup = new ButtonGroup();
        buttonGroup.add(rewardButton);
        buttonGroup.add(penaltyButton);
        radioButtonPanel.add(rewardButton);
        radioButtonPanel.add(penaltyButton);
        fieldPanel.add(radioButtonPanel);

        costPanel = new JPanel();
        costLabel = new JLabel("Cost");
        costTextField = new JTextField("0", 5);
        costTextField.setHorizontalAlignment(JTextField.CENTER);
        costPanel.add(costLabel);
        costPanel.add(costTextField);
        fieldPanel.add(costPanel);

        buttonPanel = new JPanel();
        deleteButton = new JButton("Delete Shape");
        deleteButton.setBackground(Color.red);
        deleteButton.setOpaque(true);
        deleteButton.setBorderPainted(false);
        deleteButton.setForeground(Color.white);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent c) {
                allObjects.remove(curr);
                curr = null;
                dialog.setVisible(false);
                repaint();
            }
        });

        saveButton = new JButton("Save Changes");
        saveButton.setBackground(Color.blue);
        saveButton.setForeground(Color.white);
        saveButton.setOpaque(true);
        saveButton.setBorderPainted(false);
        saveButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent c) {
               Object temp;
               boolean updatingCurrAnim = false;
               if (curr != null) { //if curr is null, the main character should be updated
                   if (curr.equals(currAnim)) {
                       updatingCurrAnim = true;
                   }
                   temp = curr;
                   allObjects.remove(curr);
                   curr = null;
               }
               else {
                   temp = character;
               }
               int width = (int)Double.parseDouble(widthInput.getText());
               int height = (int)Double.parseDouble(heightInput.getText());
               if (temp instanceof MainCharacter) {
                   int shift;
                   int currWidth = (int)character.getBounds().getWidth();
                   int currHeight = (int)character.getBounds().getHeight();
                   int currX = (int)character.getBounds().getX();
                   int currY = (int)character.getBounds().getY();
                   if (width != height) {
                       if (width != currWidth && height == currHeight) { //width was changed
                           shift = width - currWidth;
                           character.getBounds().setSize(width, width);
                           character.setRadius(width);
                       }
                       else if (height != currHeight && width == currWidth) {
                           shift = height - currHeight;
                           character.getBounds().setSize(height, height);
                           character.setRadius(height);
                       }
                       else if (width > height) {
                           shift = width - currWidth;
                           character.getBounds().setSize(width, width);
                           character.setRadius(width);
                       }
                       else {
                           shift = height - currHeight;
                           character.getBounds().setSize(height, height);
                           character.setRadius(height);
                       }
                   }
                   else {
                       shift = width - currWidth;
                       character.getBounds().setSize(width, height);
                       character.setRadius(width);
                   }

                   character.getBounds().setLocation(currX, currY - shift);
                   character.setY(currY - shift);
               }
               else if (temp instanceof Enemy) {
                   if (width != height) {
                       if (width != ((Enemy)temp).getBoundingBox().getWidth()
                               && height == ((Enemy)temp).getBoundingBox().getHeight()) { //width was changed
                           ((Enemy)temp).getBoundingBox().setSize(width, width);
                       }
                       else if (height != ((Enemy)temp).getBoundingBox().getHeight()
                               && width == ((Enemy)temp).getBoundingBox().getWidth()) {
                           ((Enemy)temp).getBoundingBox().setSize(height, height);
                       }
                       else if (width > height) {
                           ((Enemy)temp).getBoundingBox().setSize(width, width);
                       }
                       else {
                           ((Enemy)temp).getBoundingBox().setSize(height, height);
                       }
                   }
                   else {
                       ((Enemy)temp).getBoundingBox().setSize(width, height);
                   }
                   if (updatingCurrAnim) {
                       currAnim = (DrawnObject) temp;
                   }
                   allObjects.add((DrawnObject)temp);
               }
               else if (temp instanceof Platform){
                   ((DrawnObject)temp).getBoundingBox().setSize(width, height);
                   if (updatingCurrAnim) {
                       currAnim = (DrawnObject) temp;
                   }
                   allObjects.add((DrawnObject)temp);
               }
               else {
                   ((DrawnObject)temp).getBoundingBox().setSize(width, height);
                   if (rewardButton.isSelected()) {
                       ((Effect)temp).setEffect(true);
                   }
                   else {
                       ((Effect)temp).setEffect(false);
                   }
                   ((Effect)temp).setCost(Math.abs(Integer.parseInt(costTextField.getText()))); //Math.abs to control for if they put in a negative number for the penalty
                   if (updatingCurrAnim) {
                       currAnim = (DrawnObject) temp;
                   }
                   allObjects.add((DrawnObject)temp);
               }
               dialog.setVisible(false);
               repaint();
           }
        });

        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(fieldPanel, BorderLayout.CENTER);

        dialog.setVisible(false);
        dialog.pack();

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
            if (character.getY() < 250) {
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
            Rectangle b = obj.getBoundingBox();
            if (obj instanceof Enemy) {
                if (!mode && obj.equals(currAnim) && editorMode == 2) {
                    g2d.setColor(Color.white);
                    g2d.fillOval((int) b.getX() - 5, (int) b.getY() - 5, (int) b.getWidth() + 10, (int) b.getHeight() + 10);
                    g2d.setColor(Color.gray);
                    g2d.drawOval((int) b.getX() - 5, (int) b.getY() - 5, (int) b.getWidth() + 10, (int) b.getHeight() + 10);
                }
                g2d.setColor(Color.red);
                g2d.fillOval((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());

            }
            else if (obj instanceof Platform) {
                if (!mode && obj.equals(currAnim) && editorMode == 2) {
                    g2d.setColor(Color.white);
                    g2d.fillRect((int) b.getX() - 5, (int) b.getY() - 5, (int) b.getWidth() + 10, (int) b.getHeight() + 10);
                    g2d.setColor(Color.gray);
                    g2d.drawRect((int) b.getX() - 5, (int) b.getY() - 5, (int) b.getWidth() + 10, (int) b.getHeight() + 10);
                }
                g2d.setColor(new Color(150, 75, 0)); //brown
                g2d.fillRect((int) b.getX(), (int) b.getY(), (int) b.getWidth(), (int) b.getHeight());

            }
            else if (obj instanceof Effect) {
                Effect e = (Effect) obj;
                if (e.getVisible()) {
                    if (!mode && obj.equals(currAnim) && editorMode == 2) {
                        g2d.setColor(Color.white);
                        //bottom left, bottom right, top midpoint
                        int[] xPoints = new int[] {(int)b.getX() - 5, (int)(b.getX() + b.getWidth() + 5), (int)(b.getX() + (b.getWidth()/2.0))};
                        int[] yPoints = new int[] {(int)(b.getY() + b.getHeight() + 5), (int)(b.getY() + b.getHeight() + 5), (int)b.getY() - 5};
                        g2d.fillPolygon(xPoints, yPoints, 3);
                        g2d.setColor(Color.gray);
                        g2d.drawPolygon(xPoints, yPoints, 3);
                    }

                    if (e.getEffect()) {
                        g2d.setColor(Color.yellow);
                    } else {
                        g2d.setColor(Color.black);
                    }

                    //bottom left, bottom right, top midpoint
                    int[] xPoints = new int[] {(int)b.getX(), (int)(b.getX() + b.getWidth()), (int)(b.getX() + (b.getWidth()/2.0))};
                    int[] yPoints = new int[] {(int)(b.getY() + b.getHeight()), (int)(b.getY() + b.getHeight()), (int)b.getY()};
                    g2d.fillPolygon(xPoints, yPoints, 3);
                }

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

    public void setEditorMode(int m) {
        editorMode = m;
    }

    public boolean getMode() { return mode; }

    public int getEditorMode() { return editorMode; }

    public int getScore() { return score; }

    public void addPlatform() {
	    Rectangle boundingBox = new Rectangle(640, 240, 50, 50);
        Platform p = new Platform(boundingBox);
        allObjects.add(p);
    }

    public void addEnemy() {
        Rectangle boundingBox = new Rectangle(640, 240, 50, 50);
        Enemy e = new Enemy(boundingBox);
        allObjects.add(e);
    }

    public void addEffect(boolean isReward) {
        Rectangle boundingBox = new Rectangle( 640, 240, 50, 50);
        Effect e = new Effect(boundingBox, isReward, 0);
        allObjects.add(e);
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
	                    System.out.println("New score is " + score);
                    }
	                else {
	                    score -= cost;
	                    System.out.println("New score is " + score);
                    }
                }
	            else if (canMove && (obj instanceof Platform || obj instanceof Enemy)) {
                    if (isGravity && !jumping) {
                        character.setY(obj.getBoundingBox().y + character.getBounds().height + 1);
                    } else {
                        canMove = false;
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
            if (key == KeyEvent.VK_LEFT && canMove) {
                dx = -4; //This can be changed to speed up/slow down game. Larger dx == faster scrolling
            }
            else if (key == KeyEvent.VK_RIGHT && canMove) {
                dx = 4;
            }
            if (key == KeyEvent.VK_UP && canMove) {
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
            } else if (key == KeyEvent.VK_RIGHT) {
                dx = 0;
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
                if (e.isControlDown()) { //open dialog box
                    //check if the control click is on a shape
                    currPoint = e.getPoint();
                    if (allObjects.size() > 0) {
                        for (DrawnObject obj : allObjects) {
                            if (within(obj, e.getPoint())) {
                                curr = obj;
                                //open the dialog box
                                widthInput.setText("" + obj.getBoundingBox().getWidth());
                                heightInput.setText("" + obj.getBoundingBox().getHeight());
                                if (obj instanceof Effect) {
                                    if (((Effect)obj).getEffect()) {
                                        rewardButton.setSelected(true);
                                        penaltyButton.setSelected(false);
                                    }
                                    else {
                                        rewardButton.setSelected(false);
                                        penaltyButton.setSelected(true);
                                    }
                                    rewardButton.setEnabled(true);
                                    penaltyButton.setEnabled(true);
                                    rewardButton.setForeground(Color.black);
                                    penaltyButton.setForeground(Color.black);
                                    costTextField.setText("" + ((Effect)obj).getCost());
                                    costTextField.setEnabled(true);
                                    costLabel.setForeground(Color.black);
                                }
                                else {
                                    rewardButton.setEnabled(false);
                                    penaltyButton.setEnabled(false);
                                    rewardButton.setForeground(Color.lightGray);
                                    penaltyButton.setForeground(Color.lightGray);
                                    costTextField.setText("0");
                                    costTextField.setEnabled(false);
                                    costLabel.setForeground(Color.lightGray);
                                }
                                deleteButton.setEnabled(true);
                                deleteButton.setBackground(Color.red);
                                deleteButton.setForeground(Color.white);
                                dialog.setVisible(true);
                                repaint();
                            }
                        }
                    }
                    if (within(character, e.getPoint())) {
                        widthInput.setText("" + character.getRadius());
                        heightInput.setText("" + character.getRadius());
                        rewardButton.setEnabled(false);
                        penaltyButton.setEnabled(false);
                        rewardButton.setForeground(Color.lightGray);
                        penaltyButton.setForeground(Color.lightGray);
                        costTextField.setText("0");
                        costTextField.setEnabled(false);
                        costLabel.setForeground(Color.lightGray);
                        deleteButton.setEnabled(false);
                        deleteButton.setBackground(Color.lightGray);
                        deleteButton.setForeground(Color.darkGray);
                        dialog.setVisible(true);
                        repaint();
                    }
                    currentStroke = new ArrayList<>();
                    repaint();
                }
                else if (editorMode == 0) {
                    // drawing mode
                    currentStroke = new ArrayList<>();
                    repaint();
                }
                else if (editorMode == 1) {
                    // mouse press to enter dragging object mode
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
                } else if (editorMode == 2 && currAnim == null) {
                    // mouse press to start drawing an animation object
                    // associated with the curr object
                    currPoint = e.getPoint();
                    if (allObjects.size() > 0) {
                        for (DrawnObject obj : allObjects) {
                            if (within(obj, e.getPoint())) {
                                currAnim = obj;
                                return;
                            }
                        }
                    }
                } else if (editorMode == 2 && currAnim != null) {
                    currPoint = e.getPoint();
                    if (allObjects.size() > 0) {
                        for (DrawnObject obj : allObjects) {
                            if (within(obj, e.getPoint()) && !obj.equals(currAnim)) {
                                currAnim = obj;
                                return;
                            }
                        }
                    }
                    currentStroke = new ArrayList<>();
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
	        if (!mode && editorMode == 0 && currentStroke.size() > 0) {
                //pass current stroke to the recognizer
                dollar.Result r = dr.recognize(currentStroke);
                if (r.getMatchedTemplate() != null) {
                    String name = r.getName();
                    //act on whatever the recognized template is
                    if (name.equals("triangle")) {
                        Effect temp = new Effect(r.getBoundingBox(), true, 0);
                        allObjects.add(temp);
                    } else if (name.equals("circle")) {
                        Rectangle newBounds;
                        if (r.getBoundingBox().getWidth() >= r.getBoundingBox().getHeight()) {
                            newBounds = new Rectangle((int)r.getBoundingBox().getX(), (int)r.getBoundingBox().getY(), (int)r.getBoundingBox().getWidth(), (int)r.getBoundingBox().getWidth());
                        }
                        else {
                            newBounds = new Rectangle((int)r.getBoundingBox().getX(), (int)r.getBoundingBox().getY(), (int)r.getBoundingBox().getHeight(), (int)r.getBoundingBox().getHeight());
                        }
                        Enemy temp = new Enemy(newBounds);
                        allObjects.add(temp);
                    } else if (name.equals("rectangle")) {
                        Platform temp = new Platform(r.getBoundingBox());
                        allObjects.add(temp);
                    }
                }
                currentStroke = null;
                repaint();
            } else if (!mode && editorMode == 1 && curr != null && dragging) {
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
            } else if (!mode && editorMode == 2 && currentStroke != null) {
                dollar.Result r = dr.recognize(currentStroke);
                if (r.getMatchedTemplate() != null) {
                    String name = r.getName();
                    //act on whatever the recognized template is
                    if (name.equals("right square bracket")) { //forward motion
                        if (lastAnimStroke.getName().equals("left square bracket") && lastAnimStroke.getBoundingBox().getX() < r.getBoundingBox().getX()) {
                            System.out.println("Back and forth motion");
                        } else {
                            System.out.println("Forward motion");
                        }
                        lastAnimStroke = r;
                    } else if (name.equals("left square bracket")) { //backward motion
                        if (lastAnimStroke.getName().equals("right square bracket") && lastAnimStroke.getBoundingBox().getX() > r.getBoundingBox().getX()) {
                            System.out.println("Back and forth motion");
                        } else {
                            System.out.println("Backward motion");
                        }
                        lastAnimStroke = r;
                    } else if (name.equals("circle")) { //rotation
                        System.out.println("Rotation");
                        lastAnimStroke = r;
                    } else if (name.equals("caret")) { //dropping from current location
                        if (lastAnimStroke.getName().equals("v") && lastAnimStroke.getBoundingBox().getY() > r.getBoundingBox().getY()) {
                            System.out.println("Up and Down motion between the caret and v");
                        } else {
                            System.out.println("Dropping from current location");
                        }
                        lastAnimStroke = r;
                    } else if (name.equals("v")) {
                        if (lastAnimStroke.getName().equals("caret") && lastAnimStroke.getBoundingBox().getY() < r.getBoundingBox().getY()) {
                            System.out.println("Up and Down motion between the caret and v");
                        }
                        lastAnimStroke = r;
                    } else if (name.equals("x")) {
                        lastAnimStroke = r;
                        System.out.println("Clear animation for this shape");
                    }
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
            if (!mode && editorMode == 0) {
                currentStroke.add(e.getPoint());
                repaint();
            } else if (!mode && editorMode == 1 && curr != null) {
                // mouse dragging for moving objects, !editorMode implied
                dragging = true;
                // work off of curr value
                currPoint = e.getPoint();
                int xDiff = curr.getBoundingBox().x - currPoint.x;
                int yDiff = curr.getBoundingBox().y - currPoint.y;
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
                    curr.move(xDiff);
                }
                curr.moveY(yDiff);
                repaint();
            } else if (!mode && editorMode == 2 && currentStroke != null) {
                currentStroke.add(e.getPoint());
                repaint();
            }
        }

        public void mouseMoved(MouseEvent e) {
        }
    }
}