package women;

import java.sql.SQLException;

public class test {
	
	public static void main(String[] args) throws SQLException {
		
		ServiceImpl serviceImpl = new ServiceImpl();
		serviceImpl.insertRecordIntoTable("123");
	}

}
