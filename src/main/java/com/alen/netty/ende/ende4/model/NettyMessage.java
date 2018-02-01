package com.alen.netty.ende.ende4.model;

/**
 * 请求对象
 * @author alen
 * @create 2018-01-31 16:10
 **/
public class NettyMessage {

    private Header header;
    private Object body;

    public final Header getHeader() {
        return header;
    }

    public final void setHeader(Header header) {
        this.header = header;
    }

    public final Object getBody() {
        return body;
    }

    public final void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage [header=" + header + "]";
    }
}
