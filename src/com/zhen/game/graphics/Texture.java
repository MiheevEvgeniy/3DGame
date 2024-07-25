package com.zhen.game.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Texture {
    public static Render floor = loadBitMap("/textures/pile.png");
    public static Render wall = loadBitMap("/textures/wall.png");
    public static Render loadBitMap(String fileName) {
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(Texture.class.getResource(fileName)));
            int width = image.getWidth();
            int height = image.getHeight();

            Render result = new Render(width, height);

            image.getRGB(0,0, width, height,  result.PIXELS, 0, width);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("load image error", e);
        }
    }

}
