package university.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGPoolingDataSource;

public class DataSource {
	PGPoolingDataSource source = new PGPoolingDataSource();
	
	public DataSource() {
		source.setDataSourceName("PostgreSQL Data Source");
		source.setServerName("localhost");
		source.setDatabaseName("univerdb");
		source.setUser("user.name");
		source.setPassword("user.pasw");
		source.setMaxConnections(10);
	}
	
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = source.getConnection();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
		return connection;
	}
}
