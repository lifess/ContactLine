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
    private boolean isSave = true;


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
        if (!isSave) {
            paint.setColor(Color.BLACK);
            isSave = true;
        }
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
                } else {
                    isSave = false;
                    paint.setColor(Color.TRANSPARENT);
                    invalidate();
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
                for (int j = 0; j < rightRangeList.size(); j++) {
                    RightRangePointBean bean = rightRangeList.get(j);
                    float rightTop = bean.getRightTop();
                    float rightBottom = bean.getRightBottom();

                    //左右连线相同，并且集合中的线的开始点和这次的线的开始点相同，移除
                    if (startY >= leftTop && startY <= leftBottom
                            && endY >= rightTop && endY <= rightBottom
                            && next[1] >= leftTop && next[1] <= leftBottom
                            && next[3] >= rightTop && next[3] <= rightBottom
                            && next[0] == startX && next[2] == endX) {
                        iterator.remove();
                    }

                    ////左右连线相同，并且集合中的线的开始点和这次的线的结束点相同，移除
                    if (startY >= leftTop && startY <= leftBottom
                            && endY >= rightTop && endY <= rightBottom
                            && next[1] >= rightTop && next[1] <= rightBottom
                            && next[3] >= leftTop && next[3] <= leftBottom
                            && next[0] == endX && next[2] == startX) {
                        iterator.remove();
                    }

                    //右连线不同，删除起点在同一个范围内,但终点不在同一个范围内的线
                    if (startY >= leftTop && startY <= leftBottom
                            && endY >= rightTop && endY <= rightBottom
                            && next[1] >= leftTop && next[1] <= leftBottom
                            && (next[3] <= rightTop || next[3] >= rightBottom)
                            && next[0] == startX) {
                        iterator.remove();
                    }

                    //左连线不同，删除终点在同一个范围内,但起点不在同一个范围内的线
                    if (startY >= leftTop && startY <= leftBottom
                            && endY >= rightTop && endY <= rightBottom
                            && (next[1] <= leftTop || next[1] >= leftBottom)
                            && next[3] >= rightTop && next[3] <= rightBottom
                            && next[2] == endX) {
                        iterator.remove();
                    }

                    //左右连线相反时，集合里的线从右到左，这次的线从左到右，并且集合的线的终点和这次线的起点在同一个范围，
                    //但集合的线的起点和这次线的终点不在同一个范围
                    if (startY >= leftTop && startY <= leftBottom
                            && endY >= rightTop && endY <= rightBottom
                            && (next[1] <= rightTop || next[1] >= rightBottom)
                            && next[3] >= leftTop && next[3] <= leftBottom
                            && next[2] == startX) {
                        iterator.remove();
                    }

                    //左右连线相反时，集合里的线从左到右，这次的线从右到左，并且集合的线的起点和这次线的终点在同一个范围，
                    //但集合的线的终点和这次线的起点不在同一个范围
                    if (startY >= leftTop && startY <= leftBottom
                            && endY >= rightTop && endY <= rightBottom
                            && next[1] >= rightTop && next[1] <= rightBottom
                            && (next[3] <= leftTop || next[3] >= leftBottom)
                            && next[0] == endX) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    private void verifyResult(int width) { //验证对错
        if (size > 0 && list.size() >= size) {
            isVerify = true;
            for (int i = 0; i < list.size(); i++) {
                isRight = false;
                float[] next = list.get(i);
                Log.i("sss", "verifyResult: " + next[0] + ": " + next[1] + "-------" + next[2] + ": " + next[3]);
                for (int j = 0; j < resultList.size(); j++) {
                    LinkLineBean linkLineBean = resultList.get(j);
                    float leftTop = linkLineBean.getLeftTop();
                    float leftBottom = linkLineBean.getLeftBottom();
                    float rightTop = linkLineBean.getRightTop();
                    float rightBottom = linkLineBean.getRightBottom();
                    Log.i("sss", "verifyResult: 对的连线：" + leftTop + ": " + leftBottom + "---------" + rightTop + ": " + rightBottom);
                    if (next[0] == 0) {//如果从左边连的
                        if (next[1] >= leftTop && next[1] <= leftBottom
                                && next[3] >= rightTop && next[3] <= rightBottom) {
                            Log.i("sss", "verifyResult: " + "right");
                            isRight = true;
                            rightList.add(next);
                        }
                    }
                    if (next[0] == width) {//如果从右边连的
                        if (next[1] >= rightTop && next[1] <= rightBottom
                                && next[3] >= leftTop && next[3] <= leftBottom) {
                            Log.i("sss", "verifyResult: " + "right");
                            isRight = true;
                            rightList.add(next);
                        }
                    }
                }
                if (!isRight) {
                    worryList.add(next);
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
