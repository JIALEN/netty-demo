package com.alen.netty.ende.ende4.model;

/**
 * 消息类型
 *
 * @author alen
 * @create 2018-01-31 16:11
 **/
public enum  MessageType {
    SERVICE_REQ((byte) 0),
    SERVICE_RESP((byte) 1),
    ONE_WAY((byte) 2),
    //握手请求，应答
    LOGIN_REQ((byte) 3),
    LOGIN_RESP((byte) 4),
    //心跳请求，应答
    HEARTBEAT_REQ((byte) 5),
    HEARTBEAT_RESP( (byte) 6);

    private byte value;

    private MessageType(byte value) {
        this.value = value;
    }

    public byte value() {
        return this.value;
    }
}
