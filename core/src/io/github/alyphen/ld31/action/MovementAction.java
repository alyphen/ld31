package io.github.alyphen.ld31.action;

import io.github.alyphen.ld31.objects.characters.GameCharacter;

public class MovementAction extends CharacterAction {

    private int x;
    private int y;

    public MovementAction(GameCharacter character, int x, int y) {
        super(character);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean isComplete() {
        GameCharacter character = getCharacter();
        return character.getX() == getX() && character.getY() == getY();
    }

    @Override
    public void onTick() {
        GameCharacter character = getCharacter();
        if (!character.isMoving()) {
            if (character.getX() < getX()) {
                character.move("right", getX() - character.getX());
            } else if (character.getX() > getX()) {
                character.move("left", character.getX() - getX());
            } else if (character.getY() < getY()) {
                character.move("down", getY() - character.getY());
            } else if (character.getY() > getY()) {
                character.move("up", character.getY() - getY());
            }
        }
    }
}
