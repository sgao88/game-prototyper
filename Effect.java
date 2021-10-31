import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Effect extends DrawnObject {
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
}
