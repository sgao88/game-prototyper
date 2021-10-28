import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Effect {
    private Rectangle boundingBox;
    private boolean reward; //true == reward, false == penalty
    private boolean visible;

    public Effect(Rectangle b, boolean r) {
        boundingBox = b;
        reward = r;
        visible = true;
    }

    public boolean getEffect() {
        return reward;
    }

    public void setEffect(boolean r) {
        reward = r;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void move(int dx) {
        int currX = (int)boundingBox.getX();
        boundingBox.setLocation(currX - dx, (int)boundingBox.getY());
    }
}
