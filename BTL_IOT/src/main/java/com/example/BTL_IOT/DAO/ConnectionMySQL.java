package com.example.BTL_IOT.DAO;

import java.sql.DriverManager;

public class ConnectionMySQL {
	public java.sql.Connection getConnection(){ 
        java.sql.Connection connection = null;
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbc_demo", "root", "tinh030892");
        } catch (Exception e) {
        }
        return connection;
    }
    public static void closeConnection(ConnectionMySQL con){
        if (con!=null){
            try {
                con.clone();
            } catch (Exception e) {
            }
        }
    }
    
    public static void main(String[] args) {
		ConnectionMySQL conn = new ConnectionMySQL();
		System.out.println(conn.getConnection());
	}
}
