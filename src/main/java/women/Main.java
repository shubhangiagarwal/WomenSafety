package women;

import java.sql.SQLException;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = Main.class)
@EnableAutoConfiguration
public class Main {



	public static void main(String[] args) throws SQLException {
		SpringApplication.run(Main.class, args);
	}

}
