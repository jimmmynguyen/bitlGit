package com.example.BTL_IOT.Model;

import java.util.ArrayList;

public class Timekeeping {
	private Employee employee;
	private ArrayList<Integer>listTimekeeping;
	private int sum;
	
	public Timekeeping() {
		super();
	}



	public Timekeeping(Employee employee) {
		super();
		listTimekeeping = new ArrayList<>();
		this.employee = employee;
	}



	public Employee getEmployee() {
		return employee;
	}



	public void setEmployee(Employee employee) {
		this.employee = employee;
	}



	public ArrayList<Integer> getListTimekeeping() {
		return listTimekeeping;
	}



	public void setListTimekeeping(ArrayList<Integer> listTimekeeping) {
		this.listTimekeeping = listTimekeeping;
	}



	public int getSum() {
		return sum;
	}

	public void setSum(ArrayList<Integer>list) {
		int k = 0;
		for(int i=0;i<list.size();i++) {
			if (list.get(i)==1)k+=1;
		}
		this.sum = k;
	}

	@Override
	public String toString() {
		return "Timekeeping [employee=" + employee + ", listTimekeeping=" + listTimekeeping + ", sum=" + sum + "]";
	}
	
}
