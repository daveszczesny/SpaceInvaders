import javax.swing.ImageIcon;
import java.awt.*;
import java.util.ArrayList;

/*
 * Spaceship class inherits from Sprite2D
 * Class used for player
 */

public class Spaceship extends Sprite2D{
    
    private int direction = 0;
    public  ArrayList<PlayerBullet> bullets = new ArrayList<>();
    private int level = 0;
    // constructor
    // sets initial player position, and speed
    public Spaceship(ImageIcon image, Dimension WindowSize){
        super(image);
        this.setPosition(WindowSize.width / 2, WindowSize.height - 80);
    }

    // method controlled by keybinds
    public void setDirection(int d){
        this.direction = d;
    }

    public void move(){
        this.x += (this.xSpeed * direction);
    }

    public void shoot(){
        PlayerBullet bullet = new PlayerBullet(this);
        bullets.add(bullet);
    }
    public int getLevel(){
        return level;
    }
    public void setLevel(int level){
        this.level = level;
    }

}
