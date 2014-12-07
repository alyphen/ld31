package io.github.alyphen.ld31.action;

public class ConditionalAction extends Action {

    private Condition condition;
    private Action trueAction;
    private Action falseAction;

    public ConditionalAction(Condition condition, Action action) {
        this(condition, action, null);
    }

    public ConditionalAction(Condition condition, Action trueAction, Action falseAction) {
        this.condition = condition;
        this.trueAction = trueAction;
        this.falseAction = falseAction;
    }

    @Override
    public boolean isComplete() {
        return condition.isTrue() ? trueAction == null || trueAction.isComplete() : falseAction == null || falseAction.isComplete();
    }

    @Override
    public void onTick() {
        if (condition.isTrue() && trueAction != null)
            trueAction.onTick();
        else if (falseAction != null)
            falseAction.onTick();
    }

}
