package com.ss.example.contactline.drawline;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ss.example.contactline.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.Nullable;

public class DrawView extends View {

    private int size;
    private Paint paint;
    private Context mContext;
    private float startX = 0;
    private float startY = 0;
    private float endX = 0;
    private float endY = 0;
    private List<float[]> list = new ArrayList<>();
    private List<LeftRangePointBean> startList;
    private List<RightRangePointBean> endList;
    private List<LinkLineBean> resultList;
    private List<float[]> rightList = new ArrayList<>();
    private List<float[]> worryList = new ArrayList<>();
    private boolean isVerify = false;
    private boolean isRight = false;
    private boolean isSave = true;
    private OnChoiceResultListener onChoiceResultListener;


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
        paint.setColor(getResources().getColor(R.color.black));
        paint.setStrokeWidth(5);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isVerify) {
            canvas.drawLine(startX, startY, endX, endY, paint);
            if (!isSave) {
                paint.setColor(Color.BLACK);
                isSave = true;
            }
            for (int i = 0; i < list.size(); i++) {
                float[] data = list.get(i);
                canvas.drawLine(data[0], data[1], data[2], data[3], paint);
            }
        } else {
            for (int i = 0; i < rightList.size(); i++) {
                paint.setColor(getResources().getColor(R.color.answer_right));
                paint.setStrokeWidth(6);
                float[] data = rightList.get(i);
                canvas.drawLine(data[0], data[1], data[2], data[3], paint);
            }
            for (int i = 0; i < worryList.size(); i++) {
                paint.setColor(getResources().getColor(R.color.answer_wrong));
                paint.setStrokeWidth(6);
                float[] data = worryList.get(i);
                canvas.drawLine(data[0], data[1], data[2], data[3], paint);
            }
            if (worryList.size() > 0) {
                if (onChoiceResultListener != null) onChoiceResultListener.onResultSelected(false);
            } else {
                if (onChoiceResultListener != null) onChoiceResultListener.onResultSelected(true);
            }
//            isVerify = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int width = getWidth();
        int height = getHeight();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //根据数据的大小来确定可以画几条线
                if (size > -1 && list.size() < size) {
                    if (event.getX() < width / 2f) {//确定起点x轴的位置
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
                if (width <= endX && height >= endY && startX == 0
                        || endX <= 0 && height >= endY && startX == width) {//确定线的起点和终点没问题才可以进行保存、移除、验证等操作
                    if (endX >= width) {//确定终点x轴位置
                        endX = width;
                    } else {
                        endX = 0;
                    }
                    deleteSameLine();
                    float[] data = {startX, startY, endX, endY};
                    list.add(data);
                    verifyResult(width);
                } else {//不符合要求的线直接移除
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
                for (int j = 0; j < endList.size(); j++) {
                    RightRangePointBean bean = endList.get(j);
                    float rightTop = bean.getRightTop();
                    float rightBottom = bean.getRightBottom();

                    //左右连线相同，并且list中的线的开始点和这次的线的开始点相同，移除
                    if (startY >= leftTop && startY <= leftBottom
                            && endY >= rightTop && endY <= rightBottom
                            && next[1] >= leftTop && next[1] <= leftBottom
                            && next[3] >= rightTop && next[3] <= rightBottom
                            && next[0] == startX && next[2] == endX) {
                        iterator.remove();
                        invalidate();
                    }

                    ////左右连线相同，并且list中的线的开始点和这次的线的结束点相同，移除
                    if (startY >= leftTop && startY <= leftBottom
                            && endY >= rightTop && endY <= rightBottom
                            && next[1] >= rightTop && next[1] <= rightBottom
                            && next[3] >= leftTop && next[3] <= leftBottom
                            && next[0] == endX && next[2] == startX) {
                        iterator.remove();
                        invalidate();
                    }

                    //右连线不同，删除起点在同一个范围内,但终点不在同一个范围内的线
                    if (startY >= leftTop && startY <= leftBottom
                            && endY >= rightTop && endY <= rightBottom
                            && next[1] >= leftTop && next[1] <= leftBottom
                            && (next[3] <= rightTop || next[3] >= rightBottom)
                            && next[0] == startX) {
                        iterator.remove();
                        invalidate();
                    }

                    //左连线不同，删除终点在同一个范围内,但起点不在同一个范围内的线
                    if (startY >= leftTop && startY <= leftBottom
                            && endY >= rightTop && endY <= rightBottom
                            && (next[1] <= leftTop || next[1] >= leftBottom)
                            && next[3] >= rightTop && next[3] <= rightBottom
                            && next[2] == endX) {
                        iterator.remove();
                        invalidate();
                    }

                    //左右连线相反时，list里的线从右到左，这次的线从左到右，并且list的线的终点和这次线的起点在同一个范围，
                    //但list的线的起点和这次线的终点不在同一个范围
                    if (startY >= leftTop && startY <= leftBottom
                            && endY >= rightTop && endY <= rightBottom
                            && (next[1] <= rightTop || next[1] >= rightBottom)
                            && next[3] >= leftTop && next[3] <= leftBottom
                            && next[2] == startX) {
                        iterator.remove();
                        invalidate();
                    }

                    //左右连线相反时，list里的线从左到右，这次的线从右到左，并且list的线的起点和这次线的终点在同一个范围，
                    //但list的线的终点和这次线的起点不在同一个范围
                    if (startY >= leftTop && startY <= leftBottom
                            && endY >= rightTop && endY <= rightBottom
                            && next[1] >= rightTop && next[1] <= rightBottom
                            && (next[3] <= leftTop || next[3] >= leftBottom)
                            && next[0] == endX) {
                        iterator.remove();
                        invalidate();
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

    public void setStartPoint(List<LeftRangePointBean> startList) {
        this.startList = startList;
    }

    public void setEndPoint(List<RightRangePointBean> rightRangeList) {
        this.endList = rightRangeList;
    }

    public void getRightAnswer(List<LinkLineBean> resultList) {
        this.resultList = resultList;
    }

    public interface OnChoiceResultListener {
        void onResultSelected(boolean correct);
    }

    public void setOnChoiceResultListener(OnChoiceResultListener onChoiceResultListener) {
        this.onChoiceResultListener = onChoiceResultListener;
    }
}
