package io.github.alyphen.ld31.action;

import io.github.alyphen.ld31.objects.characters.GameCharacter;
import io.github.alyphen.ld31.screen.GameScreen;

public class CharacterLeaveAction extends CharacterAction {

    private GameScreen game;

    public CharacterLeaveAction(GameCharacter character, GameScreen game) {
        super(character);
        this.game = game;
    }

    @Override
    public boolean isComplete() {
        return !game.getCharacters().contains(getCharacter(), true);
    }

    @Override
    public void onTick() {
        game.removeCharacter(getCharacter());
    }

}
