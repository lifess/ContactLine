package com.ss.example.contactline.drawline;

import java.io.Serializable;

public class RangePointBean implements Serializable {
    private static final long serialVersionUID = -9016226763399909177L;
    private float leftTop;
    private float leftBottom;

    public float getLeftTop() {
        return leftTop;
    }

    public void setLeftTop(float leftTop) {
        this.leftTop = leftTop;
    }

    public float getLeftBottom() {
        return leftBottom;
    }

    public void setLeftBottom(float leftBottom) {
        this.leftBottom = leftBottom;
    }
}
