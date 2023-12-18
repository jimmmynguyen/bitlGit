package com.example.BTL_IOT.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkCalendar {
	private String T;
	private String day;
	
	public WorkCalendar(String t, String day) {
		super();
		T = t;
		this.day = day;
	}

	public WorkCalendar(String day) {
		super();
		this.day = day;
	}

	public String getT() {
		return T;
	}

	public void setT(String d, int month, int year) {
		SimpleDateFormat sdf = new SimpleDateFormat("u");
	    Date date = null;
	    int day = Integer.parseInt(d);
	    try {
	        date = new SimpleDateFormat("dd-MM-yyyy").parse(String.format("%02d-%02d-%04d", day, month, year));
	    } catch (Exception e) {
	        e.printStackTrace();
	        this.T = "Không hợp lệ";
	    }
	    String dayOfWeek = sdf.format(date);
	    
	    if (dayOfWeek.equals("1")) {
	    	this.T = "2"; // Thứ Hai
	    } else if (dayOfWeek.equals("7")) {
	    	this.T = "CN"; // Chủ Nhật
	    } else {
	    	this.T =  Integer.toString(Integer.parseInt(dayOfWeek) + 1);
	    }
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return "WorkCalendar [T=" + T + ", day=" + day + "]";
	}
	
}
