import javax.swing.*;

import java.awt.*;

/*
 * Alien is a subclass of Sprite2D,
 * Aliens move in unison, hence direction and xSpeed are static variables
 */

public class Alien extends Sprite2D {

    // static variables (allow aliens to move in unison)
    private static int direction = 1;
    private static int xSpeed = 15;
    public static int yJump = 15;
    private static int windowWidth, windowHeight;
    private int counter = 0;

    private static ImageIcon[] images = {
            new ImageIcon("res/alien_ship_1.png"),
            new ImageIcon("res/alien_ship_2.png")
    };

    public Alien(Dimension windowSize) {
        // inherits sprite2D
        super(images[0]);
        // setting static variable speed
        Alien.xSpeed *= Alien.direction;
        Alien.windowWidth = windowSize.width;
        Alien.windowHeight = windowSize.height;
    }

    public void move() {

        if (this.y < 0) {
            this.setVisible(false);
        }

        this.x += (xSpeed * direction);
    }

    public void swapImage() {
        if (counter == 0) {
            setImage(images[counter++]);
        } else {
            setImage(images[counter--]);
        }
    }

    

    // reverseDirection method used to change the direction of alien movement
 
    public static void reverseDirection(){
        Alien.direction *= -1;
    }

    public static void setSpeed(int speed){
        Alien.xSpeed = speed;
        Alien.yJump = (int)(speed * .8);
    }

}
