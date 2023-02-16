/*
 * @author: Dawid Szczesny
 * @id: 21300293
 * @date: 20/01/2023
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;

/*
 * Assignment 3
 * Starting Space invaders
 */
public class InvaderApplication extends JFrame implements Runnable, KeyListener {

    // game states
    enum GameState {
        MENU, GAME, INFO
    }

    // constants
    private static final Dimension WindowSize = new Dimension(800, 600);
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

        if (minx < 0 || maxx > 800) {
            Alien.reverseDirection();
            for (Alien alien : alienArray)
                alien.y += Alien.yJump;
        }

        checkCollisions();
        if (newWave) {
            // gerernateWave -> speed of aliens, speed function = 20 log(level) + 15, 15 =
            // starting speed
            spaceship.setLevel(spaceship.getLevel() + 1);
            gerernateWave((int)Math.log(spaceship.getLevel()) * 20 + 15);
        }
    }

    public void gerernateWave(int speed) {
        alienArray = new Alien[30];
        int j = 0;
        for (int i = 0; i < alienArray.length; i++) {
            if (i % 6 == 0) {
                j++;
            }
            alienArray[i] = new Alien(WindowSize);
            alienArray[i].setPosition(
                    (i % 6) * 60 + 200,
                    (j * 60));
        }
        Alien.setSpeed(speed);
    }

    public void createEntities() {
        alienArray = new Alien[30];
        int j = 0;
        for (int i = 0; i < alienArray.length; i++) {
            if (i % 6 == 0) {
                j++;
            }
            alienArray[i] = new Alien(WindowSize);
            alienArray[i].setPosition(
                    (i % 6) * 60 + 200,
                    (j * 60));
        }
        spaceship = new Spaceship(new ImageIcon("res/player_ship.png"), WindowSize);
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
                System.out.println("Player dies");
            }
            for (PlayerBullet bullet : spaceship.bullets) {
                if (!bullet.isVisible)
                    continue;
                Rectangle bullet_rect = bullet.getBounds();
                if (bullet_rect.intersects(alien_rect)) {
                    bullet.setVisible(false);
                    alien.setVisible(false);
                }
            }
        }
    }

    public void game_paint(Graphics g) {
        for (Alien alien : alienArray) {
            alien.paint(g);
        }
        spaceship.paint(g);
        for (PlayerBullet bullet : spaceship.bullets) {
            bullet.paint(g);
        }
    }

    // Game graphics
    public void paint(Graphics g) {

        try {

            if (state == GameState.GAME) {

                g = offscreenGraphics;
                g.setColor(Color.black);
                g.fillRect(0, 0, WindowSize.width, WindowSize.height);
                game_paint(g);

                strategy.show();
            } else if (state == GameState.MENU) {
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

            // game_paint(g);

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