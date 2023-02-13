import javax.swing.ImageIcon;
import java.awt.*;

/*
 * Spaceship class inherits from Sprite2D
 * Class used for player
 */

public class Spaceship extends Sprite2D{
    
    private int direction = 0;

    // constructor
    // sets initial player position, and speed
    public Spaceship(ImageIcon image, Dimension WindowSize){
        super(image);
        this.setPosition(WindowSize.width / 2, WindowSize.height - 80);
        this.xSpeed = 5;
    }

    // method controlled by keybinds
    public void setDirection(int d){
        this.direction = d;
    }

    public void move(){
        this.x += (this.xSpeed * direction);
    }
}
