import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainCharacter {
    int x, dx; //current x coordinate and change in x that needs to happen
    int y;
    int radius;

    public MainCharacter() {
        x = 35;
        y = 220;
        radius = 50;
    }

    public void move() {
        x = x + dx;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

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
        }
        else if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }
}