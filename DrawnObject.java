import java.awt.*;

/**
 * motionTypes/endpoints get updated in mouselisteners when an animation is added
 * motionDistances gets updated when endpoints is updated; it stores the absolute value
 * of each distance that the object has to move.
 *
 * currentMotionDistances gets updated in each redraw/interval during play mode.
 * for example, if motionDistances[0] is 5, that's how many pixels it moves left each interval.
 *
 * The values of motionDistances are calculated by taking the difference between endpoints
 * (for left/right/up/down, this is the difference between the x/y of the object's
 * bounding box and the point; for bouncing motions, this is the difference between the
 * left and right or up and down points) and dividing them by the "magic number" 5.
 * 5 is arbitrary and can be changed to change animation speed.
 *
 * currentMotionDistances stores how many "frames" are left for the bouncing motions,
 * and this is decremented each interval until it reaches 0, upon which it will become
 * the "other direction", until it reaches -4. Then, it will be reset to the magic number.
 * Ex. if the magic number is 5:
 * Values 5, 4, 3, 2, and 1 will be "positive" motion (right/down) and values 0, -1, -2, -3, -4
 * will be "negative" motion (left/up) according to the x-y plane of the game board.
 */
public abstract class DrawnObject {
    final static double magicNumber = 30.0;
    Rectangle boundingBox;
    private boolean hasAnimation = false;
    private boolean[] motionTypes = new boolean[7];
    // 0-left, 1-right, 2-up (caret), 3-down (V), 4-LR, 5-UD, 6-rotation
    private int[] motionDistances = new int[7]; // same as above
    private int[] currentMotionDistances = new int[2]; // 0-Left-Right, 1-Up-Down
    private Point[] endpoints = new Point[4]; // 0-left, 1-right, 2-up, 3-down

    public DrawnObject() {
    }

    public void move(int dx) {
        int currX = (int)boundingBox.getX();
        boundingBox.setLocation(currX - dx, (int)boundingBox.getY());
    }

    public void moveY(int dy) {
        int currY = (int)boundingBox.getY();
        boundingBox.setLocation((int)boundingBox.getX(), currY - dy);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public boolean[] getMotionTypes() { return motionTypes; }

    public int[] getMotionDistances() { return motionDistances; }

    public int[] getCurrentMotionDistances() { return currentMotionDistances; }

    public Point[] getEndpoints() { return endpoints; }

    /**
     * Updates left-right motion, including when it is already bouncing L->R
     */
    public void calculateLR() {
        motionTypes[0] = false;
        motionTypes[1] = false;
        motionTypes[4] = true;
        motionDistances[4] = (int) (Math.abs(endpoints[1].getX() - endpoints[0].getX()) / magicNumber);
        motionDistances[0] = 0;
        motionDistances[1] = 0;
        currentMotionDistances[0] = 5;
    }

    /**
     * Equivalent of calculateLR() for up-down motion
     */
    public void calculateUD() {
        motionTypes[2] = false;
        motionTypes[3] = false;
        motionTypes[5] = true;
        motionDistances[5] = (int) (Math.abs(endpoints[3].getY() - endpoints[2].getY()) / magicNumber);
        motionDistances[2] = 0;
        motionDistances[3] = 0;
        currentMotionDistances[1] = 5;
    }

    public void calculateX(Point p, int index) {
        motionDistances[index] = (int) (Math.abs(p.getX() - boundingBox.getX()) / magicNumber);
    }

    public void calculateY(Point p, int index) {
        motionDistances[index] = (int) (Math.abs(p.getY() - boundingBox.getY()) / magicNumber);
    }

    public void clearAnimation() {
        hasAnimation = false;
        motionTypes = new boolean[7];
        motionDistances = new int[7];
        currentMotionDistances = new int[2];
        endpoints = new Point[4];
    }

    public boolean hasAnimation() {
        return hasAnimation;
    }

    public void setHasAnimation(boolean h) {
        hasAnimation = h;
    }

    /**
     * Performs one interval of animation.
     */
    public void step() {
        if (motionTypes[0]) {
            // left only
            move(motionDistances[0]);
        } else if (motionTypes[1]) {
            // right only
            move(motionDistances[1]);
        } else if (motionTypes[4]) {
            // left-right
            if (currentMotionDistances[0] > 0) {
                // move right
                move(motionDistances[4]);
            } else {
                // move left
                move(-1 * motionDistances[4]);
            }
            currentMotionDistances[0]--;
            if (currentMotionDistances[0] == -29) {
                currentMotionDistances[0] = (int) magicNumber;
            }
        }

        if (motionTypes[2]) {
            // up only
            System.out.println("up");
            moveY(motionDistances[2]);
        } else if (motionTypes[3]) {
            // down only
            System.out.println("down");
            moveY(-1 * motionDistances[3]);
        } else if (motionTypes[5]) {
            // up-down
            if (currentMotionDistances[1] > 0) {
                // move down
                moveY(-1 * motionDistances[5]);
            } else {
                // move up
                moveY(motionDistances[5]);
            }
            currentMotionDistances[1]--;
            if (currentMotionDistances[1] == -29) {
                currentMotionDistances[1] = (int) magicNumber;
            }
        }
    }
}
