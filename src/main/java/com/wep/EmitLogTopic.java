package com.wep;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * topic exchange 路由规则可以是 <level>.<source> 允许通配符 *代表一个单词，#代表一个多个单词。
 */
public class EmitLogTopic {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Connection conn = ConnectionUtil.getConn();
        Channel channel = conn.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        //指定路由
        String routekey = getServerity(new String[] { "test" });
        String message = getMessage(new String[] { "test" });

        channel.basicPublish(EXCHANGE_NAME, routekey, null, message.getBytes());
        System.out.println("s[" + routekey + "]:[" + message + "]");

        channel.close();
        conn.close();
    }

    private static String getServerity(String[] strings) {
        return "kern.critical";
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 1) {
            return "Hello World!";
        }
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0) {
            return "";
        }
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
