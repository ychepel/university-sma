package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import university.Faculty;

public class FacultyDaoImplementation implements FacultyDao {

	private PostgresqlDaoFactory daoFactory = new PostgresqlDaoFactory();
	
	@Override
	public Set<Faculty> get() throws DAOException {
		String sql = "select * from faculty";
		
		Set<Faculty> faculties = new HashSet<>(); 
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			
			while(resultSet.next()) {
				Faculty faculty = new Faculty(resultSet.getString("Name"));
				faculty.setId(resultSet.getInt("Id"));
				
				faculties.add(faculty);
				
			}
		}
		catch (SQLException e) {
			throw new DAOException("Cannot get Faculty data", e);
		}
		finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
			}
			catch(SQLException e) {
				throw new DAOException("Cannot close result set", e);
			}
			
			try {
				if(statement != null) {
					statement.close();
				}
			}
			catch(SQLException e) {
				throw new DAOException("Cannot close statement", e);
			}
			
			try {
				if(connection != null) {
					connection.close();
				}
			}
			catch(SQLException e) {
				throw new DAOException("Cannot close connection", e);
			}
		}
		
		return faculties;
	}
	
	@Override
	public Faculty get(Integer id) throws DAOException {
		String sql = "select * from faculty where 'Id'=?";
		
		Faculty faculty = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.execute();
			resultSet = statement.getGeneratedKeys();
			
			resultSet.next();
			faculty = new Faculty(resultSet.getString("Name"));
			faculty.setId(id);
		}
		catch (SQLException e) {
			throw new DAOException("Cannot get Faculty data", e);
		}
		finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
			}
			catch(SQLException e) {
				throw new DAOException("Cannot close result set", e);
			}
			
			try {
				if(statement != null) {
					statement.close();
				}
			}
			catch(SQLException e) {
				throw new DAOException("Cannot close statement", e);
			}
			
			try {
				if(connection != null) {
					connection.close();
				}
			}
			catch(SQLException e) {
				throw new DAOException("Cannot close connection", e);
			}
		}
		
		return faculty;
	}
	
	public static void main(String[] args) throws DAOException{
		FacultyDaoImplementation d = new FacultyDaoImplementation();
		Faculty f = d.get(1);
		
		System.out.println(f.getName());
	}

}
