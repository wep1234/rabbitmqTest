package com.wep;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

/**
 * direct exchange 路由规则单个单词
 */
public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Connection conn = ConnectionUtil.getConn();
        Channel channel = conn.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        // diff
        String serverity = getServerity(new String[] { "test" });//指定路由
        String message = getMessage(new String[] { "test" });

        channel.basicPublish(EXCHANGE_NAME, serverity, null, message.getBytes());
        System.out.println("s[" + serverity + "]:[" + message + "]");

        channel.close();
        conn.close();
    }

    private static String getServerity(String[] strings) {
        return "waring";
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
