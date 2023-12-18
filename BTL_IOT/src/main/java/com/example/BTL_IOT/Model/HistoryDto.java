package com.example.BTL_IOT.Model;

import java.sql.Date;

public class HistoryDto {
	private long id;
	private String eName;
	private long deviceId;
	private String cardId;
	private String department;
	private Date date;
	private String time_in;
	
	public HistoryDto() {
		super();
	}

	public HistoryDto(long id, String eName, long deviceId, String cardId, String department, Date date, String time_in
			) {
		super();
		this.id = id;
		this.eName = eName;
		this.deviceId = deviceId;
		this.cardId = cardId;
		this.department = department;
		this.date = date;
		this.time_in = time_in;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTime_in() {
		return time_in;
	}

	public void setTime_in(String time_in) {
		this.time_in = time_in;
	}

	
}
