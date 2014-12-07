package io.github.alyphen.ld31.objects.furniture;

import io.github.alyphen.ld31.LD31;
import io.github.alyphen.ld31.objects.GameObject;

public class Curtains extends GameObject {

    public Curtains(LD31 game, int x, int y, int depth) {
        super(x, y, 96, 64, depth, game.getTexture("curtains"));
    }

}
