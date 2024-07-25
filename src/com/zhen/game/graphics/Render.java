package com.zhen.game.graphics;

import com.zhen.game.Display;

public class Render {
    public final int WIDTH;
    public final int HEIGHT;
    public final int[] PIXELS;

    public Render(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        PIXELS = new int[WIDTH*HEIGHT];
    }

    public void draw(Render render, int xOffset, int yOffset){
        for (int y = 0; y < HEIGHT; y++) {
            int yPix = y + yOffset;
            if (yPix < 0 || yPix >= HEIGHT) {
                continue;
            }
            for (int x = 0; x < WIDTH; x++) {
                int xPix = x + xOffset;
                if (xPix < 0 || xPix >= WIDTH) {
                    continue;
                }

                int alpha = render.PIXELS[x + y * render.WIDTH];
                if (alpha > 0) {
                    PIXELS[xPix + yPix * WIDTH] = alpha;
                }
            }
        }
    }
}
