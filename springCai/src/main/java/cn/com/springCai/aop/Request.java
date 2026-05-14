package cn.com.springCai.aop;

import java.util.List;

/**
 * @Author caiJH
 * @Date 2025/9/16 5:10 PM
 * @Version 1.0
 */
public class Request {

    private String UUID;

    private String startTime;

    private String endTime;

    private List<Integer> recordIdList;


    private List<OrderAndEmployeeInner> orderAndEmployeeList;

    /**
     * 订单号和姓名和员工号拼接字符串
     */
    static class OrderAndEmployeeInner{
        /**
         * 订单号
         */
        private String orderNo;

        /**
         * 员工名称
         */
        private String name;

        /**
         * 员工编号
         */
        private String employeeNo;

        public static Request.OrderAndEmployeeInner getInstance(String orderNo, String name, String employeeNo) {
            Request.OrderAndEmployeeInner orderAndEmployeeInner = new Request.OrderAndEmployeeInner();
            orderAndEmployeeInner.orderNo = orderNo;
            orderAndEmployeeInner.name = name;
            orderAndEmployeeInner.employeeNo = employeeNo;
            return orderAndEmployeeInner;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmployeeNo() {
            return employeeNo;
        }

        public void setEmployeeNo(String employeeNo) {
            this.employeeNo = employeeNo;
        }

        @Override
        public String toString() {
            return "OrderAndEmployeeInner{" +
                    "orderNo='" + orderNo + '\'' +
                    ", name='" + name + '\'' +
                    ", employeeNo='" + employeeNo + '\'' +
                    '}';
        }
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getRecordIdList() {
        return recordIdList;
    }

    public void setRecordIdList(List<Integer> recordIdList) {
        this.recordIdList = recordIdList;
    }

    public List<OrderAndEmployeeInner> getOrderAndEmployeeList() {
        return orderAndEmployeeList;
    }

    public void setOrderAndEmployeeList(List<OrderAndEmployeeInner> orderAndEmployeeList) {
        this.orderAndEmployeeList = orderAndEmployeeList;
    }
}
