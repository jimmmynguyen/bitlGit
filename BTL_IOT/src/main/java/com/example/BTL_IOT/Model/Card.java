package com.example.BTL_IOT.Model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="card")
public class Card {
	@Id
    private String id;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_e", referencedColumnName = "id")
	private Employee employee;
	
	@Column(name = "day")
    private Date day;

	public Card() {
		super();
	}	

	public Card(String id) {
		super();
		this.id = id;
	}

	public Card(String id, String employeeId, Date day) {
		super();
		this.id = id;
		this.employee = new Employee(employeeId);
		this.day = day;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", employee=" + employee + ", day=" + day + "]";
	}
		
}
