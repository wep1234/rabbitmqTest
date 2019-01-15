package com.wep;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;

public class WorkTest{

    @Test
    public void send()throws Exception{
        Connection conn = ConnectionUtil.getConn();
        Channel chan = conn.createChannel();
        // 声明一个队列
        chan.queueDeclare("work", false, false, false, null);
        for(int i=0;i<100;i++){
            String msg = "第"+i+"条hello";
            chan.basicPublish("", "work", null, msg.getBytes("UTF-8"));
        }
        chan.close();
        conn.close();
    }

    @Test
    public void receive1()throws Exception{
        Connection conn = ConnectionUtil.getConn();
        Channel chan = conn.createChannel();
        // 声明一个队列
        chan.queueDeclare("work", false, false, false, null);
        //同一时刻服务器只发送一条消息给同一消费者,消费者空闲,才发送一条
        chan.basicQos(1);
        //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(chan) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Customer Received1 '" + message + "'");
            }
        };
        Thread.sleep(50);
        //自动回复队列应答 -- RabbitMQ中的消息确认机制
        chan.basicConsume("work", true, consumer);
    }

    @Test
    public void receive2()throws Exception{
        Connection conn = ConnectionUtil.getConn();
        Channel chan = conn.createChannel();
        // 声明一个队列
        chan.queueDeclare("work", false, false, false, null);
        //同一时刻服务器只发送一条消息给同一消费者,消费者空闲,才发送一条
        chan.basicQos(1);
        //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(chan) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Customer Received2 '" + message + "'");
            }
        };
        Thread.sleep(150);
        //自动回复队列应答 -- RabbitMQ中的消息确认机制
        chan.basicConsume("work", true, consumer);
    }
}
