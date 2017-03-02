package cn.com.condenast.coverdemo;

import android.view.View;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by xianshang.liu on 2017/3/1.
 */

public class CoverGalleryHolder extends BaseViewHolder {

    private List<CoverBean> mDataList;
    private final ImageView mImageGallery;
    private IOnItemClick mIOnItemClick;

    public CoverGalleryHolder(View itemView) {
        super(itemView);
        mImageGallery = (ImageView) itemView.findViewById(R.id.img_gallery);
    }

    @Override
    public void bindData(Object obj, final int position, final IOnItemClick aIOnItemClick) {
        mDataList = (List<CoverBean>) obj;
        mImageGallery.setImageResource(mDataList.get(position).getResId());
        mImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View aView) {
                aIOnItemClick.onItemClicker(mDataList.get(position),position);
            }
        });
    }
}
