package io.github.alyphen.ld31.objects.furniture;

import io.github.alyphen.ld31.LD31;
import io.github.alyphen.ld31.objects.GameObject;

public class Sink extends GameObject {
    public Sink(LD31 game, int x, int y, int depth) {
        super(x, y, 64, 32, depth, game.getTexture("sink"));
    }
}
