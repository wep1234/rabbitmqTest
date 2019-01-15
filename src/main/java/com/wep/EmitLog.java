package com.wep;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * fanout广播模式
 */
public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception{
        Connection conn = ConnectionUtil.getConn();
        Channel channel = conn.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        String message = "Hello RabbitMQ";
        //exchange  queue
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
        System.out.println("Producer Send +'" + message + "'");

        //关闭通道和连接
        channel.close();
        conn.close();
    }
}
