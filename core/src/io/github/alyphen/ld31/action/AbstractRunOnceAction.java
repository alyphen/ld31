package io.github.alyphen.ld31.action;

public abstract class AbstractRunOnceAction extends Action {

    private boolean complete;

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public void onTick() {
        if (!complete) {
            run();
            complete = true;
        }
    }

    public abstract void run();

}
