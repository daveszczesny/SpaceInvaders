
import javax.swing.ImageIcon;

public class PlayerBullet extends Sprite2D{
    

    private static ImageIcon image = new ImageIcon("res/bullet.png");

    public PlayerBullet(Spaceship player){
        super(image);
        this.setPosition(player.x+25, player.y);
    }

    public void move(){
        this.y -= this.xSpeed;
    }


}
