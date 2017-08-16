package com.pong.library.multilayout;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pong.p on 4/7/2016.
 */
public class TextOption implements Parcelable {
    private float size;
    private int color;

    public TextOption(float size, int color) {
        this.size = size;
        this.color = color;
    }

    public TextOption() {
    }

    public TextOption(Parcel in) {
        this.size = in.readFloat();
        this.color = in.readInt();
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

    public static final Creator<TextOption> CREATOR = new Creator<TextOption>() {
        @Override
        public TextOption createFromParcel(Parcel in) {
            return new TextOption(in);
        }

        @Override
        public TextOption[] newArray(int size) {
            return new TextOption[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeFloat(size);
        parcel.writeInt(color);
    }
}
