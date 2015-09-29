package women;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import women.model.Durga;
import women.model.Location;
import women.service.WomenService;

@Component
public class WomenServiceImpl implements WomenService {

	private static Connection dbConnection;

	// @Value("${mysql.username}")
	private static String username = "root";

	// @Value("${mysql.password}")
	private static String password = System.getProperty("MYSQL_PASSWORD");

	private void sendMails(List<String> localDurgas, String lat, String lng) {
		SendMailTLS sendMail = new SendMailTLS();
		for (String emailId : localDurgas) {
			sendMail.sendEmail(emailId, lat, lng);
		}
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
			// TODO provide username /password
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/womensafety", username,
					password);
			return connection;

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		}
		return connection;
	}

	public Location sendWarning(String lat, String lng) {
		List<String> localDurgas = findEmailIdOfLocalDuragas(lat, lng);
		sendMails(localDurgas, lat, lng);
		return new Location(lat, lng);

	}

	private List<String> findEmailIdOfLocalDuragas(String lat, String lng) {
		List<String> localDurgasNumbers = new ArrayList<String>();

		Double minLat = Double.parseDouble(lat) - 10;
		Double maxLat = Double.parseDouble(lat) + 10;
		Double minLng = Double.parseDouble(lng) - 10;
		Double maxLng = Double.parseDouble(lng) + 10;
		if (minLat <= 0)
			minLat = 0.0;
		if (maxLat <= 0)
			maxLat = 0.0;
		if (minLng <= 0)
			minLng = 0.0;
		if (maxLng <= 0)
			maxLng = 0.0;
		String localDurgas = "SELECT EMAILID FROM durgas WHERE enable_durga=true AND latitude between ? AND ? AND longitude between ? AND ?";

		PreparedStatement preparedStatement = null;
		try {
			dbConnection = getConnection();
			preparedStatement = dbConnection.prepareStatement(localDurgas);
			preparedStatement.setString(1, minLat.toString());
			preparedStatement.setString(2, maxLat.toString());
			preparedStatement.setString(3, minLng.toString());
			preparedStatement.setString(4, maxLng.toString());

			// execute insert SQL statement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				localDurgasNumbers.add(rs.getString("EMAILID"));
			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return localDurgasNumbers;

	}

	public Durga registerDurga(String name, String lat, String lng,
			String phoneNumber, String emailId) {
		PreparedStatement preparedStatement = null;
		Durga durga = null;
		try {
			dbConnection = getConnection();
			String registerDurga = "insert into durgas(name,latitude,longitude,phoneNumber,enable_durga,emailId) values (?,?,?,?,?,?)";
			preparedStatement = dbConnection.prepareStatement(registerDurga);

			preparedStatement.setString(1, name);
			preparedStatement.setString(2, lat);
			preparedStatement.setString(3, lng);
			preparedStatement.setString(4, phoneNumber);
			preparedStatement.setBoolean(5, true);
			preparedStatement.setString(6, emailId);

			// execute insert SQL stetement
			preparedStatement.executeUpdate();

			durga = new Durga(name, lat, lng, phoneNumber, emailId);
			System.out.println("Record is inserted into DBUSER table!");

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (dbConnection != null) {
				try {
					dbConnection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return durga;
	}
}
