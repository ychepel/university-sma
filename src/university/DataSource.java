package university;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.postgresql.ds.PGPoolingDataSource;

public class DataSource {
	
	private String user = "user.name";
	private String password = "user.pasw";
	private String dbName = "univerdb";
	private String serverName = "localhost:5432";
	private Integer maxConnection = 10;
	
	public Connection getConnection() throws SQLException {
		PGPoolingDataSource source = new PGPoolingDataSource();
		source.setDataSourceName("PostgreSQL DB");
		source.setServerName(serverName);
		source.setDatabaseName(dbName);
		source.setUser(user);
		source.setPassword(password);
		source.setMaxConnections(maxConnection);
			
		Connection connection = source.getConnection();
		return connection;
	}
	
	public static void main (String[] args) throws NamingException, SQLException {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				  "com.sun.enterprise.naming.SerialInitContextFactory");
		env.put(Context.PROVIDER_URL,
				  "localhost:5432");
		
		InitialContext initContext = new InitialContext(env);
		Context envContext = (Context) initContext.lookup( "java:/comp/env" );
		DataSource dataSource = (DataSource) envContext.lookup("jdbc/univerdb");
		
		Connection connection = dataSource.getConnection();
	}
}