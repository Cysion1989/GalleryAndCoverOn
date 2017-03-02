package cn.com.condenast.coverdemo;

/**
 * Created by xianshang.liu on 2016/10/18.
 * 接口类，将数据绑定到holder
 */

public interface IDataBind {

    void bindData(Object obj, int position,IOnItemClick aIOnItemClick);

}
