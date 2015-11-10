package university.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.postgresql.ds.PGPoolingDataSource;

public class ConnectionFactory {
	private String sourceName = "PostgreSQL Data Source";
	private String serverName = "localhost";
	private String dbName = "univerdb";
	private String userName = "user.name";
	private String userPassword = "user.pasw"; 
	private Integer maxConnection = 10;
	
	private static PGPoolingDataSource source = null;
	private static Logger log = Logger.getLogger(DaoFactory.class);
	
	protected ConnectionFactory() {
		if(source == null) {
			log.info("Create DataSource Pool");
			source = new PGPoolingDataSource();
			source.setDataSourceName(sourceName);
			source.setServerName(serverName);
			source.setDatabaseName(dbName);
			source.setUser(userName);
			source.setPassword(userPassword);
			source.setMaxConnections(maxConnection);
		}
	}
	
	protected Connection getConnection() throws SQLException{
		log.debug("Create new DataSource Connection");
		Connection connection = source.getConnection();
		return connection;
	}
}
