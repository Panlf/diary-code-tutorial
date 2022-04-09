package com.plf.diary.activiti.more;

import java.io.Serializable;
import java.util.Date;

/**
 * 请假实体类
 *  注意POJO类型，一定要实现Serializable接口
 * @author Panlf
 * @date 2019/12/11
 */
public class Holiday implements Serializable{


    private Integer id;
    //申请人姓名
    private String holidatName;
    //开始时间
    private Date beginDate;
    //结束时间
    private Date endDate;
    //请假天数
    private Float num;
    //事由
    private String reason;
    //请假类型
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHolidatName() {
        return holidatName;
    }

    public void setHolidatName(String holidatName) {
        this.holidatName = holidatName;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Float getNum() {
        return num;
    }

    public void setNum(Float num) {
        this.num = num;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
