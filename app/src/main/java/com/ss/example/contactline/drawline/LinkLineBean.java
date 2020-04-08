package com.ss.example.contactline.drawline;


import java.util.Objects;

/**
 * @Description: 表示LinkLine对象
 */
public class LinkLineBean {

    /**
     * 直线的横纵坐标
     */
    private float leftTop;
    private float leftBottom;
    private float rightTop;
    private float rightBottom;

    public LinkLineBean(float leftTop, float leftBottom, float rightTop, float rightBottom) {
        this.leftTop = leftTop;
        this.leftBottom = leftBottom;
        this.rightTop = rightTop;
        this.rightBottom = rightBottom;
    }


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

    public float getRightTop() {
        return rightTop;
    }

    public void setRightTop(float rightTop) {
        this.rightTop = rightTop;
    }

    public float getRightBottom() {
        return rightBottom;
    }

    public void setRightBottom(float rightBottom) {
        this.rightBottom = rightBottom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkLineBean bean = (LinkLineBean) o;
        return Float.compare(bean.leftTop, leftTop) == 0 &&
                Float.compare(bean.leftBottom, leftBottom) == 0 &&
                Float.compare(bean.rightTop, rightTop) == 0 &&
                Float.compare(bean.rightBottom, rightBottom) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftTop, leftBottom, rightTop, rightBottom);
    }
}
