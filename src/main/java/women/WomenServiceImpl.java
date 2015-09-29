package women;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import women.model.Durga;
import women.service.WomenService;

@Component
public class WomenServiceImpl implements WomenService {

	private static Connection dbConnection;

	private void sendMails(List<String> localDurgas) {
		for (String emailId : localDurgas) {
			SendMailTLS.sendEmail(emailId);
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
			//TODO provide username /password
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/womensafety", "",
					"");
			return connection;

		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();

		}
		return connection;
	}

	public void sendWarning(String lat, String lng) {
		List<String> localDurgas = findEmailIdOfLocalDuragas(lat, lng);
		sendMails(localDurgas);

	}

	private List<String> findEmailIdOfLocalDuragas(String lat, String lng) {
		List<String> localDurgasNumbers = new ArrayList<String>();

		String localDurgas = "SELECT EMAILID FROM durgas WHERE enable_durga=true AND lat = ? AND lng = ?";

		PreparedStatement preparedStatement = null;
		try {
			dbConnection = getConnection();
			preparedStatement = dbConnection.prepareStatement(localDurgas);
			preparedStatement.setString(1, lat);
			preparedStatement.setString(2, lng);

			// execute insert SQL statement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				localDurgasNumbers.add(rs.getString("PHONENUMBER"));
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
