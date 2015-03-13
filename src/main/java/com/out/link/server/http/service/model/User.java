package com.out.link.server.http.service.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

	private static final long serialVersionUID = -5586727573484345992L;
	
	private long id;
	private String password;
	private String device_id;
	private String nick_name;
	private String country;
	private short gender = 0; //0 男性 1 女性
	private String avatar_url;
	private short data_status; //数据是否完整 0 不完整 1 完整
	private long max_point;//可以使用的字符数
	private long used_point ;//已经使用的字符数，单位个
	private Date create_time;
	private Date update_time;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public short getGender() {
		return gender;
	}
	public void setGender(short gender) {
		this.gender = gender;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public long getMax_point() {
		return max_point;
	}
	public void setMax_point(long max_point) {
		this.max_point = max_point;
	}
	public long getUsed_point() {
		return used_point;
	}
	public void setUsed_point(long used_point) {
		this.used_point = used_point;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public short getData_status() {
		return data_status;
	}
	public void setData_status(short data_status) {
		this.data_status = data_status;
	}
	
}
