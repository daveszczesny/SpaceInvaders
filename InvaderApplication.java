/*
 * @author: Dawid Szczesny
 * @id: 21300293
 * @date: 20/01/2023
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.ArrayList;

/*
 * Finishing Space invaders
 */
public class InvaderApplication extends JFrame implements Runnable, KeyListener {

    // game states
    enum GameState {
        MENU, GAME
    }

    // constants
    private static final Dimension WindowSize = new Dimension(800, 600);
    private final int MAX_ALIENS = 30;
    private Alien[] alienArray;
    private Spaceship spaceship;
    private Graphics offscreenGraphics;
    private GameState state = GameState.MENU;
    public int minx, maxx;

    private BufferStrategy strategy;

    // constructor
    public InvaderApplication() {

        setWindow(); // sets up window
        // required for key listener

        addKeyListener(this);

        // // starts thread
        Thread thread = new Thread(this);
        thread.start();

        createBufferStrategy(2);
        strategy = getBufferStrategy();

        offscreenGraphics = strategy.getDrawGraphics();
    }

    // Game run loop
    public void game() {
        minx = 400;
        maxx = 400;

        boolean newWave = true;

        for (Alien alien : alienArray) {
            if (alien == null)
                continue;

            if (alien.x < minx)
                minx = alien.x;
            if (alien.x + alien.getImageDimension()[0] > maxx)
                maxx = alien.x + alien.getImageDimension()[0];

            if (alien.isVisible)
                newWave = false;

            alien.swapImage();
        }

        if (minx < 0 || maxx > WindowSize.getWidth()) {
            Alien.reverseDirection();
            for (Alien alien : alienArray)
                alien.y += Alien.yJump;
        }

        checkCollisions();
        if (newWave) {
            // gerernateWave -> speed of aliens, speed function = 20 log(level) + 15, 15 =
            // starting speed
            if(spaceship.getBulletsRemaining() > 0){
                spaceship.incrementScore(spaceship.getBulletsRemaining() * 10);
            }

            spaceship.setLevel(spaceship.getLevel() + 1);
            gerernateWave((int) Math.log(spaceship.getLevel()) * 20 + 15);
        }
    }

    public void gerernateWave(int speed) {
        alienArray = new Alien[MAX_ALIENS];
        int j = 0;
        for (int i = 0; i < alienArray.length; i++) {
            if (i % 6 == 0) {
                j++;
            }
            alienArray[i] = new Alien(WindowSize);
            alienArray[i].setPosition(
                    (i % 6) * 60,
                    (j * 60));
        }
        Alien.setSpeed(speed);

        spaceship.bullets = new ArrayList<PlayerBullet>();
    }

    public void createEntities() {
        spaceship = new Spaceship(new ImageIcon("res/player_ship.png"), WindowSize);
        gerernateWave((int) Math.log(spaceship.getLevel()) * 20 + 15);
    }

    // Game loop
    public void run() {
        while (true) {

            if (state == GameState.GAME) {
                game();
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.repaint();
        }
    }

    // keyListener
    public void keyPressed(KeyEvent e) {
        if (state == GameState.GAME) {

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                this.spaceship.setDirection(-1);
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                this.spaceship.setDirection(1);
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                // create Bullet entity
                spaceship.shoot();

            }
        } else if (state == GameState.MENU) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                createEntities();
                state = GameState.GAME;
            }
        }

    }

    // listens for when a key is released
    // undoes keyPressed events
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.spaceship.setDirection(0);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    private void checkCollisions() {
        Rectangle spaceship_rect = spaceship.getBounds();
        for (Alien alien : alienArray) {
            Rectangle alien_rect = alien.getBounds();
            if (!alien.isVisible)
                continue;
            if (spaceship_rect.intersects(alien_rect)) {
                spaceship.setVisible(false);
                state = GameState.MENU;
            }
            for (PlayerBullet bullet : spaceship.bullets) {
                if (!bullet.isVisible)
                    continue;
                Rectangle bullet_rect = bullet.getBounds();
                if (bullet_rect.intersects(alien_rect)) {
                    bullet.setVisible(false);
                    alien.setVisible(false);
                    spaceship.incrementScore();
                }
            }
        }
    }

    public void game_paint(Graphics g) {
        g = offscreenGraphics;
        g.setColor(Color.black);
        g.fillRect(0, 0, WindowSize.width, WindowSize.height);
        for (Alien alien : alienArray) {
            alien.paint(g);
        }
        spaceship.paint(g);
        for (PlayerBullet bullet : spaceship.bullets) {
            bullet.paint(g);
        }

        g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 20));
        g.setColor(Color.WHITE);
        g.drawString("Wave: " + (spaceship.getLevel() + 1), 10, 60);
        g.drawString("Score: " + spaceship.getScore(), 10, 80);
        g.drawString("Best: ", 10, 100);

        g.setFont(new Font(g.getFont().getName(), Font.PLAIN, 13));
        g.drawString("Bullets remaining: " + spaceship.getBulletsRemaining(), 10,
                (int) (WindowSize.getHeight() * .97));

        strategy.show();
    }

    public void menu_paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WindowSize.width, WindowSize.height);
        g.setColor(Color.white);
        g.setFont(
                new Font(g.getFont().getName(),
                        Font.PLAIN, 100));
        g.drawString("MENU", (int) WindowSize.getWidth() / 2 - 150,
                (int) (WindowSize.getHeight() * .3));
        g.setFont(
                new Font(g.getFont().getName(),
                        Font.PLAIN, 15));
        g.drawString("Press [SPACE] key to play", (int) WindowSize.getWidth() / 2 - 90,
                (int) (WindowSize.getHeight() * .35));
        g.drawString("[Arrow keys to move, space to shoot]", (int) WindowSize.getWidth() / 2 - 150,
                (int) (WindowSize.getHeight() * .4));
    }

    // Paint method
    public void paint(Graphics g) {

        try {

            if (state == GameState.GAME) {
                game_paint(g);

            } else if (state == GameState.MENU) {
                menu_paint(g);

            }

        } catch (NullPointerException e) {
            repaint();
        }
    }

    // method to set up JFrame window
    /*
     * Sets title
     * Default close operation
     * Window size
     * resizability and visibility
     */
    private void setWindow() {

        this.setTitle("Space Invaders Alpha: 0.01");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width / 2 - WindowSize.width / 2;
        int y = screensize.height / 2 - WindowSize.height / 2;

        this.setBounds(x, y, WindowSize.width, WindowSize.height);
        this.setResizable(true);

        this.setVisible(true);

    }

    public static void main(String[] args) {
        InvaderApplication app = new InvaderApplication();
    }
}