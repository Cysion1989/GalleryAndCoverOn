package cn.com.condenast.coverdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianshang.liu on 2016/11/25.
 * 底层页的布局适配器，主要用来分发布局
 * 目前适配的布局主要包括：
 * 顶部图文，顶部装扮，web，标签，上传，分享/收藏以及用户评论
 */

public class GlobalDetailAdapter extends RecyclerView.Adapter {

    //布局类型
    public static final int IMAGE = 0XC01;
    public static final int HGRU = 0XC02;
    public static final int WEB = 0XC03;
    public static final int SHARE = 0XC04;
    public static final int COMMENT = 0XC05;
    public static final int ACTIVITY = 0XC06;
    public static final int LABLE = 0XC07;
    //布局列表
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<CoverBean> dataList = new ArrayList<>();

    private IOnItemClick mIOnItemClick;
    //复用协调数据

    public GlobalDetailAdapter(Context aContext, List<CoverBean> aTypeList) {
        dataList = aTypeList;
        mContext = aContext.getApplicationContext();
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public IOnItemClick getIOnItemClick() {
        return mIOnItemClick;
    }

    public void setIOnItemClick(IOnItemClick aIOnItemClick) {
        mIOnItemClick = aIOnItemClick;
    }

    //根据type分发显示各个holder对应的UI
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HGRU:
                return new CoverGalleryHolder(mLayoutInflater.inflate(R.layout
                        .layout_cover_gallery, parent, false));
        }
        return null;
    }

    //调用holder绑定数据
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).bindData(dataList, holder.getLayoutPosition(),mIOnItemClick);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    //获取item的布局类型
    @Override
    public int getItemViewType(int position) {
        return HGRU;
    }
}
