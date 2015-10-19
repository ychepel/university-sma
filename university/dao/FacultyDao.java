package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import university.domain.Faculty;

public class FacultyDao {

	private DaoFactory daoFactory = new DaoFactory();
	
	public Set<Faculty> getFaculties() throws DaoException {
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
				Faculty faculty = new Faculty(resultSet.getString("name"));
				faculty.setId(resultSet.getInt("id"));
				
				faculties.add(faculty);
				
			}
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get Faculty data", e);
		}
		finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
			}
			catch(SQLException e) {
				throw new DaoException("Cannot close result set", e);
			}
			
			try {
				if(statement != null) {
					statement.close();
				}
			}
			catch(SQLException e) {
				throw new DaoException("Cannot close statement", e);
			}
			
			try {
				if(connection != null) {
					connection.close();
				}
			}
			catch(SQLException e) {
				throw new DaoException("Cannot close connection", e);
			}
		}
		
		return faculties;
	}
	
	public Faculty getById(Integer id) throws DaoException {
		String sql = "select * from faculty where id=?";
		
		Faculty faculty = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			faculty = new Faculty(resultSet.getString("name"));
			faculty.setId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get Faculty data", e);
		}
		finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
			}
			catch(SQLException e) {
				throw new DaoException("Cannot close result set", e);
			}
			
			try {
				if(statement != null) {
					statement.close();
				}
			}
			catch(SQLException e) {
				throw new DaoException("Cannot close statement", e);
			}
			
			try {
				if(connection != null) {
					connection.close();
				}
			}
			catch(SQLException e) {
				throw new DaoException("Cannot close connection", e);
			}
		}
		
		return faculty;
	}
	
	public Faculty updateFaculty(Integer id, String name) throws DaoException {
		String sql = "update faculty set name=? where id=?";
		
		Faculty faculty = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setInt(2, id);
			statement.executeUpdate();
			
			faculty = new Faculty(name);
			faculty.setId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot update Facultys data", e);
		}
		finally {
			try {
				if(statement != null) {
					statement.close();
				}
			}
			catch(SQLException e) {
				throw new DaoException("Cannot close statement", e);
			}
			
			try {
				if(connection != null) {
					connection.close();
				}
			}
			catch(SQLException e) {
				throw new DaoException("Cannot close connection", e);
			}
		}
		
		return faculty;
	}
	
	public Faculty createFaculty(String name) throws DaoException {
		String sql = "insert into faculty (name) values ('" + name + "')";
		
		Faculty faculty = null; 
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			
			resultSet = statement.executeQuery("select last_insert_id() as last_id from faculty");
			resultSet.next();
			Integer id = resultSet.getInt("last_id");
			
			faculty = new Faculty(name);
			faculty.setId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot create Facultys data", e);
		}
		finally {
			try {
				if(resultSet != null) {
					resultSet.close();
				}
			}
			catch(SQLException e) {
				throw new DaoException("Cannot close resultset", e);
			}
			
			try {
				if(statement != null) {
					statement.close();
				}
			}
			catch(SQLException e) {
				throw new DaoException("Cannot close statement", e);
			}
			
			try {
				if(connection != null) {
					connection.close();
				}
			}
			catch(SQLException e) {
				throw new DaoException("Cannot close connection", e);
			}
		}
		
		return faculty;
	}
	
}
