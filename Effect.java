import java.awt.*;

public class Effect extends DrawnObject {
    private boolean reward; //true == reward, false == penalty
    private boolean visible;
    private int cost; //always positive number even if the Effect is a penalty

    public Effect(Rectangle b, boolean r, int c) {
        boundingBox = b;
        reward = r;
        visible = true;
        cost = c;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int c) {
        cost = c;
    }
}
