package com.excelbkk.pong.multilayout;

/**
 * Created by pong.p on 4/7/2016.
 */
public class TextOption {
    private float size;
    private int color;

    public TextOption(float size, int color) {
        this.size = size;
        this.color = color;
    }

    public TextOption() {
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
