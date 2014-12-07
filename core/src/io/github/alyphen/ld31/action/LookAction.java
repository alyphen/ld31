package io.github.alyphen.ld31.action;

import io.github.alyphen.ld31.objects.characters.GameCharacter;

public class LookAction extends CharacterAction {

    private String direction;

    public LookAction(GameCharacter character, String direction) {
        super(character);
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public boolean isComplete() {
        return getCharacter().getDirection().equals(getDirection());
    }

    @Override
    public void onTick() {
        getCharacter().setDirection(getDirection());
    }
}
