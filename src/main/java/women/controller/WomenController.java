package women.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import women.model.Durga;
import women.model.Location;
import women.service.WomenService;

@RestController
@RequestMapping("/api/v1")
public class WomenController {

	@Autowired
	private WomenService womenService;

	@RequestMapping("/warning")
	public ResponseEntity<Location> getWarning(@RequestParam("lat") String lat,
			@RequestParam("lng") String lng) {

		return new ResponseEntity<Location>(womenService.sendWarning(lat, lng),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/registerDurga", method = RequestMethod.POST)
	public ResponseEntity<Durga> regsiterDurga(
			@RequestParam("name") String name, @RequestParam("lat") String lat,
			@RequestParam("lng") String lng,
			@RequestParam("phone") String phoneNumber,
			@RequestParam("email") String emailId) {

		return new ResponseEntity<Durga>(womenService.registerDurga(name, lat,
				lng, phoneNumber, emailId), HttpStatus.CREATED);
	}
}
