package org.example;

public class Entity {
    public int animationIndex;

    Entity() {

    }

    public int animate(Texture[] texArray, int animationIndex, int timeToDisplay) {
        if (animationIndex + 1 >= texArray.length * timeToDisplay) {
            animationIndex = 0;
        }
        animationIndex += 1;
        return animationIndex;
    }

    public void draw() {

    }
}
