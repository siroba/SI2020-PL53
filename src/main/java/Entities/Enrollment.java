package Entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import PL53.util.Date;
import PL53.util.DateTime;
import PL53.util.Random;
import Utils.Database;

public class Enrollment {
	private int ID_fa, ID_professional;
	private Status status;
	private DateTime timeEn;
	private String group; 

	/**
	 * Enrollment default constructor. The date and time are assumed to be today and now.
	 *
	 * @param ID_fa
	 * @param ID_professional
	 * @param status
	 * @param timeEn
	 */
	public Enrollment(int ID_fa, int ID_professional, Status status, DateTime timeEn, String group) {
		this.status = status;
		this.timeEn = timeEn;
		this.ID_fa = ID_fa;
		this.ID_professional = ID_professional;
		this.group = group;
	}

	/**
	 * Enrollment random constructor
	 */
	public Enrollment() {
		Random r = new Random();

		this.status = Status.values()[r.nextInt(Status.values().length)];
		this.timeEn = new DateTime(Date.random());
		this.ID_fa = -1;
		this.ID_professional = -1;
		this.group = "Standard";
	}

	/**
	 * @return Name of the table in the database
	 */
	public static String tableName() {
		return "Enrollment";
	}

	/**
	 * Method to delete all the elements from the table
	 *
	 * @throws SQLException
	 */
	public static void deleteAll(Database db) throws SQLException {
		String SQL = "DELETE FROM " + tableName();

		Connection conn = db.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(SQL);

		pstmt.executeUpdate();
		pstmt.close();
		conn.close();
	}

	/**
	 * Does the query you specify and returns a list with all the results
	 *
	 * @param query
	 * @param db
	 * @return
	 * @throws SQLException
	 */
	public static List<Enrollment> get(String query, Database db) throws SQLException {
		Connection conn = db.getConnection();
		// Statement object needed to send statements to the database
		Statement st = conn.createStatement();
		// executeQuery will return a resultSet
		ResultSet rs = st.executeQuery(query.toString());

		List<Enrollment> enrollments = new ArrayList<>();

		while (rs.next()) {
			DateTime d;
			try {
				d = DateTime.parseString(rs.getString("timeEn"));
			} catch (ParseException e) {
				d = DateTime.fromMillis(rs.getLong("timeEn"));
			}

			Enrollment e = new Enrollment(
					rs.getInt("ID_fa"),
					rs.getInt("ID_professional"),
					Status.valueOf(rs.getString("status").toUpperCase()),
					d,
					rs.getString("category")); // TODO: Fix parse

			enrollments.add(e);
		}

		// Very important to always close all the objects related to the database
		rs.close();
		st.close();
		conn.close();

		return enrollments;
	}

	/**
	 * Does the query you specify and returns the first result
	 *
	 * @param query
	 * @param db
	 * @return
	 * @throws SQLException
	 */
	public static Enrollment getOne(String query, Database db) throws SQLException {
		Connection conn = db.getConnection();
		// Statement object needed to send statements to the database
		Statement st = conn.createStatement();
		// executeQuery will return a resultSet
		ResultSet rs = st.executeQuery(query.toString());
		rs.next();

		DateTime d;
		try {
			d = DateTime.parseString(rs.getString("timeEn"));
		} catch (ParseException e) {
			d = DateTime.fromMillis(rs.getLong("timeEn"));
		}

		Enrollment e = new Enrollment(
					rs.getInt("ID_fa"),
					rs.getInt("ID_professional"),
					Status.valueOf(rs.getString("status").toUpperCase()),
					d,
					rs.getString("category")); // TODO: Fix parse

		// Very important to always close all the objects related to the database
		rs.close();
		st.close();
		conn.close();

		return e;
	}

	/**
	 * Inserts all the given enrollments into the given database
	 *
	 * @param enrollments
	 * @param db
	 * @throws SQLException
	 * @throws ParseException 
	 */
	public static void insert(List<Enrollment> enrollments, Database db) throws SQLException, ParseException {
		for (Enrollment e : enrollments)
			e.insert(db);
	}

	/**
	 * Inserts itself into the given database
	 *
	 * @param db
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void insert(Database db) throws SQLException, ParseException {
		/*
		 * status TEXT NOT NULL CHECK( status IN('received','confirmed','cancelled')),
		 * dateEn DATE NOT NULL, name TEXT NOT NULL, ID_fa INTEGER NOT NULL UNIQUE,
		 * ID_student INTEGER NOT NULL UNIQUE,
		 */

		String SQL = "INSERT INTO " + tableName() + "(ID_fa, ID_professional, status, timeEn, category) VALUES(?,?,?,?,?)";

		Connection conn = db.getConnection(); // Obtain the connection
		// Prepared Statement initialized with the INSERT statement
		PreparedStatement pstmt = conn.prepareStatement(SQL);
		// Sets of the parameters of the prepared statement

		pstmt.setInt(1, this.getID_fa());
		pstmt.setInt(2, this.getID_professional());
		pstmt.setString(3, this.getStatus().toString().toUpperCase());
		pstmt.setTimestamp(4, this.getTimeEn().toTimestamp());
		pstmt.setString(5, this.getGroup());

		pstmt.executeUpdate(); // statement execution

		pstmt.close();
		conn.close();
	}



	public int getID_fa() {
		return ID_fa;
	}

	public void setID_fa(int iD_fa) {
		ID_fa = iD_fa;
	}

	public int getID_professional() {
		return ID_professional;
	}

	public void setID_professional(int iD_professional) {
		ID_professional = iD_professional;
	}

	public DateTime getTimeEn() {
		return timeEn;
	}

	public void setTimeEn(DateTime timeEn) {
		this.timeEn = timeEn;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public enum Status {
		RECEIVED, CONFIRMED, CANCELLED;
	}
}