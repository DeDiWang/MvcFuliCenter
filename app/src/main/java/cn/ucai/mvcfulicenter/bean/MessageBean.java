package cn.ucai.mvcfulicenter.bean;

import java.io.Serializable;

/**
 * Created by 11039 on 2016/10/13.
 */
public class MessageBean implements Serializable {

    public MessageBean() {
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                '}';
    }

    /**
     * success : true
     * msg : 收藏成功
     */

    private boolean success;
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
