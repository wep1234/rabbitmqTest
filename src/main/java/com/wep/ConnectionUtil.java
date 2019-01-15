package com.wep;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class ConnectionUtil {
    public static Connection getConn() {
        try {
            // 创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();

            //设置RabbitMQ地址
            factory.setHost("47.98.176.255");
            factory.setUsername("rabbit");
            factory.setPassword("rabbitadmin");
            factory.setPort(5672);

            //创建一个新的连接
            Connection conn = factory.newConnection();
            return conn;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public static Connection getConn(String desc) {
        try {
            // 创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();

            //设置RabbitMQ地址
            factory.setHost("47.98.176.255");
            factory.setUsername("rabbit");
            factory.setPassword("rabbitadmin");
            factory.setPort(5672);
            factory.setAutomaticRecoveryEnabled(true);
            factory.setNetworkRecoveryInterval(10000);

            Map<String, Object> connectionFactoryPropertiesMap = new HashMap<String,Object>();
            connectionFactoryPropertiesMap.put("principal", "RobertoWang");
            connectionFactoryPropertiesMap.put("description", "RGP订单系统test");
            connectionFactoryPropertiesMap.put("emailAddress", "1017335380@qq.com");
            factory.setClientProperties(connectionFactoryPropertiesMap);
            //创建一个新的连接
            Connection conn = factory.newConnection(desc);
            return conn;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
