package university;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.postgresql.ds.PGPoolingDataSource;

@WebServlet("/fcount")
public class TestDataSource extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		try {
			Connection conn = initConnection();
			
			Statement statement = conn.createStatement();
			String sql = "SELECT COUNT(*) FROM FACULTY";
			ResultSet rs = statement.executeQuery(sql);
			
			rs.next();
			writer.println("Faculty Records Count is " + rs.getInt(1));
		} catch (NamingException ex) {
			System.err.println(ex);
		} catch (SQLException ex) {
			System.err.println(ex);
		}
	}
	
	public Connection initConnection() throws NamingException, SQLException {
		InitialContext initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup( "java:comp/env" );
		DataSource dataSource = (DataSource) envContext.lookup("jdbc/univerdb");
		
		Connection connection = dataSource.getConnection();
		
		return connection;
	}
}