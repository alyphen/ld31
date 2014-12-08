package io.github.alyphen.ld31.action;

import com.badlogic.gdx.utils.Array;
import io.github.alyphen.ld31.message.AnswerListener;
import io.github.alyphen.ld31.message.Question;
import io.github.alyphen.ld31.screen.GameScreen;

public class QuestionAction extends Action {

    private GameScreen game;
    private String text;
    private Array<String> answers;
    private Question question;
    private Array<AnswerListener> listeners;

    public QuestionAction(GameScreen game, String text, Array<String> answers) {
        this.game = game;
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
            question = game.showQuestion(text, answers);
            for (AnswerListener listener : listeners) {
                question.addListener(listener);
            }
        }
    }

    public QuestionAction addListener(AnswerListener listener) {
        listeners.add(listener);
        return this;
    }
}
