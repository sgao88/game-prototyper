import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Platform {
    private Rectangle boundingBox;

    public Platform(Rectangle b) {
        boundingBox = b;
    }

    public void move(int x, int y) {
        boundingBox.setLocation(x, y);
    }
}
