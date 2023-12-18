package com.example.BTL_IOT.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "duty")
	private int duty;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_nv", referencedColumnName = "id")
	private Employee employee;
	
	public Account() {
		
	}
	
	public Account(Employee employee) {
		super();
		this.employee = employee;
	}

	public Account(int id) {
		super();
		this.id = id;
	}

	public Account(int id, String username, String password, String email, int duty,String id_nv) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.duty = duty;
		this.employee = new Employee(id_nv);
	}
	
	
	public Account(String username, String password, String email, int duty) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.duty = duty;
	}	

	public Employee getEmployee() {
		return employee;
	}


	public void setEmployee(Employee employee) {
		this.employee = employee;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getDuty() {
		return duty;
	}
	public void setDuty(int duty) {
		this.duty = duty;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email
				+ ", duty=" + duty + "]";
	}
	
}
