package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import university.domain.StudentGroup;
import university.domain.Faculty;

public class StudentGroupDao {

	private DaoFactory daoFactory = new DaoFactory();
	
	public Set<StudentGroup> getStudentGroups(Faculty faculty) throws DaoException {
		String sql = "SELECT * FROM STUDENT_GROUP WHERE FACULTY_ID=?";
		
		Set<StudentGroup> set = new HashSet<>(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Integer facultyId = faculty.getId();

		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, facultyId);
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				StudentGroup studentGroup = new StudentGroup(resultSet.getString("STUDENT_GROUP_NAME"));
				studentGroup.setId(resultSet.getInt("STUDENT_GROUP_ID"));
				
				set.add(studentGroup);
			}
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get Student Groups data", e);
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
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			result = new StudentGroup(resultSet.getString("STUDENT_GROUP_NAME"));
			result.setId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get Student Group data", e);
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
		
		StudentGroup result = null; 
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
			
			result = new StudentGroup(name);
			result.setId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot create Student Group data", e);
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
		
		return result;
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
			throw new DaoException("Cannot delete Student Group data", e);
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
