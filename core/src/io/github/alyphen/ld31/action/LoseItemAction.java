package io.github.alyphen.ld31.action;

import io.github.alyphen.ld31.screen.GameScreen;

public class LoseItemAction extends Action {

    private GameScreen game;
    private String item;

    public LoseItemAction(GameScreen game, String item) {
        this.game = game;
        this.item = item;
    }

    @Override
    public boolean isComplete() {
        return !game.hasItem(item);
    }

    @Override
    public void onTick() {
        if (game.hasItem(item)) game.takeItem(item);
    }

}
