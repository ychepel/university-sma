package university.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DaoFactory {
	
	private String user = "user.name";
	private String password = "user.pasw";
	private String url = "jdbc:postgresql://localhost:5432/univerdb";
	private String driver = "org.postgresql.Driver";
	
	private static Logger log = Logger.getLogger(DaoFactory.class);
	
	public DaoFactory() {
		try {
			Class.forName(driver);
		}
		catch (ClassNotFoundException e) {
			log.fatal("Class Not Found", e);
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
	
}
