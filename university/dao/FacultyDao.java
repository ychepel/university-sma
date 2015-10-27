package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import university.domain.Faculty;
import university.domain.Department;
import university.domain.StudentGroup;

public class FacultyDao {

	private DaoFactory daoFactory = new DaoFactory();
	private StudentGroupDao studentGroupDao = new StudentGroupDao();
	private DepartmentDao departmentDao = new DepartmentDao(); 
	
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
				Faculty faculty = new Faculty(resultSet.getString("FACULTY_NAME"));
				faculty.setId(resultSet.getInt("FACULTY_ID"));
				
				Set<StudentGroup> studentGroups = studentGroupDao.getStudentGroups(faculty);
				faculty.setStudentGroups(studentGroups);
				Set<Department> departments = departmentDao.getDepartments(faculty);
				faculty.setDepartments(departments);

				set.add(faculty);
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
		
		return set;
	}
	
	public Faculty getFacultyById(Integer id) throws DaoException {
		String sql = "SELECT * FROM FACULTY WHERE FACULTY_ID=?";
		
		Faculty result = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			result = new Faculty(resultSet.getString("FACULTY_NAME"));
			result.setId(id);
			
			Set<StudentGroup> studentGroups = studentGroupDao.getStudentGroups(result);
			result.setStudentGroups(studentGroups);
			Set<Department> departments = departmentDao.getDepartments(result);
			result.setDepartments(departments);
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
		
		return result;
	}
	
	public Faculty createFaculty(Faculty faculty) throws DaoException {
		String name = faculty.getName();
		String sql = "INSERT INTO FACULTY (FACULTY_NAME) VALUES ('" + name + "')";
		
		Faculty result = null; 
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
			
			result = new Faculty(name);
			result.setId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot create Faculty data", e);
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
	
	public void dropFacultyById(Integer id) throws DaoException {
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
			throw new DaoException("Cannot delete Faculty data", e);
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