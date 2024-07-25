package com.zhen.game.graphics;

import com.zhen.game.Game;

import java.util.Random;

public class Screen extends Render{
    private Render3D render3D;

    public Screen(int WIDTH, int HEIGHT) {
        super(WIDTH, HEIGHT);
        render3D = new Render3D(WIDTH, HEIGHT);
    }
    
    public void render(Game game){
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            PIXELS[i] = 0;
        }
        render3D.renderFloor(game);
        render3D.createWalls(0,0.5,1.5, 1.5,0);
        render3D.renderDistanceLimiter();
        draw(render3D, 0 , 0);
    }
}
