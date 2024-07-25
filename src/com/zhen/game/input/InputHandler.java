package com.zhen.game.input;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener {
    public boolean[] key = new boolean[68836];
    public static int mouseX;
    public static int mouseY;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode > 0 && keyCode < key.length) {
            key[keyCode] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode > 0 && keyCode < key.length) {
            key[keyCode] = false;
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
//        Arrays.fill(key, false);
    }

    @Override
    public void focusLost(FocusEvent e) {
        Arrays.fill(key, false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        try {
            Robot robot = new Robot();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int centerX = screenSize.width/2;
            int centerY = screenSize.height/2;
            robot.mouseMove(centerX, centerY);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
