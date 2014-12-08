package io.github.alyphen.ld31.objects.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import io.github.alyphen.ld31.LD31;
import io.github.alyphen.ld31.message.Message;
import io.github.alyphen.ld31.message.Question;
import io.github.alyphen.ld31.objects.GameObject;

public class GameCharacter extends GameObject {

    private LD31 game;
    private String name;
    private Animation walkUpAnimation;
    private Animation walkLeftAnimation;
    private Animation walkRightAnimation;
    private Animation walkDownAnimation;
    private String direction;
    private float stateTime;
    private int moveDist;
    private int dist;
    private boolean nameKnown;
    private int trust;

    public GameCharacter(LD31 game, int x, int y, int depth, String name) {
        super(x, y, 16, 32, depth, game.getCharacterAnimation(name, "up").getKeyFrame(0F));
        this.game = game;
        this.name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        direction = "up";
        walkUpAnimation = game.getCharacterAnimation(name, "up");
        walkDownAnimation = game.getCharacterAnimation(name, "down");
        walkLeftAnimation = game.getCharacterAnimation(name, "left");
        walkRightAnimation = game.getCharacterAnimation(name, "right");
    }

    public String getName() {
        return name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public boolean isMoving() {
        return moveDist < dist;
    }

    public void move(String direction, int dist) {
        setDirection(direction);
        moveDist = 0;
        this.dist = dist;
    }

    private void move() {
        int speed = 2;
        switch (getDirection()) {
            case "up":
                setY(getY() - speed);
                moveDist += speed;
                break;
            case "down":
                setY(getY() + speed);
                moveDist += speed;
                break;
            case "right":
                setX(getX() + speed);
                moveDist += speed;
                break;
            case "left":
                setX(getX() - speed);
                moveDist += speed;
                break;
        }
    }

    public Message say(String text) {
        return game.showMessage((isNameKnown() ? getName() : "?") + ": " + text);
    }

    public Question ask(String text, Array<String> answers) {
        return game.showQuestion((isNameKnown() ? getName() : "?") + ": " + text, answers);
    }

    @Override
    public void onTick() {
        if (isMoving()) {
            move();
            stateTime += Gdx.graphics.getDeltaTime();
        } else {
            stateTime = 0F;
        }
        switch (getDirection()) {
            case "up": setTexture(walkUpAnimation.getKeyFrame(stateTime)); break;
            case "down": setTexture(walkDownAnimation.getKeyFrame(stateTime)); break;
            case "left": setTexture(walkLeftAnimation.getKeyFrame(stateTime)); break;
            case "right": setTexture(walkRightAnimation.getKeyFrame(stateTime)); break;
        }
    }

    public boolean isNameKnown() {
        return nameKnown;
    }

    public void setNameKnown(boolean nameKnown) {
        this.nameKnown = nameKnown;
    }

    public int getTrust() {
        return trust;
    }

    public void setTrust(int trust) {
        this.trust = Math.min(Math.max(trust, -100), 100);
    }

}
