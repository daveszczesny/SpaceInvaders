
import java.awt.*;
import javax.swing.*;

/*
 * Parent class of all 2D sprites
 * Contains position, movement speed, and sprite image
 */

public class Sprite2D {

    protected int x, y;
    protected double xSpeed = 0;
    protected Image image;

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

    protected void setImage(ImageIcon image){
        this.image = image.getImage();
    }


    // moves and paints sprite to screen
    protected void paint(Graphics g) {
        this.move();
        g.drawImage(this.image, x, y, null);
    }

}