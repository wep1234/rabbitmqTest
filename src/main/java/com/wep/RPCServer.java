package com.wep;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;

public class RPCServer {

    public static void main(String[] args) throws Exception{
        Connection conn = ConnectionUtil.getConn("RGP订单系统Server端");
        final Channel channel = conn.createChannel();
        String exchangeName = "roberto.order";
        String followQueue = "roberto.order.add";
        //声明要关注的队列（队列已存在） 1.队列名2.是否持久化，3是否局限与链接，4不再使用是否删除，5其他的属性
        channel.queueDeclare(followQueue, true, false, false, new HashMap<String, Object>());

        channel.exchangeDeclare(exchangeName,BuiltinExchangeType.DIRECT,true,false,false,new HashMap<String, Object>());

        channel.basicConsume(followQueue,true,"RGP订单系统Server端",new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String replyTo = properties.getReplyTo();//指定路由
                String correlationId = properties.getCorrelationId();

                System.out.println("----------收到RPC调用请求消息----------");
                System.out.println(consumerTag);
                System.out.println("消息属性为:" + properties);
                System.out.println("消息内容为" + new String(body));
                try {
                    String orderId = RPCMehtod.addOrder(new String(body));
                    AMQP.BasicProperties replyProperties = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").correlationId(correlationId).build();
                    //发送消息到replyTo队列中
                    channel.basicPublish("", replyTo, replyProperties, orderId.getBytes());
                    System.out.println("----------RPC调用成功 结果已返回----------");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
