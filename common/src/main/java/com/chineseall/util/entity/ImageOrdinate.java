package com.chineseall.util.entity;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 14:34.
 */
public class ImageOrdinate implements Comparable<ImageOrdinate> {
    private int topX;
    private int topY;
    private int bottomX;
    private int bottomY;

    public ImageOrdinate(int topX, int topY, int bottomX, int bottomY) {
        this.topX = topX;
        this.topY = topY;
        this.bottomX = bottomX;
        this.bottomY = bottomY;
    }

    public ImageOrdinate() {

    }

    public int getTopX() {
        return topX;
    }

    public void setTopX(int topX) {
        this.topX = topX;
    }

    public int getTopY() {
        return topY;
    }

    public void setTopY(int topY) {
        this.topY = topY;
    }

    public int getBottomX() {
        return bottomX;
    }

    public void setBottomX(int bottomX) {
        this.bottomX = bottomX;
    }

    public int getBottomY() {
        return bottomY;
    }

    public void setBottomY(int bottomY) {
        this.bottomY = bottomY;
    }

    @Override
    public int compareTo(ImageOrdinate o) {
        return this.topX - o.topX;
    }

    @Override
    public String toString() {
        return "topX:" + topX + ",topY:" + topY + ",bottomX:" + bottomX + ",bottomY:" + bottomY;
    }
}
