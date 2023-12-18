package com.example.BTL_IOT.Model;

import java.sql.Date;

import jakarta.persistence.Entity;

public class DeviceDto {
	private long id;
	private String name;
	private String serial;
	private String image;
	private Date day;
	private String category;
	
	public DeviceDto() {
		
	}
	
	public DeviceDto(long id, String name, String serial, String image, Date day, String category) {

		this.id = id;
		this.name = name;
		this.serial = serial;
		this.image = image;
		this.day = day;
		this.category = category;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "DeviceDto [id=" + id + ", name=" + name + ", serial=" + serial + ", image=" + image + ", day=" + day
				+ ", category=" + category + "]";
	}
	
}
