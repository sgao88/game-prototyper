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
        x = 10;
        y = 220;
        radius = 50;
        bounds = new Rectangle(x-(radius/2), y-(radius/2), radius, radius);
    }

    public void move() {
        if (x + dx >= 10 && x + dx <= 680) { //make sure it doesn't run off page
            x = x + dx;
            bounds.x = x;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() { return y; }

    public int getRadius() {
        return radius;
    }

    public Rectangle getBounds() { return bounds; }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -1;
        }
        else if (key == KeyEvent.VK_RIGHT) {
            dx = 1;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        } else if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }
}