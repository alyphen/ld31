package io.github.alyphen.ld31.message;

public class Message {

    private String text;
    private boolean hasShown;

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean hasShown() {
        return hasShown;
    }

    public void setHasShown(boolean hasShown) {
        this.hasShown = hasShown;
    }

}
