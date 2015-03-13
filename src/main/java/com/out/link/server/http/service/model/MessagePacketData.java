package com.out.link.server.http.service.model;

import java.util.Date;

public class MessagePacketData {
	private long sender;
	private String senderAvatarUrl;
	private long receiver;
	private String text;
	private Date sentDate;
	public long getSender() {
		return sender;
	}
	public void setSender(long sender) {
		this.sender = sender;
	}
	public long getReceiver() {
		return receiver;
	}
	public void setReceiver(long receiver) {
		this.receiver = receiver;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	public String getSenderAvatarUrl() {
		return senderAvatarUrl;
	}
	public void setSenderAvatarUrl(String senderAvatarUrl) {
		this.senderAvatarUrl = senderAvatarUrl;
	}
	
}
