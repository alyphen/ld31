package io.github.alyphen.ld31.action;

import com.badlogic.gdx.utils.Array;
import io.github.alyphen.ld31.message.AnswerListener;
import io.github.alyphen.ld31.message.Question;
import io.github.alyphen.ld31.objects.characters.GameCharacter;

public class AskAction extends CharacterAction {

    private String text;
    private String[] answers;
    private Question question;
    private Array<AnswerListener> listeners;

    public AskAction(GameCharacter character, String text, String... answers) {
        super(character);
        this.text = text;
        this.answers = answers;
        listeners = new Array<>();
    }

    @Override
    public boolean isComplete() {
        return question != null && question.hasShown();
    }

    @Override
    public void onTick() {
        if (question == null) {
            question = getCharacter().ask(text, answers);
            for (AnswerListener listener : listeners) {
                question.addListener(listener);
            }
        }
    }

    public AskAction addListener(AnswerListener listener) {
        listeners.add(listener);
        return this;
    }
}
