package io.github.alyphen.ld31.objects.furniture;

import io.github.alyphen.ld31.LD31;
import io.github.alyphen.ld31.objects.GameObject;

public class Desk extends GameObject {

    public Desk(LD31 game, int x, int y, int depth) {
        super(x, y, 64, 48, depth, game.getTexture("desk"));
    }
}
