import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Enemy {
    private Rectangle boundingBox;

    public Enemy(Rectangle b) {
        boundingBox = b;
    }

    public void move(int x, int y) {
        boundingBox.setLocation(x, y);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}
