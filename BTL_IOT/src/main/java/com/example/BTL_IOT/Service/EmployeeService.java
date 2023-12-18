package com.example.BTL_IOT.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BTL_IOT.Model.Employee;
import com.example.BTL_IOT.Repository.EmployeeRepository;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public ArrayList<Employee>getAllEmployees(){
		return (ArrayList<Employee>) employeeRepository.findAll();
	}
}
