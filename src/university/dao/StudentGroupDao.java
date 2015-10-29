package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import university.domain.StudentGroup;
import university.domain.Faculty;

public class StudentGroupDao {

	private DaoFactory daoFactory = new DaoFactory();
	
	private static Logger log = Logger.getLogger(StudentGroupDao.class);
	
	public Set<StudentGroup> getStudentGroups(Faculty faculty) throws DaoException {
		String sql = "SELECT * FROM STUDENT_GROUP WHERE FACULTY_ID=?";
		
		Set<StudentGroup> set = new HashSet<>(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Integer facultyId = faculty.getId();
		log.debug("Get Student Groups for faculty.id=" + facultyId + "; name=" + faculty.getName());

		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, facultyId);
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				String studentGroupName = resultSet.getString("STUDENT_GROUP_NAME");
				Integer studentGroupId = resultSet.getInt("STUDENT_GROUP_ID");
				log.debug("Student Group id=" + studentGroupId + "; name=" + studentGroupName);
				StudentGroup studentGroup = new StudentGroup(studentGroupName);
				studentGroup.setId(studentGroupId);
				set.add(studentGroup);
			}
		}
		catch (SQLException e) {
			log.error("Cannot get Student Groups", e);;
			throw new DaoException("Cannot get Student Groups", e);
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
	
	public StudentGroup getStudentGroupById(Integer id) throws DaoException {
		String sql = "SELECT * FROM STUDENT_GROUP WHERE STUDENT_GROUP_ID=?";
		
		StudentGroup result = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		log.debug("Get StudentGroup with id=" + id);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			
			if(resultSet.next()) {
				String studentGroupName = resultSet.getString("STUDENT_GROUP_NAME");
				log.debug("Selected StudentGroup.name=" + studentGroupName);
				result = new StudentGroup(studentGroupName);
				result.setId(id);
			}
		}
		catch (SQLException e) {
			log.error("Cannot get Student Group by Id", e);
			throw new DaoException("Cannot get Student Group by Id", e);
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
	
	public StudentGroup createStudentGroup(StudentGroup studentGroup, Faculty faculty) throws DaoException {
		Integer facultyId = faculty.getId();
		String name = studentGroup.getName();
		String sql = "INSERT INTO STUDENT_GROUP (STUDENT_GROUP_NAME, FACULTY_ID) "
				+ "VALUES ('" + name + "', " + facultyId + ")";
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
			log.warn("New StudentGroup Id=" + id);
			studentGroup.setId(id);
		}
		catch (SQLException e) {
			log.error("Cannot create Student Group", e);
			throw new DaoException("Cannot create Student Group", e);
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
		
		return studentGroup;
	}
	
	public void updateStudentGroup(StudentGroup studentGroup, Faculty faculty) throws DaoException {
		String sql = "UPDATE STUDENT_GROUP SET STUDENT_GROUP_NAME=?, FACULTY_ID=? WHERE STUDENT_GROUP_ID=?";

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Integer studentGroupId = studentGroup.getId();
		String name = studentGroup.getName();
		Integer facultyId = faculty.getId();
		log.debug("Updating StudentGroup id=" + studentGroupId + "; name=" + name + " for Faculty with id=" + facultyId);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setInt(2, facultyId);
			statement.setInt(3, studentGroupId);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot update Student Group", e);
			throw new DaoException("Cannot update Student Group", e);
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
	
	public void dropStudentGroupById(Integer id) throws DaoException {
		String sql = "DELETE FROM STUDENT_GROUP WHERE STUDENT_GROUP_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot delete Student Group", e);
			throw new DaoException("Cannot delete Student Group", e);
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
