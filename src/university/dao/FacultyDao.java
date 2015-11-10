package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import university.domain.Faculty;

public class FacultyDao {

	private ConnectionFactory daoFactory = new ConnectionFactory();
	private static Logger log = Logger.getLogger(FacultyDao.class);
	
	public Set<Faculty> getFaculties() throws DaoException {
		String sql = "SELECT * FROM FACULTY";
		
		Set<Faculty> set = new HashSet<>(); 
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			while(resultSet.next()) {
				String facultyName = resultSet.getString("FACULTY_NAME").trim();
				Integer facultyId = resultSet.getInt("FACULTY_ID");
				log.warn("Get Faculty with id=" + facultyId + " and name=" + facultyName);
				Faculty faculty = new Faculty(facultyName);
				faculty.setId(facultyId);
				set.add(faculty);
			}
		}
		catch (SQLException e) {
			log.error("Cannot get Faculties", e);
			throw new DaoException("Cannot get Faculties", e);
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
		return set;
	}
	
	public Faculty getFacultyById(Integer id) throws DaoException {
		String sql = "SELECT * FROM FACULTY WHERE FACULTY_ID=?";
		
		Faculty result = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		log.warn("Select Faculty with id=" + id);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			String facultyName = resultSet.getString("FACULTY_NAME").trim();
			log.debug("Selected Faculty.name=" + facultyName);
			result = new Faculty(facultyName);
			result.setId(id);
		}
		catch (SQLException e) {
			log.error("Cannot get Faculty", e);
			throw new DaoException("Cannot get Faculty", e);
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
		return result;
	}
	
	public Faculty createFaculty(Faculty faculty) throws DaoException {
		String name = faculty.getName();
		String sql = "INSERT INTO FACULTY (FACULTY_NAME) VALUES ('" + name + "')";
		log.debug(sql);
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			resultSet = statement.getGeneratedKeys();
			resultSet.next();
			Integer id = resultSet.getInt(1);
			log.warn("New Faculty Id=" + id);
			faculty.setId(id);
		}
		catch (SQLException e) {
			log.error("Cannot create Faculty", e);
			throw new DaoException("Cannot create Faculty", e);
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
	
	public void updateFaculty(Faculty faculty) throws DaoException {
		String sql = "UPDATE FACULTY SET FACULTY_NAME=? WHERE FACULTY_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Integer facultyId = faculty.getId();
		String name = faculty.getName();
		log.debug("Updating Faculty id=" + facultyId + "; name=" + name);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, name);
			statement.setInt(2, facultyId);
			
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot update Faculty", e);
			throw new DaoException("Cannot update Faculty", e);
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
	}
	
	public void deleteFacultyById(Integer id) throws DaoException {
		String sql = "DELETE FROM FACULTY WHERE FACULTY_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot delete Faculty", e);
			throw new DaoException("Cannot delete Faculty", e);
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
	}
	
}