package cn.com.condenast.coverdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xianshang.liu on 2017/3/1.
 */


public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements IDataBind{
    private String mPageType;

    public String getPageType() {
        return mPageType;
    }

    public void setPageType(String aPageType) {
        mPageType = aPageType;
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public abstract void bindData(Object obj, int position,IOnItemClick aIOnItemClick);
}