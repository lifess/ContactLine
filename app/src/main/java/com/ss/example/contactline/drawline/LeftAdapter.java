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

public class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.ViewHolder> {

    private Context mContext;
    private List<LinkDataBean> mLeftList;
    private onClickListener mListener;

    public LeftAdapter(Context context, List<LinkDataBean> leftList) {

        mContext = context;
        mLeftList = leftList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.left_data, parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        LinkDataBean linkDataBean = mLeftList.get(position);
        holder.mTvLeft.setText(linkDataBean.getContent());
    }

    @Override
    public int getItemCount() {
        return mLeftList.size() > 0 ? mLeftList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvLeft;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvLeft = itemView.findViewById(R.id.tv_left);
        }
    }

    public void setClick(onClickListener listener) {

        mListener = listener;
    }

    public interface onClickListener {
        void onLeftClick(float startX, float startY);
    }
}
