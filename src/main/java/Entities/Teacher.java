package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import PL53.util.Random;
import Utils.Database;

public class Teacher {
	private int ID = -1;
	private String name, surname, phone, email, fiscalNumber;
	

	public Teacher(int ID, String name, String surname, String phone, String email, String fiscalNumber) {
		this.ID = ID;
		this.name = name;
		this.surname = surname; 
		this.phone = phone;
		this.email = email; 
		this.fiscalNumber = fiscalNumber;
	}
	
	public Teacher(String name, String surname, String phone, String email, String fiscalNumber) {
		this.name = name;
		this.surname = surname; 
		this.phone = phone;
		this.email = email; 
		this.fiscalNumber = fiscalNumber;
	}

	public Teacher() {
		Random r = new Random();
		this.name = r.name(3, 10);
		this.surname = r.name(3, 10);
		this.phone = "";
		this.email = ""; 
		this.fiscalNumber = "";
	}

	public static String tableName() {
		return "Teacher";
	}

	public void insert(Database db) throws SQLException {
		Connection conn = db.getConnection(); // Obtain the connection
		PreparedStatement pstmt;
		
		if(this.getID() == -1) {
			String sql = "INSERT INTO " + tableName() + " VALUES (null, ?, ?, ?, ?, ?)";
	
			pstmt = conn.prepareStatement(sql);
			// Sets of the parameters of the prepared statement
	
			pstmt.setString(1, this.getName());
			pstmt.setString(2, this.getSurname());
			pstmt.setString(3, this.getPhone());
			pstmt.setString(4, this.getEmail());
			pstmt.setString(5, this.getFiscalNumber());

			pstmt.executeUpdate();
			
			ResultSet tableKeys = pstmt.getGeneratedKeys();
			tableKeys.next();
			this.ID = tableKeys.getInt(1);
			pstmt.close();
			tableKeys.close();
		}else {
			String sql = "INSERT INTO " + tableName() + " (ID_teacher, name) VALUES (?, ?, ?, ?, ?, ?)";
	
			pstmt = conn.prepareStatement(sql);
			// Sets of the parameters of the prepared statement
	
			pstmt.setInt(1, this.getID());
			pstmt.setString(2, this.getName());
			pstmt.setString(3, this.getSurname());
			pstmt.setString(4, this.getPhone());
			pstmt.setString(5, this.getEmail());
			pstmt.setString(6, this.getFiscalNumber());

			pstmt.executeUpdate();
			pstmt.close();
		}
		
		conn.close();
	}

	public static List<Teacher> get(String sql, Database db) throws SQLException {
		List<Teacher> list = new ArrayList<Teacher>();

		Connection conn = db.getConnection();
		// Statement object needed to send statements to the database
		Statement st = conn.createStatement();
		// executeQuery will return a resultSet
		ResultSet rs = st.executeQuery(sql);

		while (rs.next()) {
			Teacher t = new Teacher(rs.getInt("ID_teacher"), 
									rs.getString("name"), 
									rs.getString("surname"), 
									rs.getString("phone"), 
									rs.getString("email"), 
									rs.getString("fiscalNumber"));
			list.add(t);
		}

		st.close();
		conn.close();
		rs.close();
		conn.close();
		return list;
	}

	public static Teacher getOne(String sql, Database db) throws SQLException {
		Connection conn = db.getConnection();
		// Statement object needed to send statements to the database
		Statement st = conn.createStatement();
		// executeQuery will return a resultSet
		ResultSet rs = st.executeQuery(sql);

		rs.next();

		Teacher t = new Teacher(rs.getInt("ID_teacher"),
								rs.getString("name"), 
								rs.getString("surname"), 
								rs.getString("phone"), 
								rs.getString("email"), 
								rs.getString("fiscalNumber"));

		rs.close();
		st.close();
		conn.close();
		
		return t;
	}
	public Teacher getTeacherByName(Database db) throws SQLException {
		Connection conn = db.getConnection(); 
		PreparedStatement pstmt;
		
		String sql = "SELECT *  FROM " + tableName() + " WHERE name=? and surname=?";
		
		pstmt = conn.prepareStatement(sql);
		// Sets of the parameters of the prepared statement

		pstmt.setString(1, this.getName());
		pstmt.setString(2, this.getSurname());

		ResultSet rs = pstmt.executeQuery();
		
		rs.next();
		Teacher t = new Teacher(rs.getInt("ID_teacher"),
				rs.getString("name"), 
				rs.getString("surname"), 
				rs.getString("phone"), 
				rs.getString("email"), 
				rs.getString("fiscalNumber"));
		
		rs.close();
		pstmt.close();
		conn.close();
		
		return t;
	}

	public void removeTeacher(Database db) throws SQLException {
		Connection conn = db.getConnection(); 
		PreparedStatement pstmt;
		
		String sql = "DELETE FROM " + tableName() + " WHERE name=? and surname=?";
		
		pstmt = conn.prepareStatement(sql);
		// Sets of the parameters of the prepared statement

		pstmt.setString(1, this.getName());
		pstmt.setString(2, this.getSurname());

		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public void updateTeacherPhone(Database db, String phone) throws SQLException {
		Connection conn = db.getConnection(); 
		PreparedStatement pstmt;
		
		String sql = "UPDATE " + tableName() + " SET phone=? WHERE name=? and surname=?";
		
		pstmt = conn.prepareStatement(sql);
		// Sets of the parameters of the prepared statement

		pstmt.setString(1, phone);
		pstmt.setString(2, this.getName());
		pstmt.setString(3, this.getSurname());

		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public void updateTeacherEmail(Database db, String email) throws SQLException {
		Connection conn = db.getConnection(); 
		PreparedStatement pstmt;
		
		String sql = "UPDATE " + tableName() + " SET email=? WHERE name=? and surname=?";
		
		pstmt = conn.prepareStatement(sql);
		// Sets of the parameters of the prepared statement

		pstmt.setString(1, email);
		pstmt.setString(2, this.getName());
		pstmt.setString(3, this.getSurname());

		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	public void updateTeacherFiscalNumber(Database db, String fN) throws SQLException {
		Connection conn = db.getConnection(); 
		PreparedStatement pstmt;
		
		String sql = "UPDATE " + tableName() + " SET fiscalNumber=? WHERE name=? and surname=?";
		
		pstmt = conn.prepareStatement(sql);
		// Sets of the parameters of the prepared statement

		pstmt.setString(1, fN);
		pstmt.setString(2, this.getName());
		pstmt.setString(3, this.getSurname());

		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}
	
	public static boolean checkPhone(String phone) {
		// Regex pattern that matches a phone number
		String pattern = "^(\\(?\\+[1-9]{2}\\)?)? ?([0-9] ?){6,14}$";

		return phone.matches(pattern);
	}

	public static boolean checkEmail(String email) {
		// Regex pattern that matches a phone number
		String pattern = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

		return email.matches(pattern);
	}
	
	public static boolean checkFiscalNumber(String fiscalNumber) {
		// Regex pattern that matches a fiscal number
		String pattern = "^[A-Z]?[0-9]{8,8}[A-Z]$";
		
		return fiscalNumber.matches(pattern);
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getFiscalNumber() {
		return fiscalNumber;
	}

	public void setfiscalNumber(String fN) {
		this.fiscalNumber = fN;
	}
	
}
