package com.example.BTL_IOT.Model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "device")
public class Device {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "name")
    private String name;
	
	@Column(name = "serial")
    private String serial;
	
	@Column(name = "description")
    private String description;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category", referencedColumnName = "id")
	private Category category;
	
	@Column(name="image")
	private String image;
	
	@Column(name = "day")
    private Date day;
    
	public Device() {

	}

	public Device(Long id) {
		super();
		this.id = id;
	}

	public Device(String name, String serial, String description, int cid, String image, Date day) {
		this.name = name;
		this.serial = serial;
		this.description = description;
		this.category = new Category(cid);
		this.image = image;
		this.day = day;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	@Override
	public String toString() {
		return "Device [id=" + id + ", name=" + name + ", serial=" + serial + ", description=" + description
				+ ", category=" + category + ", image=" + image + ", day=" + day + "]";
	}
	
	

}

