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


    private Context mContext;
    private Paint paint;
    private float mStartX;
    private float mStartY;
    private float mTop;
    private float mBottom;
    private int size;

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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (size > 0  && list.size() < size && mStartY != 0) {
                    startX = mStartX;
                    startY = mStartY;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (size > 0 && list.size() < size && mStartY != 0) {
                    endX = event.getX();
                    endY = event.getY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                int width = getWidth();
                int height = getHeight();
                if (width <= endX && height > endY) {
                    Iterator<float[]> iterator = list.iterator();
                    while (iterator.hasNext()){
                        float[] next = iterator.next();
                        if (next[0] == startX && next[1] == startY || next[3] > mTop && next[3] < mBottom ) {
                            iterator.remove();
                        }
                    }
                    float[] data = {startX, startY, endX, endY};
                    list.add(data);
                }
                break;
        }
        return true;
    }

    public void setStartPoint(float startX, float startY) {
        mStartX = startX;
        mStartY = startY;
    }

    public void setEndPoint(float top, float bottom) {
        mTop = top;
        mBottom = bottom;
    }

    public void getDataSize(int size) {
        this.size = size;
    }
}
