package com.zhen.game;

import com.zhen.game.input.Controller;

import java.awt.event.KeyEvent;

public class Game {
    public double time;
    public Controller controller;

    public Game() {
        controller = new Controller();
    }

    public void tick(boolean[] key){
        time+=0.3;
        boolean forward = key[KeyEvent.VK_W];
        boolean right = key[KeyEvent.VK_D];
        boolean left = key[KeyEvent.VK_A];
        boolean back = key[KeyEvent.VK_S];
        boolean jump = key[KeyEvent.VK_SPACE];
        boolean crouch = key[KeyEvent.VK_CONTROL];
        boolean sprint = key[KeyEvent.VK_SHIFT];

        controller.tick(forward,back, left, right, jump, crouch, sprint);
    }
}
