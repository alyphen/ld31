package io.github.alyphen.ld31.action;

import io.github.alyphen.ld31.message.Message;
import io.github.alyphen.ld31.objects.characters.GameCharacter;

public class SpeechAction extends CharacterAction {

    private String text;
    private Message message;

    public SpeechAction(GameCharacter character, String text) {
        super(character);
        this.text = text;
    }

    @Override
    public boolean isComplete() {
        return message != null && message.hasShown();
    }

    @Override
    public void onTick() {
        if (message == null) message = getCharacter().say(text);
    }

}
