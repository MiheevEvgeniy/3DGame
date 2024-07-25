package com.zhen.game.graphics;

import com.zhen.game.Game;
import com.zhen.game.input.Controller;

public class Render3D extends Render {
    public double[] zBuffer;
    public double RENDER_DISTANCE = 8000.0;
    public final int FLOOR_POSITION = 8;
    public final int CEILING_POSITION = 8;
    public final int COLOR_SHIFT = 16;
    private double forward, right, up, cosine, sine, walking;

    public Render3D(int WIDTH, int HEIGHT) {
        super(WIDTH, HEIGHT);
        zBuffer = new double[WIDTH * HEIGHT];
    }

    public void renderFloor(Game game) {
        double ceiling;
        double depth;
        double z;

        forward = game.controller.z;
        right = game.controller.x;
        up = game.controller.y;
        walking = 0;
        double rotationX = game.controller.rotationX;
        cosine = Math.cos(rotationX);
        sine = Math.sin(rotationX);
        for (int y = 0; y < HEIGHT; y++) {
            ceiling = (y - HEIGHT / 2.0) / HEIGHT;

            if (Controller.walking) {
                walking = Math.sin(game.time / Controller.shakeSpeed) * 1;
                z = (FLOOR_POSITION + up + walking) / ceiling;
            } else {
                z = (FLOOR_POSITION + up) / ceiling;
            }

            if (ceiling < 0) {
                if (Controller.walking) {
                    z = (CEILING_POSITION - up - walking) / -ceiling;
                } else {
                    z = (CEILING_POSITION - up) / -ceiling;
                }
            }


            for (int x = 0; x < WIDTH; x++) {


                depth = ((x - WIDTH / 2.0) / HEIGHT) * z;
                double xx = depth * cosine + z * sine;
                double yy = z * cosine - depth * sine;

                int xPix = (int) (xx + right); // left-right
                int yPix = (int) (yy + forward); // back-forward

                int texX = (xPix % 16 + 16) & 15;
                int texY = (yPix % 16 + 16) & 15;

                zBuffer[x + y * WIDTH] = z;

                try {
                    PIXELS[x + y * WIDTH] = Texture.floor.PIXELS[texX + texY * 16];
                } catch (Exception e){
                    System.out.println("error " + e.getMessage());
                }
            }
        }
    }

    public void createWalls(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {
        double correct =  (1.0 / (CEILING_POSITION + FLOOR_POSITION));

        double xcLeft = ((xLeft) - (right * correct)) * 2;
        double zcLeft = ((zDistanceLeft) - (forward * correct)) * 2;

        double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
        double yCornerTL = ((-yHeight) - (-up * correct + walking * -correct)) * 2;
        double yCornerBL = ((0.5 - yHeight) - (-up * correct + walking * -correct)) * 2;
        double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

        double xcRight = ((xRight) - (right * correct)) * 2;
        double zcRight = ((zDistanceRight) - (forward * correct)) * 2;

        double rotRightSideX = xcRight * cosine - zcRight * sine;
        double yCornerTR = ((-yHeight) - (-up * correct + walking * -correct)) * 2;
        double yCornerBR = ((0.5 - yHeight) - (-up * correct + walking * -correct)) * 2;
        double rotRightSideZ = zcRight * cosine + xcRight * sine;

        double xPixelLeft = (rotLeftSideX / rotLeftSideZ * HEIGHT + WIDTH / 2.0);
        double xPixelRight = (rotRightSideX / rotRightSideZ * HEIGHT + WIDTH / 2.0);

        if (xPixelLeft >= xPixelRight)
            return;
        int xPixelLeftInt = (int) xPixelLeft;
        int xPixelRightInt = (int) xPixelRight;

        if (xPixelLeftInt < 0)
            xPixelLeftInt = 0;
        if (xPixelRightInt > WIDTH)
            xPixelRightInt = WIDTH;

        double yPixelLeftTop = yCornerTL / rotLeftSideZ * HEIGHT + HEIGHT / 2.0;
        double yPixelLeftBottom = yCornerBL / rotLeftSideZ * HEIGHT + HEIGHT / 2.0;
        double yPixelRightTop = yCornerTR / rotRightSideZ * HEIGHT + HEIGHT / 2.0;
        double yPixelRightBottom = yCornerBR / rotRightSideZ * HEIGHT + HEIGHT / 2.0;

        double tex1 = 1 / rotLeftSideZ;
        double tex2 = 1 / rotRightSideZ;
        double tex3 = 0 / rotLeftSideZ;
        double tex4 = 16 / rotRightSideZ - tex3;

        for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
            double pixRot = (x - xPixelLeft) / (xPixelRight - xPixelLeft);

            int xTexture = (int) ((tex3 + tex4 * pixRot) / (tex1 + (tex2 - tex1) * pixRot));

            double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixRot;
            double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixRot;

            int yPixelTopInt = (int) (yPixelTop);
            int yPixelBottomInt = (int) (yPixelBottom);

            if (yPixelTopInt < 0)
                yPixelTopInt = 0;
            if (yPixelBottomInt > HEIGHT)
                yPixelBottomInt = HEIGHT;

            for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
                double pixRotY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
                int yTexture = (int) (16 * pixRotY);

                try {
                    PIXELS[x + y * WIDTH] = Texture.wall.PIXELS[(xTexture % 16) + (yTexture % 16) * 16];
                    zBuffer[x + y * WIDTH] = 1 / (tex1 + (tex2 - tex1) * pixRot) * 8;
                } catch (Exception ex){
                    System.out.println("error " + ex.getMessage());
                }
            }
        }
    }

    public void renderDistanceLimiter() {
        int color;
        int brightness;
        int r, g, b;
        for (int i = 0; i < WIDTH * HEIGHT; i++) {
            color = PIXELS[i];
            brightness = (int) (RENDER_DISTANCE / zBuffer[i]);

            if (brightness < 0)
                brightness = 0;
            if (brightness > 255)
                brightness = 255;
            r = ((color >> 16) & 0xff) * brightness / 255;
            g = ((color >> 8) & 0xff) * brightness / 255;
            b = (color & 0xff) * brightness / 255;
            PIXELS[i] = r << 16 | g << 8 | b;
        }
    }
}
