package com.zhen.game.input;

public class Controller {
    public double x, y, z, rotationX,rotationY, xa, za, rotationXA, rotationYA;
    public static boolean turnLeft = false;
    public static boolean turnRight = false;
    public static boolean lookUp = false;
    public static boolean lookDown = false;
    public static boolean walking = false;
    public static double shakeSpeed = 20;
    public void tick(boolean forward, boolean back, boolean left, boolean right, boolean jump, boolean crouch, boolean sprint) {
        double rotationSpeed = 0.05;
        double walkSpeed = 1;
        double jumpHeight = 0.8;
        double crouchHeight = 0.5;
        double zMove = 0;
        double xMove = 0;

        if (forward){
            zMove++;
            walking = true;
            shakeSpeed = 5;
        }
        if (back){
            zMove--;
            walking = true;
            shakeSpeed = 5;
        }
        if (left){
            xMove--;
            walking = true;
            shakeSpeed = 7;
        }
        if (right){
            xMove++;
            walking = true;
            shakeSpeed = 7;
        }
        if (turnRight)
            rotationXA+= rotationSpeed;
        if (turnLeft)
            rotationXA-= rotationSpeed;
        if (lookDown)
            rotationYA+= rotationSpeed;
        if (lookUp)
            rotationYA-= rotationSpeed;
        if (jump){
            y += jumpHeight;
            walking = false;
        }

        boolean inMove = forward || back || right || left;

        if (crouch && !sprint && !jump && inMove){
            walkSpeed = 0.2;
            y -= crouchHeight;
            shakeSpeed = 10;
        }
        if (sprint && !crouch && !jump && inMove){
            walkSpeed = 1.5;
            shakeSpeed = 2;
        }
        if (!forward && !back && !right && !left && !crouch && !sprint)
            walking = false;

        xa = (xMove * Math.cos(rotationX) + zMove * Math.sin(rotationX)) * walkSpeed;
        za = (zMove * Math.cos(rotationX) - xMove * Math.sin(rotationX)) * walkSpeed;

        x += xa;
        z += za;

        y*=0.9;
        xa*= 0.1;
        za*= 0.1;

        rotationX += rotationXA;
        rotationY += rotationYA;

        rotationX *= 0.5;
        rotationY *= 0.5;
    }
}
