import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Effect {
    private Rectangle boundingBox;
    private boolean reward; //true == reward, false == penalty

    public Effect(Rectangle b, boolean r) {
        boundingBox = b;
        reward = r;
    }

    public boolean getEffect() {
        return reward;
    }

    public void setEffect(boolean r) {
        reward = r;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void move(int x, int y) {
        boundingBox.setLocation(x, y);
    }
}
