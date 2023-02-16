import javax.swing.*;

public class PlayerBullet extends Sprite2D{
    

    private static ImageIcon image = new ImageIcon("res/bullet.png");

    public PlayerBullet(Spaceship player){
        super(image);
        this.setPosition(player.x 
            + (int)(player.image.getWidth(null)/2),
             player.y);
    }

    public void move(){
        this.y -= this.xSpeed;
    }

  

}
