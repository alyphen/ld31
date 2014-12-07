package io.github.alyphen.ld31.action;

import com.badlogic.gdx.Gdx;

public class WaitAction extends Action {

    private float delay;
    private float waited;

    public WaitAction(float delay) {
        this.delay = delay;
    }

    public float getDelay() {
        return delay;
    }

    public float getWaited() {
        return waited;
    }

    @Override
    public boolean isComplete() {
        return getWaited() >= getDelay();
    }

    public void onTick() {
        waited += Gdx.graphics.getDeltaTime();
    }

}
