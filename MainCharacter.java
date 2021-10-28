import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainCharacter {
    int x, dx; //current x coordinate and change in x that needs to happen
    int y;
    int radius;
    Rectangle bounds;

    public MainCharacter() {
        x = 30;
        y = 270;
        radius = 50;
        bounds = new Rectangle(x-(radius/2), y-(radius/2), radius, radius);
    }

    public int getX() {
        return x;
    }

    public int getY() { return y; }

    public int getRadius() {
        return radius;
    }

    public Rectangle getBounds() { return bounds; }
}