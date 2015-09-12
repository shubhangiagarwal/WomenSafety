package women;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Controller {

	public static void main(String[] args) {
		SpringApplication.run(Controller.class, args);

	}

	@RequestMapping("/getnotification")
	public void getNotification(@RequestParam("CameraNo") String cameraNo) {

		ServiceImpl impl = new ServiceImpl();
		try {
			impl.insertRecordIntoTable(cameraNo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
