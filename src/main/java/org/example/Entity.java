package org.example;

public abstract class Entity {
    public int animationIndex;

    public float x, y;
    public float moveSpeed;
    public boolean moving;
    public boolean flipped;

    Entity(float _x, float _y, float _moveSpeed) {
        this.x = _x;
        this.y = _y;
        this.moveSpeed = _moveSpeed;
        this.moving = false;
        this.animationIndex = 0;
        this.flipped = false;
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

    public void handleInput(Main game) {
        
    }

    public void handleAnimationState() {

    }

    public void draw(Main game) {

    }

    public void update(Main game) {

    }
}
