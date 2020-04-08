package com.ss.example.contactline.drawline;

import java.io.Serializable;

public class RightRangePointBean implements Serializable {
    private static final long serialVersionUID = -9016226763399909177L;
    private float RightTop;
    private float RightBottom;
    private int q_num;

    public float getRightTop() {
        return RightTop;
    }

    public void setRightTop(float rightTop) {
        RightTop = rightTop;
    }

    public float getRightBottom() {
        return RightBottom;
    }

    public void setRightBottom(float rightBottom) {
        RightBottom = rightBottom;
    }

    public int getQ_num() {
        return q_num;
    }

    public void setQ_num(int q_num) {
        this.q_num = q_num;
    }
}
