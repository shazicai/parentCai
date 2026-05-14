package cn.com.springCai.consumer.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author caiJH
 * @Date 2025/6/27 2:49 PM
 * @Version 1.0
 */
@Data
public class ErpTrainPersonInfo implements Serializable {

    private static final long serialVersionUID = -1657183650644603633L;

    public ErpTrainPersonInfo() {
    }

    public ErpTrainPersonInfo(String no, String courseNumber, String userName, String companyName, String positionName,
                              String deptName, String card, String gender, String creditCard, String mobile, String orderNo) {
        this.no = no;
        this.courseNumber = courseNumber;
        this.userName = userName;
        this.companyName = companyName;
        this.positionName = positionName;
        this.deptName = deptName;
        this.card = card;
        this.gender = gender;
        this.creditCard = creditCard;
        this.mobile = mobile;
        this.orderNo = orderNo;
    }

    /**
     * 编号
     */
    private String no;

    /**
     */
    private String courseNumber;

    /**
     */
    private String userName;

    /**
     */
    private String companyName;

    /**
     */
    private String positionName;

    /**
     */
    private String deptName;

    /**
     */
    private String card;

    /**
     * 性别 1 男 2 女
     */
    private String gender;

    /**
     */
    private String creditCard;

    /**
     */
    private String mobile;

    /**
     */
    private String orderNo;

    @Override
    public String toString() {
        return "ErpTrainPersonInfo{" +
                "no='" + no + '\'' +
                ", courseNumber='" + courseNumber + '\'' +
                ", userName='" + userName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", positionName='" + positionName + '\'' +
                ", deptName='" + deptName + '\'' +
                ", card='" + card + '\'' +
                ", gender='" + gender + '\'' +
                ", creditCard='" + creditCard + '\'' +
                ", mobile='" + mobile + '\'' +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }
}
