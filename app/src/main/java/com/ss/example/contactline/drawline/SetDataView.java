package com.ss.example.contactline.drawline;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.ss.example.contactline.LinkDataBean;
import com.ss.example.contactline.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SetDataView extends RelativeLayout {

    private RecyclerView rlvLeft;
    private RecyclerView rlvRight;
    private List<LinkDataBean> allList = new ArrayList<>();
    private List<LinkDataBean> leftList = new ArrayList<>();
    private List<LinkDataBean> rightList = new ArrayList<>();
    private int size;

    private Context context;
    private LeftAdapter mLeftAdapter;
    private RightAdapter mRightAdapter;
    private DrawView drawView;
    private ArrayList<LeftRangePointBean> leftRangeList = new ArrayList<>();
    private ArrayList<RightRangePointBean> rightRangeList = new ArrayList<>();

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
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_data, this);
        rlvLeft = inflate.findViewById(R.id.rlv_left);
        rlvRight = inflate.findViewById(R.id.rlv_right);
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

    private void getResultList() {
        int width = drawView.getWidth();
        List<LinkLineBean> resultList = new ArrayList<>();
            for (int i = 0; i < leftRangeList.size(); i++) {
                float leftTop = leftRangeList.get(i).getLeftTop();
                float leftBottom = leftRangeList.get(i).getLeftBottom();
                LeftRangePointBean leftRangePointBean = leftRangeList.get(i);
                for (int j = 0; j < rightRangeList.size(); j++) {
                    RightRangePointBean rightRangePointBean = rightRangeList.get(j);
                    if (leftRangePointBean.getQ_num() == rightRangePointBean.getQ_num()) {
                        float rightTop = rightRangeList.get(j).getRightTop();
                        float rightBottom = rightRangeList.get(j).getRightBottom();
                        LinkLineBean leftLinkLineBean = new LinkLineBean(leftTop, leftBottom, rightTop, rightBottom);
                        resultList.add(leftLinkLineBean);
                    }
                }
            }
            drawView.getRightAnswer(resultList);
    }

    private void addLeftView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        rlvLeft.setLayoutManager(layoutManager);
        mLeftAdapter = new LeftAdapter(context, leftList);
        rlvLeft.setAdapter(mLeftAdapter);
        mLeftAdapter.notifyDataSetChanged();
        rlvLeft.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for (int i = 0; i < leftList.size(); i++) {
                    LeftRangePointBean leftRangePointBean = new LeftRangePointBean();
                    View childAt = layoutManager.findViewByPosition(i);
                    if (childAt != null) {
                        float top = childAt.getTop();
                        float bottom = childAt.getBottom();
                        Log.i("======", "addLeftView: " + "top:" + top + "------" + "bottom:" + bottom);
                        leftRangePointBean.setLeftTop(top);
                        leftRangePointBean.setLeftBottom(bottom);
                        leftRangePointBean.setQ_num(leftList.get(i).getQ_num());
                        leftRangeList.add(leftRangePointBean);
                    }
                    if (leftList.size() - 1 == i) {
                        rlvLeft.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
                drawView.setStartPoint(leftRangeList);
            }
        });
    }

    private void addRightView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rlvRight.setLayoutManager(linearLayoutManager);
        mRightAdapter = new RightAdapter(context, rightList);
        rlvRight.setAdapter(mRightAdapter);
        mRightAdapter.notifyDataSetChanged();
        rlvRight.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for (int i = 0; i < rightList.size(); i++) {
                    RightRangePointBean rightRangePointBean = new RightRangePointBean();
                    View childAt = linearLayoutManager.findViewByPosition(i);
                    if (childAt != null) {
                        float top = childAt.getTop();
                        float bottom = childAt.getBottom();
                        Log.i("======", "addLeftView: " + "top:" + top + "------" + "bottom:" + bottom);
                        rightRangePointBean.setRightTop(top);
                        rightRangePointBean.setRightBottom(bottom);
                        rightRangePointBean.setQ_num(rightList.get(i).getQ_num());
                        rightRangeList.add(rightRangePointBean);
                    }
                    if (rightList.size() - 1 == i) {
                        rlvRight.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
                getResultList();
                drawView.setRightPoint(rightRangeList);
            }
        });
    }
}
