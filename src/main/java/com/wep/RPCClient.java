package com.wep;

import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class RPCClient {
    public static void main(String[] args) throws Exception {
        String exchangeNmae = "roberto.order";
        String queueName = "roberto.order.add";
        String direct = "add";
        Connection conn = ConnectionUtil.getConn("RGP订单系统Client端");
        Channel channel = conn.createChannel();

        // 声明一个队列
        channel.queueDeclare(queueName, true, false, false, new HashMap<String, Object>());
        channel.exchangeDeclare(exchangeNmae, BuiltinExchangeType.DIRECT, true, false, new HashMap<String, Object>());
        //队列与交换器通过路由绑定
        channel.queueBind(queueName, exchangeNmae, direct, new HashMap<String, Object>());

        String replyTo = "roberto.order.add.replay";
        //声明要关注的队列
        channel.queueDeclare(replyTo, true, false, false, new HashMap<String, Object>());
        String correlationId = UUID.randomUUID().toString();
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder().deliveryMode(2).contentType("UTF-8").correlationId(correlationId).replyTo(replyTo).build();
        channel.basicPublish(exchangeNmae, direct, true, basicProperties, "订单消息信息".getBytes());
        //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        channel.basicConsume(replyTo, true, "RGP订单系统Client端", new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("----------RPC调用结果----------");
                System.out.println(consumerTag);
                System.out.println("消息属性为:" + properties);
                System.out.println("消息内容为" + new String(body));
            }
        });
    }
}
