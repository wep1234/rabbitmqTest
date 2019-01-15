package com.wep;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ReceiveLogsTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Connection conn = ConnectionUtil.getConn();
        Channel channel = conn.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        String queueName = channel.queueDeclare().getQueue();

        String[] strs = new String[]{"kern.critical","*.critical"};

        for(String str:strs){
            channel.queueBind(queueName,EXCHANGE_NAME,str);
        }

        System.out.println("CTRL+C");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                String routingKey = envelope.getRoutingKey();
                System.out.println("r:[" + routingKey + "]:[" + message + "]");
            }
        };

        channel.basicConsume(queueName,true,consumer);
    }
}
