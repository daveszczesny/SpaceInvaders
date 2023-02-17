import javax.swing.ImageIcon;
import java.awt.*;
import java.util.ArrayList;

/*
 * Spaceship class inherits from Sprite2D
 * Class used for player
 */

public class Spaceship extends Sprite2D {

    private int direction = 0;
    public ArrayList<PlayerBullet> bullets = new ArrayList<>();
    private int level = 0;
    private int score = 0;
    private int maxBullets = 100;

    // constructor
    // sets initial player position, and speed
    public Spaceship(ImageIcon image, Dimension WindowSize) {
        super(image);
        this.setPosition(WindowSize.width / 2, WindowSize.height - 80);
    }

    // method controlled by keybinds
    public void setDirection(int d) {
        this.direction = d;
    }

    public void move() {
        this.x += (this.xSpeed * direction);
    }

    public int getBulletsRemaining() {
        return maxBullets - bullets.size();
    }
    public int getMaxBullets() { return maxBullets;}

    public void shoot() {
        if (bullets.size() > maxBullets)
            return;

        PlayerBullet bullet = new PlayerBullet(this);
        bullets.add(bullet);
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public void incrementScore(int score){
        this.score += score;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
