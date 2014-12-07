package io.github.alyphen.ld31.objects.furniture;

import io.github.alyphen.ld31.LD31;
import io.github.alyphen.ld31.objects.GameObject;

public class RugCandle extends GameObject {
    public RugCandle(LD31 game, int x, int y, int depth) {
        super(x, y, 80, 80, depth, game.getTexture("rug_candle"));
    }
}
