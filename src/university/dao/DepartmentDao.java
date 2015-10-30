package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import university.domain.Department;
import university.domain.Faculty;

public class DepartmentDao {

	private DaoFactory daoFactory = new DaoFactory();
	
	private static Logger log = Logger.getLogger(DepartmentDao.class);
	
	public Set<Department> getDepartments(Faculty faculty) throws DaoException {
		String sql = "SELECT * FROM DEPARTMENT WHERE FACULTY_ID=?";
		
		Set<Department> set = new HashSet<>(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Integer facultyId = faculty.getId();
		log.debug("Get Department for Fculty.id=" + facultyId);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, facultyId);
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				String departmentName = resultSet.getString("DEPARTMENT_NAME");
				Integer departmentId = resultSet.getInt("DEPARTMENT_ID");
				log.warn("Create Department name=" + departmentName + " with id=" + departmentId);
				Department department = new Department(departmentName);
				department.setId(departmentId);
				set.add(department);
			}
		}
		catch (SQLException e) {
			log.error("Cannot get Departments for Faculty", e);
			throw new DaoException("Cannot get Departments for Faculty", e);
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
	
	public Department getDepartmentById(Integer id) throws DaoException {
		String sql = "SELECT * FROM DEPARTMENT WHERE DEPARTMENT_ID=?";
		
		Department result = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		log.debug("Get Dpartment with id=" + id);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			String departmentName = resultSet.getString("DEPARTMENT_NAME");
			log.debug("Selected Department.name=" + departmentName);
			result = new Department(departmentName);
			result.setId(id);
		}
		catch (SQLException e) {
			log.error("Cannot get Department by Id", e);
			throw new DaoException("Cannot get Department by Id", e);
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
	
	public Department createDepartment(Department department, Faculty faculty) throws DaoException {
		Integer facultyId = faculty.getId();
		String name = department.getName();
		String sql = "INSERT INTO DEPARTMENT (DEPARTMENT_NAME, FACULTY_ID) VALUES ('" + name + "', " + facultyId + ")";
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
			log.warn("New Department Id=" + id);
			department.setId(id);
		}
		catch (SQLException e) {
			log.error("Cannot create Department", e);
			throw new DaoException("Cannot create Department", e);
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
		return department;
	}
	
	public void updateDepartment(Department department) throws DaoException {
		String sql = "UPDATE DEPARTMENT SET DEPARTMENT_NAME=? WHERE DEPARTMENT_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Integer departmentId = department.getId();
		String name = department.getName();
		log.debug("Updating Department id=" + departmentId + "; name=" + name);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setInt(2, departmentId);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot update Department", e);
			throw new DaoException("Cannot update Department", e);
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
	
	public void dropDepartmentById(Integer id) throws DaoException {
		String sql = "DELETE FROM DEPARTMENT WHERE DEPARTMENT_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot delete Department data", e);
			throw new DaoException("Cannot delete Department data", e);
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