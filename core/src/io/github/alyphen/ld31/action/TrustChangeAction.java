package io.github.alyphen.ld31.action;

import io.github.alyphen.ld31.objects.characters.GameCharacter;

public class TrustChangeAction extends CharacterAction {

    private int trustChange;
    private boolean complete;

    public TrustChangeAction(GameCharacter character, int trustChange) {
        super(character);
        this.trustChange = trustChange;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public void onTick() {
        getCharacter().setTrust(getCharacter().getTrust() + trustChange);
        complete = true;
    }
}
