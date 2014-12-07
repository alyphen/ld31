package io.github.alyphen.ld31.objects.furniture;

import io.github.alyphen.ld31.LD31;
import io.github.alyphen.ld31.objects.GameObject;

public class Fireplace extends GameObject {

    public Fireplace(LD31 game, int x, int y, int depth) {
        super(x, y, 64, 176, depth, game.getTexture("fireplace"));
    }

}
