package com.ss.example.contactline.drawline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ss.example.contactline.LinkDataBean;
import com.ss.example.contactline.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RightAdapter extends RecyclerView.Adapter<RightAdapter.ViewHolder> {

    private Context mContext;
    private List<LinkDataBean> mRightList;
    private onClickListener mListener;

    public RightAdapter(Context context, List<LinkDataBean> rightList) {

        mContext = context;
        mRightList = rightList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.right_data, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LinkDataBean linkDataBean = mRightList.get(position);
        holder.mTvRight.setText(linkDataBean.getContent());
    }

    @Override
    public int getItemCount() {
        return mRightList.size() > 0 ? mRightList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvRight;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvRight = itemView.findViewById(R.id.tv_right);
        }
    }

    public void setClick(onClickListener listener) {

        mListener = listener;
    }

    public interface onClickListener {
        void onRightClick(float endX, float endY);
    }
}
