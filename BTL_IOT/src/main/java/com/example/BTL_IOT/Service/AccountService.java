package com.example.BTL_IOT.Service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BTL_IOT.Model.Account;
import com.example.BTL_IOT.Model.Employee;
import com.example.BTL_IOT.Repository.AccountRepository;


@Service
public class AccountService {
	@Autowired
	private AccountRepository accountRepository;
	
	public ArrayList<Account> getAllAcount () {
		return (ArrayList<Account>) accountRepository.findAll();
	}
	
	public boolean checkAccount (String user, String pass) {
		return accountRepository.checkExistsAccount(user, pass);
	}
	
	public Account getAccount (String user, String pass) {
		return accountRepository.getAccountByUsernameAndPassword(user, pass);
	}
	
	public Account getAccountByEmployee(Employee employee) {
		return accountRepository.getAccountByEmployee(employee);
	}
}
