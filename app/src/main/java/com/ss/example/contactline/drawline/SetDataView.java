package com.ss.example.contactline.drawline;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.ss.example.contactline.LinkDataBean;
import com.ss.example.contactline.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SetDataView extends RelativeLayout implements LeftAdapter.onClickListener, RightAdapter.onClickListener{

    private RecyclerView llLeft;
    private RecyclerView llRight;
    private List<LinkDataBean> allList = new ArrayList<>();
    private List<LinkDataBean> leftList = new ArrayList<>();
    private List<LinkDataBean> rightList = new ArrayList<>();
    private int size;

    private Context context;
    private LeftAdapter mLeftAdapter;
    private RightAdapter mRightAdapter;
    private Paint paint;
    private DrawView drawView;

    public SetDataView(Context context) {
        super(context);
    }

    public SetDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SetDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_data, this);
        llLeft = inflate.findViewById(R.id.rlv_left);
        llRight = inflate.findViewById(R.id.rlv_right);
        drawView = inflate.findViewById(R.id.draw_view);
    }

    public void setData(List<LinkDataBean> linkDataBeanList) {
        if (linkDataBeanList == null || linkDataBeanList.size() == 0) {
            return;
        }

        this.allList = linkDataBeanList;

        // 将数据分为两列
        for (LinkDataBean item : allList) {
            if (0 == item.getCol()) {
                leftList.add(item);
            } else {
                rightList.add(item);
            }
        }

        // 将数据根据行号排序，避免数据错乱
        Collections.sort(leftList, new Comparator<LinkDataBean>() {
            @Override
            public int compare(LinkDataBean o1, LinkDataBean o2) {
                return o1.getRow() - o2.getRow();
            }
        });
        Collections.sort(rightList, new Comparator<LinkDataBean>() {
            @Override
            public int compare(LinkDataBean o1, LinkDataBean o2) {
                return o1.getRow() - o2.getRow();
            }
        });
        size = Math.min(leftList.size(), rightList.size());
        drawView.getDataSize(size);
        addLeftView();
        addRightView();
    }

    private void addLeftView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        llLeft.setLayoutManager(layoutManager);
        mLeftAdapter = new LeftAdapter(getContext(),leftList);
        llLeft.setAdapter(mLeftAdapter);
        mLeftAdapter.setClick(this);
        mLeftAdapter.notifyDataSetChanged();
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < leftList.size(); i++) {
//                    View childAt = llLeft.getChildAt(i);
                    View childAt = layoutManager.findViewByPosition(i);
                    if (childAt != null) {
                        int top = childAt.getTop();
                        int bottom = childAt.getBottom();
                        Log.i("======", "addLeftView: "+top +" : "+ bottom);
                    }
                }
            }
        },200);*/
    }

    private void addRightView() {
        llRight.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new RightAdapter(getContext(), rightList);
        llRight.setAdapter(mRightAdapter);
        mRightAdapter.setClick(this);
    }

    @Override
    public void onLeftClick(float startX, float startY) {
        drawView.setStartPoint(startX, startY);
    }

    @Override
    public void onRightClick(float top, float bottom) {
        drawView.setEndPoint(top, bottom);
    }
}
