package com.ss.example.contactline.drawline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.Nullable;

public class DrawView extends View {

    private Paint paint;
    private int size;
    private ArrayList<LeftRangePointBean> startList;
    private Context mContext;
    private ArrayList<RightRangePointBean> rightRangeList;
    private List<LinkLineBean> resultList;
    private ArrayList<float[]> rightList = new ArrayList<>();
    private ArrayList<float[]> worryList = new ArrayList<>();
    private boolean isVerify = false;
    private Paint rightPaint;
    private Paint worryPaint;
    private boolean isRight = false;


    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    private void init(Context context) {
        mContext = context;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        rightPaint = new Paint();
        rightPaint.setColor(Color.GREEN);
        rightPaint.setStrokeWidth(5);
        worryPaint = new Paint();
        worryPaint.setColor(Color.RED);
        worryPaint.setStrokeWidth(5);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(startX, startY, endX, endY, paint);
        for (int i = 0; i < list.size(); i++) {
            float[] data = list.get(i);
            canvas.drawLine(data[0], data[1], data[2], data[3], paint);
        }

        if (isVerify) {
            for (int i = 0; i < rightList.size(); i++) {
                float[] data = rightList.get(i);
                canvas.drawLine(data[0], data[1], data[2], data[3], rightPaint);
            }
            for (int i = 0; i < worryList.size(); i++) {
                float[] data = worryList.get(i);
                canvas.drawLine(data[0], data[1], data[2], data[3], worryPaint);
            }
            isVerify = false;
        }

    }

    float startX = 0;
    float startY = 0;

    float endX = 0;
    float endY = 0;
    ArrayList<float[]> list = new ArrayList<>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int width = getWidth();
        int height = getHeight();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //根据数据的大小来确定可以画几条线
                if (size > -1 && list.size() < size) {
                    if (event.getX() < width / 2f) {
                        startX = 0;
                    } else {
                        startX = width;
                    }
                    startY = event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (size > -1 && list.size() < size) {
                    endX = event.getX();
                    endY = event.getY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                //判断正连线，以及反连线
                if (width <= endX && height >= endY || endX <= 0 && height >= endY) {
                    if (endX >= width) {//确定终点x轴位置
                        endX = width;
                    } else {
                        endX = 0;
                    }
                    deleteSameLine();
                    float[] data = {startX, startY, endX, endY};
                    list.add(data);
                    verifyResult(width);
                }
                break;
        }
        return true;
    }

    private void deleteSameLine() {
        Iterator<float[]> iterator = list.iterator();
        while (iterator.hasNext()) {
            float[] next = iterator.next();
            for (int i = 0; i < startList.size(); i++) {
                LeftRangePointBean s = startList.get(i);
                float leftTop = s.getLeftTop();
                float leftBottom = s.getLeftBottom();
                //删除起点在同一个范围内的线,如果起点和终点连得一样，谁先遍历到，谁先走，并return
                if (startY >= leftTop && startY <= leftBottom
                        && next[1] >= leftTop && next[1] <= leftBottom && next[0] == startX
                        || startY >= leftTop && startY <= leftBottom && next[3] >= leftTop && next[3] <= leftBottom
                        && next[1] >= leftTop && next[1] <= leftBottom) {
                    iterator.remove();
                    return;
                }
                //删除终点在同一个范围内的线
                if (endY >= leftTop && endY <= leftBottom
                        && next[3] >= leftTop && next[3] <= leftBottom && next[2] == endX
                        || endY >= leftTop && endY <= leftBottom && next[1] >= leftTop && next[1] <= leftBottom
                        && next[3] >= leftTop && next[3] <= leftBottom) {
                    iterator.remove();
                    return;
                }
            }
        }
    }

    private void verifyResult(int width) { //验证对错
        if (size > 0 && list.size() >= size) {
            isVerify = true;
            for (int i = 0; i < resultList.size(); i++) {
                isRight = false;
                LinkLineBean linkLineBean = resultList.get(i);
                float leftTop = linkLineBean.getLeftTop();
                float leftBottom = linkLineBean.getLeftBottom();
                float rightTop = linkLineBean.getRightTop();
                float rightBottom = linkLineBean.getRightBottom();
                Log.i("sss", "verifyResult: " + leftTop + ": " + leftBottom + "---------" + rightTop + ": " + rightBottom);
                Iterator<float[]> iterator = list.iterator();
                while (iterator.hasNext()) {
                    float[] next = iterator.next();
                    Log.i("sss", "verifyResult: " + next[0] + ": " + next[1] + ": " + next[3]);
                    if (next[1] >= leftTop && next[1] <= leftBottom
                            && next[3] >= rightTop && next[3] <= rightBottom
                            || next[1] >= rightTop && next[1] <= rightBottom
                            && next[3] >= leftTop && next[3] <= leftTop) {
                        Log.i("sss", "verifyResult: " + "right");
                        isRight = true;
                        rightList.add(next);
                    }
                }
                if (!isRight) {
                    worryList.add(list.get(i));
                }
            }
            invalidate();
        }
    }

    public void getDataSize(int size) {
        this.size = size;
    }

    public void setStartPoint(ArrayList<LeftRangePointBean> startList) {
        this.startList = startList;
    }

    public void setRightPoint(ArrayList<RightRangePointBean> rightRangeList) {
        this.rightRangeList = rightRangeList;
    }

    public void getRightAnswer(List<LinkLineBean> resultList) {
        this.resultList = resultList;
    }
}
