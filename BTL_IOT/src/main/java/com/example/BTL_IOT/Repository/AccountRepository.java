package com.example.BTL_IOT.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.BTL_IOT.Model.Account;
import com.example.BTL_IOT.Model.Employee;
@Repository
public interface AccountRepository extends CrudRepository<Account, Long>{
	@Query("SELECT COUNT(a) > 0 FROM Account a WHERE a.username = :username AND a.password = :password")
	public boolean checkExistsAccount(@Param("username") String username, @Param("password") String password);
	
	public Account getAccountByUsernameAndPassword(String username,String password);
	
	public Account getAccountByEmployee(Employee employee);
}
