package com.zhen.game;

import com.zhen.game.graphics.Screen;
import com.zhen.game.input.Controller;
import com.zhen.game.input.InputHandler;
import jdk.jshell.execution.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Display extends Canvas implements Runnable {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "Walkie Not Talkie";

    private Thread thread;
    private boolean running = false;
    private Screen screen;
    private BufferedImage image;
    private Game game;
    private int[] pixels;
    private InputHandler input;
    private int newX = 0;
    private int newY = 0;
    private int oldX = 0;
    private int oldY = 0;
    private int fps;

    public Display() {
        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        screen = new Screen(WIDTH, HEIGHT);
        game = new Game();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        input = new InputHandler();

        addKeyListener(input);
        addFocusListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void run() {
        int frames = 0;
        double unprocessedSeconds = 0;
        long previousTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;
        long currentTime;
        long passedTime;
        while (running) {
            currentTime = System.nanoTime();
            passedTime = currentTime - previousTime;
            previousTime = currentTime;
            unprocessedSeconds += passedTime / 1_000_000_000.0;

            requestFocus();

            while (unprocessedSeconds > secondsPerTick) {
                tick();
                unprocessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;
                if (tickCount % 60 == 0) {
                    fps = frames;
                    previousTime += 1000;
                    frames = 0;
                }
            }
            if (ticked) {
                render();
                frames++;
            }
            render();
            frames++;
            newX = InputHandler.mouseX;
            if (newX > oldX) {
                Controller.turnLeft = false;
                Controller.turnRight = true;
            }
            if (newX < oldX) {
                Controller.turnLeft = true;
                Controller.turnRight = false;
            }
            if (newX == oldX) {
                Controller.turnLeft = false;
                Controller.turnRight = false;
            }
            oldX = newX;


            newY = InputHandler.mouseY;
            if (newY > oldY) {
                Controller.lookUp = false;
                Controller.lookDown = true;
            }
            if (newY < oldY) {
                Controller.lookUp = true;
                Controller.lookDown = false;
            }
            if (newY == oldY) {
                Controller.lookUp = false;
                Controller.lookDown = false;
            }
            oldY = newY;
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        screen.render(game);

        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            pixels[i] = screen.PIXELS[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        g.setFont(new Font("Verdana", 1, 30));
        g.setColor(Color.yellow);
        g.drawString("FPS: " + fps, 20, 50);
        g.dispose();
        bs.show();
    }

    private void tick() {
        game.tick(input.key);
    }

    public static void main(String[] args) {
        BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");
        Display game = new Display();
        JFrame frame = new JFrame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setTitle(TITLE);
        frame.setCursor(blank);
        game.start();
    }
}
