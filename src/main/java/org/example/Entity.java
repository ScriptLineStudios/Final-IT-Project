package org.example;

public class Entity {
    public int animationIndex;

    public float x, y;
    public float moveSpeed;
    boolean moving;

    Entity(float _x, float _y, float _moveSpeed) {
        this.x = _x;
        this.y = _y;
        this.moveSpeed = _moveSpeed;
        this.moving = false;
        this.animationIndex = 0;
    }

    public int animate(Texture[] texArray, int animationIndex, int timeToDisplay) {
        if (animationIndex + 1 >= texArray.length * timeToDisplay) {
            animationIndex = 0;
        }
        animationIndex += 1;
        return animationIndex;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void handleInput() {
        
    }

    public void handleAnimationState() {

    }

    public void draw() {

    }

    public void update() {

    }
}
