package io.github.alyphen.ld31.objects.furniture;

import io.github.alyphen.ld31.LD31;
import io.github.alyphen.ld31.objects.GameObject;

public class SofaRight extends GameObject {
    public SofaRight(LD31 game, int x, int y, int depth) {
        super(x, y, 32, 80, depth, game.getTexture("sofa_right"));
    }
}
