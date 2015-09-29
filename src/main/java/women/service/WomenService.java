package women.service;

import org.springframework.stereotype.Service;

import women.model.Durga;
import women.model.Location;

@Service
public interface WomenService {

	public Location sendWarning(String lat, String lng);

	public Durga registerDurga(String name, String lat, String lng,
			String phoneNumber,String emailId);

}
