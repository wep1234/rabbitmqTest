package com.wep;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ReceiveLogsDirect {
    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Connection conn = ConnectionUtil.getConn();
        Channel channel = conn.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();
        String[] strs = new String[] { "info", "waring", "error" ,"*.y"};//路由规则在 info,waring,error的能接收到消息
        for (String str : strs) {
            channel.queueBind(queueName, EXCHANGE_NAME, str);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C，queueName:"+queueName);
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                String routingKey = envelope.getRoutingKey();
                System.out.println("r:[" + routingKey + "]:[" + message + "]");
            }
        };
        //Thread.sleep(50);
        //自动回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(queueName,true,consumer);
    }
}
