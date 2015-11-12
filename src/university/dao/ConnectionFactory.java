package university.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class ConnectionFactory {

	private static Logger log = Logger.getLogger(DaoFactory.class);
	
	protected Connection getConnection() throws DaoException{
		Connection connection = null;
		log.debug("Create new DataSource Connection");
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			DataSource source = (DataSource) envContext.lookup("jdbc/univerdb");
			connection = source.getConnection();
		}
		catch (NamingException e) {
			log.fatal("Cannot get Connection", e);
			throw new DaoException("Cannot get Connection", e);
		} 
		catch (SQLException e) {
			log.fatal("Cannot get Connection", e);
			throw new DaoException("Cannot get Connection", e);
		}
		return connection;
	}
}
