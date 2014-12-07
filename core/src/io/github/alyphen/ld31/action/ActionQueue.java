package io.github.alyphen.ld31.action;

import com.badlogic.gdx.utils.Array;

public class ActionQueue {

    private Array<Action> actions;
    private int actionIndex;

    public ActionQueue(Action... actions) {
        this.actions = new Array<>(actions);
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public void onTick() {
        if (actionIndex < actions.size) {
            Action action = actions.get(actionIndex);
            action.onTick();
            if (action.isComplete()) {
                actionIndex++;
                onTick();
            }
        }
    }

    public boolean isComplete() {
        return actionIndex >= actions.size;
    }

}
