import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Platform {
    private Rectangle boundingBox;

    public Platform(Rectangle b) {
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
