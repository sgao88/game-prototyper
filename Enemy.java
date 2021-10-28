import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Enemy {
    private Rectangle boundingBox;

    public Enemy(Rectangle b) {
        boundingBox = b;
    }

    public void move(int dx) {
        int currX = (int)boundingBox.getX();
        boundingBox.setLocation(currX - dx, (int)boundingBox.getY());
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}
