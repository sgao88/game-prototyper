import org.pushingpixels.radiance.animation.api.Timeline;
import java.awt.*;

public abstract class DrawnObject {
    Rectangle boundingBox;
    Timeline animation;

    public DrawnObject() {
    }

    public void move(int dx) {
        int currX = (int)boundingBox.getX();
        boundingBox.setLocation(currX - dx, (int)boundingBox.getY());
    }

    public void moveY(int dy) {
        int currY = (int)boundingBox.getY();
        boundingBox.setLocation((int)boundingBox.getX(), currY - dy);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}
