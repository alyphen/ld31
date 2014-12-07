package io.github.alyphen.ld31.objects.furniture;

import io.github.alyphen.ld31.LD31;
import io.github.alyphen.ld31.objects.GameObject;

public class Chair extends GameObject {

    public Chair(LD31 game, int x, int y, int depth) {
        super(x, y, 48, 48, depth, game.getTexture("chair"));
    }

}
