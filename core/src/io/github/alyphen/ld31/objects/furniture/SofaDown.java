package io.github.alyphen.ld31.objects.furniture;

import io.github.alyphen.ld31.LD31;
import io.github.alyphen.ld31.objects.GameObject;

public class SofaDown extends GameObject {
    public SofaDown(LD31 game, int x, int y, int depth) {
        super(x, y, 48, 96, depth, game.getTexture("sofa_down"));
    }
}
