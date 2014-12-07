package io.github.alyphen.ld31.action;

import io.github.alyphen.ld31.objects.characters.GameCharacter;

public class DepthChangeAction extends CharacterAction {

    private int depth;

    public DepthChangeAction(GameCharacter character, int depth) {
        super(character);
        this.depth = depth;
    }

    @Override
    public boolean isComplete() {
        return getCharacter().getDepth() == depth;
    }

    @Override
    public void onTick() {
        getCharacter().setDepth(depth);
    }

}
