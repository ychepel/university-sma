package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import university.domain.Course;
import university.domain.Department;
import university.domain.DomainException;

public class CourseDao {
	private DaoFactory daoFactory = new DaoFactory();
	
	public Set<Course> getCourses(Department department) throws DaoException {
		String sql = "SELECT * FROM COURSE WHERE DEPARTMENT_ID=?";
		
		Set<Course> set = new HashSet<>(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Integer departmentId = department.getId();

		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, departmentId);
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				Course course = new Course(resultSet.getString("COURSE_NAME"));
				course.setId(resultSet.getInt("COURSE_ID"));
				course.setGrade(resultSet.getInt("GRADE"));

				set.add(course);
			}
		}
		catch (DomainException ignore) {/*NOP*/}
		catch (SQLException e) {
			throw new DaoException("Cannot get Course data", e);
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
	
	public Course getCourseById(Integer id) throws DaoException {
		String sql = "SELECT * FROM COURSE WHERE COURSE_ID=?";
		
		Course result = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			result = new Course(resultSet.getString("COURSE_NAME"));
			result.setGrade(resultSet.getInt("GRADE"));
			result.setId(id);
			
		}
		catch (DomainException ignore) {/*NOP*/}
		catch (SQLException e) {
			throw new DaoException("Cannot get Course data", e);
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
	
	public Course createCourse(Course course, Department department) throws DaoException {
		Integer departmentId = department.getId();
		String name = course.getName();
		Integer grade = course.getGrade();
		String sql = "INSERT INTO COURSE (COURSE_NAME, GRADE, DEPARTMENT_ID) VALUES ("
				+ "'" + name + "', " + grade + ", " + departmentId + ")";
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			resultSet = statement.getGeneratedKeys();
			resultSet.next();
			Integer courseId = resultSet.getInt(1);
			course.setId(courseId);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot create Course data", e);
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
		
		return course;
	}
	
	public void updateCourse(Course course) throws DaoException {
		
		String sql = "UPDATE COURSE SET "
				+ "COURSE_NAME=?, "
				+ "GRADE=? "
				+ "WHERE COURSE_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Integer courseId = course.getId();
		String name = course.getName();
		Integer grade = course.getGrade();
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, name);
			statement.setInt(2, grade);
			statement.setInt(3, courseId);
			
			statement.executeUpdate();

		}
		catch (SQLException e) {
			throw new DaoException("Cannot update Course data", e);
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
	
	public void dropCourseById(Integer id) throws DaoException {
		String sql = "DELETE FROM COURSE WHERE COURSE_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			throw new DaoException("Cannot delete Course data", e);
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