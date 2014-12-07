package io.github.alyphen.ld31.objects.furniture;

import io.github.alyphen.ld31.LD31;
import io.github.alyphen.ld31.objects.GameObject;

public class RugWelcome extends GameObject {
    public RugWelcome(LD31 game, int x, int y, int depth) {
        super(x, y, 48, 32, depth, game.getTexture("rug_welcome"));
    }
}
