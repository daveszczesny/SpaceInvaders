import javax.swing.*;

/*
 * Alien is a subclass of Sprite2D,
 * Aliens move in unison, hence direction and xSpeed are static variables
 */

public class Alien extends Sprite2D {

    // static variables (allow aliens to move in unison)
    private static int direction = 1;
    private static int xSpeed = 0;

    private int counter = 0;

    private static ImageIcon[] images = {
        new ImageIcon("res/alien_ship_1.png"),
        new ImageIcon("res/alien_ship_2.png")
    };

    public Alien() {
        // inherits sprite2D
        super(images[0]);
        // setting static variable speed
        Alien.xSpeed = 10 * Alien.direction;
    }

    public void move() {
        this.x += (xSpeed * direction);
    }


    public void swapImage(){
        if(counter == 0){
            setImage(images[counter++]);
        }else{
            setImage(images[counter--]);
        }
    }

    // reverseDirection method used to change the direction of alien movement
    public void reverseDirection() {
        Alien.direction *= -1;
    }
}
