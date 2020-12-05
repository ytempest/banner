package com.ytempest.bannerlib.bean;

/**
 * @author heqidu
 * @since 2020/12/5
 */
public class BannerBean {

    private int no;
    private int imgColor;
    private String title;

    public BannerBean(int no, int imgColor, String title) {
        this.no = no;
        this.imgColor = imgColor;
        this.title = title;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getImgColor() {
        return imgColor;
    }

    public void setImgColor(int imgColor) {
        this.imgColor = imgColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
