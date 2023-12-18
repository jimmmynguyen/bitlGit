package com.example.BTL_IOT.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.BTL_IOT.Model.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long>{

}
