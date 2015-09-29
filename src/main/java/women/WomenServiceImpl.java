package women;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import women.model.Durga;
import women.model.Location;
import women.service.WomenService;

@Component
public class WomenServiceImpl implements WomenService {

	@Autowired
	private JDBCDao dao;

	private void sendMails(List<String> localDurgas, String lat, String lng) {
		SendMailTLS sendMail = new SendMailTLS();
		for (String emailId : localDurgas) {
			sendMail.sendEmail(emailId, lat, lng);
		}
	}

	public Location sendWarning(String lat, String lng) {
		List<String> localDurgas = findEmailIdOfLocalDuragas(lat, lng);
		sendMails(localDurgas, lat, lng);
		return new Location(lat, lng);

	}

	private List<String> findEmailIdOfLocalDuragas(String lat, String lng) {

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
		List<String> rs = (List<String>) dao.query(localDurgas,
				new ResultSetExtractor<List<String>>() {

					public List<String> extractData(ResultSet resultSet)
							throws SQLException, DataAccessException {
						List<String> localDurgasNumbers = new ArrayList<String>();
						while (resultSet.next()) {
							localDurgasNumbers.add(resultSet
									.getString("EMAILID"));
						}
						return localDurgasNumbers;
					}
				}, minLat, maxLat, minLng, maxLng);

		return rs;

	}

	public Durga registerDurga(String name, String lat, String lng,
			String phoneNumber, String emailId) {

		Durga durga = null;
		String registerDurga = "insert into durgas(name,latitude,longitude,phoneNumber,enable_durga,emailId) values (?,?,?,?,?,?)";

		dao.save(registerDurga, name, lat, lng, phoneNumber, "1", emailId);
		durga = new Durga(name, lat, lng, phoneNumber, emailId);
		return durga;
	}
}
