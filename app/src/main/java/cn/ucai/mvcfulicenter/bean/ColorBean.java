package cn.ucai.mvcfulicenter.bean;

import java.io.Serializable;

/**
 * Created by 11039 on 2016/10/13.
 */
public class ColorBean implements Serializable {
    public ColorBean() {
    }

    @Override
    public String toString() {
        return "ColorBean{" +
                "colorId=" + colorId +
                ", colorName='" + colorName + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", colorUrl='" + colorUrl + '\'' +
                '}';
    }

    /**
     * colorId : 4
     * colorName : 绿色
     * colorCode : #59d85c
     * colorUrl : 1
     */

    private int colorId;
    private String colorName;
    private String colorCode;
    private String colorUrl;

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorUrl() {
        return colorUrl;
    }

    public void setColorUrl(String colorUrl) {
        this.colorUrl = colorUrl;
    }
}
