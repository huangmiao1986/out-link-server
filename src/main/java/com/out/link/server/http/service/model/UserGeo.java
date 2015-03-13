package com.out.link.server.http.service.model;

import java.util.Date;

public class UserGeo {
	private long id;
	private long userId;
	private double latitude;
	private double longitude;
	private String country;
	private Date createTime;
	private Date updateeTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateeTime() {
		return updateeTime;
	}
	public void setUpdateeTime(Date updateeTime) {
		this.updateeTime = updateeTime;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
}
