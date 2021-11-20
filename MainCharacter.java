import java.awt.*;

public class MainCharacter {
    int x, dx; //current x coordinate and change in x that needs to happen
    int y;
    int radius;
    Rectangle bounds;

    public MainCharacter() {
        x = 10;
        y = 238;
        radius = 50;
        //bounds = new Rectangle(x-(radius/2), y-(radius/2), radius, radius);
        bounds = new Rectangle(x, y, radius, radius);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() { return y; }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int r) {
        radius = r;
    }

    public Rectangle getBounds() { return bounds; }

    public void moveY(int dy) {
        int currY = (int)bounds.getY();
        bounds.setLocation((int) bounds.getX(), currY - dy);
        setY(currY - dy);
    }
}