package women;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.twilio.sdk.TwilioRestException;

public class ServiceImpl {

	private static Connection dbConnection;

	public void insertRecordIntoTable(String cameraNo) throws SQLException {

		// get camera location by camera'no
		String location = getLocation(cameraNo);

		// find local duragas and send msgs

		List<String> localDurgas = findLocalDuragas(location);

		try {
			sendMessage(localDurgas);
		} catch (TwilioRestException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String insertTableSQL = "INSERT INTO Events (CameraNo, Location,created_time) VALUES(?,?,?)";

		PreparedStatement preparedStatement = null;
		try {
			dbConnection = getConnection();
			preparedStatement = dbConnection.prepareStatement(insertTableSQL);

			preparedStatement.setString(1, cameraNo);
			preparedStatement.setString(2, location);
			preparedStatement.setTimestamp(3, getCurrentTimeStamp());

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

			System.out.println("Record is inserted into DBUSER table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}

	private void sendMessage(List<String> localDurgas)
			throws TwilioRestException {
		TwilioTest test = new TwilioTest();
		for (String phone : localDurgas) {
			test.sendMessage(phone);
		}
	}

	private List<String> findLocalDuragas(String location) throws SQLException {

		List<String> localDurgasNumbers = new ArrayList<String>();

		String localDurgas = "SELECT PHONENUMBER FROM volunteers WHERE enable_durga=true AND LOCATION = ?";

		PreparedStatement preparedStatement = null;
		try {
			dbConnection = getConnection();
			preparedStatement = dbConnection.prepareStatement(localDurgas);
			preparedStatement.setString(1, location);

			// execute insert SQL statement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				localDurgasNumbers.add(rs.getString("PHONENUMBER"));
			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		return localDurgasNumbers;
	}

	private static String getLocation(String cameraNo) throws SQLException {
		String selectLocationOfCamera = "SELECT location FROM camera_details "
				+ "WHERE CAMERA_ID = ?";

		PreparedStatement preparedStatement = null;
		String location = null;
		try {
			dbConnection = getConnection();
			preparedStatement = dbConnection
					.prepareStatement(selectLocationOfCamera);
			preparedStatement.setString(1, cameraNo);

			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			location = rs.getString("location");
			System.out.println("Location of camera found " + location);

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		return location;

	}

	public static Connection getConnection() {
		System.out
				.println("-------- MySQL JDBC Connection Testing ------------");

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return null;
		}

		System.out.println("MySQL JDBC Driver Registered!");
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/womensafety", "root",
					"hasher123");
			return connection;

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		}
		return connection;
	}

	private static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}
}
