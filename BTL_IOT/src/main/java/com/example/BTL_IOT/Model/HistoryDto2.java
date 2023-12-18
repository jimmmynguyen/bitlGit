package com.example.BTL_IOT.Model;

import java.sql.Date;

public class HistoryDto2 {
	private long id;
	private String eName;
	private long deviceId;
	private String cardId;
	private String department;
	private String date;
	private String time_in;
	private String state;
	
	public HistoryDto2() {
		super();
	}

	public HistoryDto2(long id, String eName, long deviceId, String cardId, String department, String date,
			String time_in) {
		super();
		this.id = id;
		this.eName = eName;
		this.deviceId = deviceId;
		this.cardId = cardId;
		this.department = department;
		this.date = date;
		this.time_in = time_in;
		this.state = time_in.compareTo("13:00:00") < 0 ? "Đúng giờ" : "Muộn giờ";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime_in() {
		return time_in;
	}

	public void setTime_in(String time_in) {
		this.time_in = time_in;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
