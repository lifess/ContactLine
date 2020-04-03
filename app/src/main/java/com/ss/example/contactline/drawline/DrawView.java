package com.ss.example.contactline.drawline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;

import androidx.annotation.Nullable;

public class DrawView extends View {

    private Paint paint;
    private int size;
    private ArrayList<RangePointBean> startList;
    private Context mContext;


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
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(startX, startY, endX, endY, paint);
        for (int i = 0; i < list.size(); i++) {
            float[] data = list.get(i);
            canvas.drawLine(data[0], data[1], data[2], data[3], paint);
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
                    }else {
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
                    deleteSameLine();
                    float[] data = {startX, startY, endX, endY};
                    list.add(data);
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
                RangePointBean s = startList.get(i);
                float leftTop = s.getLeftTop();
                float leftBottom = s.getLeftBottom();
                //删除起点在同一个范围内的线,如果起点和终点连得一样，谁先遍历到，谁先走，并return
                if (startY >= leftTop && startY <= leftBottom
                        && next[1] >= leftTop && next[1] <= leftBottom) {
                    iterator.remove();
                    return;
                }
                //删除终点在同一个范围内的线
                if (endY >= leftTop && endY <= leftBottom
                        && next[3] >= leftTop && next[3] <= leftBottom) {
                    iterator.remove();
                    return;
                }
            }
        }
    }

    public void getDataSize(int size) {
        this.size = size;
    }

    public void setStartPoint(ArrayList<RangePointBean> startList) {
        this.startList = startList;
    }

}
