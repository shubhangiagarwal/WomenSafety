package women;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class JDBCDao {

	private JdbcTemplate jdbcTemplate;

	public JDBCDao(javax.sql.DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public int save(String query, Object... args) {
		return jdbcTemplate.update(query, args);
	}

	public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) {
		return jdbcTemplate.query(sql, args, rse);
	}

}
