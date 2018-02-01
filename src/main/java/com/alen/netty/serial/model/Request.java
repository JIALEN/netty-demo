package com.alen.netty.serial.model;

import java.io.Serializable;

/**
 * 有些序列化方式需要 implements Serializable，有些不需要
 */
public class Request implements Serializable{

	private static final long  SerialVersionUID = 1L;

	private String id ;
	private String name ;
	private String requestMessage ;
	private byte[] attachment;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRequestMessage() {
		return requestMessage;
	}
	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}
	public byte[] getAttachment() {
		return attachment;
	}
	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
}
