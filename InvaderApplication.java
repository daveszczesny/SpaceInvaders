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

    // constants
    private static final Dimension WindowSize = new Dimension(800, 600);

    private Alien[] alienArray = new Alien[30];
    private Spaceship spaceship;

    private BufferStrategy strategy;

    // constructor
    public InvaderApplication() {
        setWindow(); // sets up window
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        createEntities();

        // required for key listener
        addKeyListener(this);

        // starts thread
        Thread thread = new Thread(this);
        thread.start();

    }

    public void createEntities()
    {   
        int j = 0;
        for(int i =0; i < alienArray.length; i++){
            if (i%6 == 0){
                j++;
            }
            alienArray[i] = new Alien();
            alienArray[i].setPosition(
                (i%6)*60,
                 (j*60) + 20);
        }


        spaceship = new Spaceship(new ImageIcon("res/player_ship.png"), WindowSize);
    }

    // keyListener
    public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_LEFT){
                this.spaceship.setDirection(-1);
            }
            if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                this.spaceship.setDirection(1);
            }

    }

    // listens for when a key is released
    // undoes keyPressed events
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT){
            this.spaceship.setDirection(0);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    // Game loop
    public void run() {
        while (true) {
            this.repaint();
            for(Alien alien: alienArray){
                alien.swapImage();
                if(alien.x < 0 || alien.x +50 > 800){
                    alien.reverseDirection();
                    for(Alien a: alienArray){
                        a.y += 2;
                    }
                }
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Game graphics
    public void paint(Graphics g) {

        // moves and paints all aliens and the player
        try {
            g = strategy.getDrawGraphics();
            // fills background (black)
            g.setColor(Color.black);
            g.fillRect(0, 0, WindowSize.width, WindowSize.height);

            for(Alien alien: alienArray){
                alien.paint(g);
            }
            spaceship.paint(g);

            strategy.show();
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width / 2 - WindowSize.width / 2;
        int y = screensize.height / 2 - WindowSize.height / 2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        InvaderApplication app = new InvaderApplication();
    }
}