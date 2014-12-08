package io.github.alyphen.ld31.message;

import com.badlogic.gdx.utils.Array;

public class Question extends Message {

    private Array<String> answers;
    private Array<AnswerListener> listeners;

    public Question(String text, Array<String> answers) {
        super(text);
        this.answers = answers;
        listeners = new Array<>();
    }

    public Array<String> getAnswers() {
        return answers;
    }

    public void selectAnswer(int answerIndex) {
        for (AnswerListener listener : listeners) {
            listener.onAnswer(answerIndex);
        }
    }

    public void addListener(AnswerListener listener) {
        listeners.add(listener);
    }

}
