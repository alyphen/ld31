package io.github.alyphen.ld31.action;

import com.badlogic.gdx.utils.Array;

public class EventQueue {

    private Array<Event> events;
    private int eventIndex;

    public EventQueue() {
        events = new Array<>();
    }

    public void onTick() {
        if (eventIndex < events.size) {
            Event event = events.get(eventIndex);
            event.onTick();
            if (event.isComplete()) {
                eventIndex++;
                onTick();
            }
        }
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public boolean isComplete() {
        return eventIndex >= events.size;
    }

}
