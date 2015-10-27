package university.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {
	
	private String user = "user.name";
	private String password = "user.pasw";
	private String url = "jdbc:postgresql://localhost:5432/univerdb";
	private String driver = "org.postgresql.Driver";
	
	public DaoFactory() {
		try {
			Class.forName(driver);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
	
}
