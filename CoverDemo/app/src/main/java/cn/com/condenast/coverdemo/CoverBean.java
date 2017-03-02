package cn.com.condenast.coverdemo;

/**
 * Created by xianshang.liu on 2017/3/1.
 */

public class CoverBean {

    private String name;
    private String imgUrl;
    private String targetUrl;
    private int resId;
    private int tempCover;

    public int getTempCover() {
        return tempCover;
    }

    public void setTempCover(int aTempCover) {
        tempCover = aTempCover;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int aResId) {
        resId = aResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String aImgUrl) {
        imgUrl = aImgUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String aTargetUrl) {
        targetUrl = aTargetUrl;
    }
}
