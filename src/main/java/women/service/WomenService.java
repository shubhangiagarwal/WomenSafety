package women.service;

import org.springframework.stereotype.Service;

import women.model.Durga;

@Service
public interface WomenService {

	public void sendWarning(String lat, String lng);

	public Durga registerDurga(String name, String lat, String lng,
			String phoneNumber,String emailId);

}
