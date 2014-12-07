package io.github.alyphen.ld31.action;

import io.github.alyphen.ld31.objects.characters.GameCharacter;

public abstract class CharacterAction extends Action {

    private GameCharacter character;

    public CharacterAction(GameCharacter character) {
        this.character = character;
    }

    public GameCharacter getCharacter() {
        return character;
    }

}
