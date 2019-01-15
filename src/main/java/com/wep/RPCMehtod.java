package com.wep;

public class RPCMehtod {

    private static Integer orderId = 10;
    public static String addOrder(String orderInfo) {
        try {
            System.out.println("orderInfo已添加到数据库");
            orderId++;
            return "订单ID为"+orderId;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
