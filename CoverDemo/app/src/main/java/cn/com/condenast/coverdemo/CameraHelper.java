package cn.com.condenast.coverdemo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianshang.liu on 2017/3/1.
 */

public class CameraHelper {

    public int[] images = {R.mipmap.q7, R.mipmap.q8, R.mipmap.q5, R.mipmap.q1, R.mipmap.q4};
    public int[] imageCover = {R.mipmap.paopao, R.mipmap.lable1, R.mipmap.redpao, R.mipmap.redpao, R.mipmap.lable1};

    private static volatile CameraHelper instance;

    private CameraHelper() {

    }

    public static synchronized CameraHelper getInstance() {
        if (instance == null) {
            instance = new CameraHelper();
        }
        return instance;
    }

    public List<CoverBean> getCoverList() {
        List<CoverBean> dataList = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            CoverBean c1 = new CoverBean();
            c1.setResId(images[i]);
            c1.setTempCover(imageCover[i]);
            dataList.add(c1);
        }
        return dataList;
    }
}
