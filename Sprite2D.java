
import java.awt.*;
import javax.swing.*;

/*
 * Parent class of all 2D sprites
 * Contains position, movement speed, and sprite image
 */

public class Sprite2D {

    protected int x, y;
    protected double xSpeed = 15;
    protected Image image;

    protected boolean isVisible = true;

    public Sprite2D(ImageIcon image) {
        this.image = image.getImage();
    }

    protected void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void setXSpeed(double s) {
        this.xSpeed = s;
    }

    protected void move() {

    }

    protected int[] getImageDimension() {
        int[] imageDimension = {
                this.image.getWidth(null),
                this.image.getHeight(null)
        };
        return imageDimension;
    }

    protected void setImage(ImageIcon image) {
        this.image = image.getImage();
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, getImageDimension()[0], getImageDimension()[1]);
    }

    public Image getImage() {
        return image;
    }

    public void setVisible(boolean foo) {
        isVisible = foo;
    }

    // moves and paints sprite to screen
    protected void paint(Graphics g) {
        if (isVisible) {
            this.move();
            g.drawImage(this.image, x, y, null);
        }
    }

}