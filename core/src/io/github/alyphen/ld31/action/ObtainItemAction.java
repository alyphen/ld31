package io.github.alyphen.ld31.action;

import io.github.alyphen.ld31.screen.GameScreen;

public class ObtainItemAction extends Action {

    private GameScreen game;
    private String item;

    public ObtainItemAction(GameScreen game, String item) {
        this.game = game;
        this.item = item;
    }

    @Override
    public boolean isComplete() {
        return game.hasItem(item);
    }

    @Override
    public void onTick() {
        if (!game.hasItem(item)) game.giveItem(item);
    }

}
