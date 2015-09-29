package women;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackageClasses = Main.class)
@EnableAutoConfiguration
public class Main {

	@Autowired
	private DataSourceProperties dataSourceProperties;

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public JDBCDao dashboardDataSource(DataSource dataSource) {

		return new JDBCDao(dataSource);
	}

	@Bean
	public DataSource getDataSource() {

		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(dataSourceProperties.getUrl());
		hikariConfig.setMaximumPoolSize(100);
		hikariConfig.setUsername(dataSourceProperties.getUsername());
		hikariConfig.setPassword(dataSourceProperties.getPassword());
		hikariConfig.setDriverClassName(dataSourceProperties
				.getDriverClassName());
		hikariConfig.setIdleTimeout(45000);

		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		return hikariDataSource;
	}
}
