package women.model;

public class Durga {

	private String name;

	public Durga(String name, String lat, String lng, String phone,
			String emailId) {
		super();
		this.name = name;
		this.lat = lat;
		this.lng = lng;
		this.phone = phone;
		this.emailId = emailId;
	}

	public Durga() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	private String lat;
	private String lng;
	private String phone;
	private String emailId;

}
