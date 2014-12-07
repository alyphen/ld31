package io.github.alyphen.ld31.action;

import io.github.alyphen.ld31.message.Message;
import io.github.alyphen.ld31.screen.GameScreen;

public class NarrativeAction extends Action {

    private GameScreen game;
    private String text;
    private Message message;

    public NarrativeAction(GameScreen game, String text) {
        this.game = game;
        this.text = text;
    }

    @Override
    public boolean isComplete() {
        return message != null && message.hasShown();
    }

    @Override
    public void onTick() {
        if (message == null) message = game.showMessage(text);
    }

}
