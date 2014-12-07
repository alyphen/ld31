package io.github.alyphen.ld31.action;

public class Event {

    private ActionQueue queue;

    public Event(ActionQueue queue) {
        this.queue = queue;
    }

    public ActionQueue getQueue() {
        return queue;
    }

    public void onTick() {
        getQueue().onTick();
    }

    public boolean isComplete() {
        return getQueue().isComplete();
    }

}
