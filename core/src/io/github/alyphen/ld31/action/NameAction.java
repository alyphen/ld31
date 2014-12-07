package io.github.alyphen.ld31.action;

import io.github.alyphen.ld31.objects.characters.GameCharacter;

public class NameAction extends CharacterAction {

    public NameAction(GameCharacter character) {
        super(character);
    }

    @Override
    public boolean isComplete() {
        return getCharacter().isNameKnown();
    }

    @Override
    public void onTick() {
        getCharacter().setNameKnown(true);
    }

}
