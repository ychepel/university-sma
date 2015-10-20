package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import university.domain.Department;

public class DepartmentDao {

	private DaoFactory daoFactory = new DaoFactory();
	
	public Set<Department> getDepartments() throws DaoException {
		String sql = "SELECT * FROM DEPARTMENT";
		
		Set<Department> set = new HashSet<>(); 
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			while(resultSet.next()) {
				Department element = new Department(resultSet.getString("NAME"));
				element.setId(resultSet.getInt("ID"));
				
				set.add(element);
				
			}
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get Department data", e);
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
	
	public Department getById(Integer id) throws DaoException {
		String sql = "SELECT * FROM DEPARTMENT WHERE ID=?";
		
		Department result = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			result = new Department(resultSet.getString("NAME"));
			result.setId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get Department data", e);
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
	
	public Department updateDepartment(Integer id, String name) throws DaoException {
		String sql = "UPDATE DEPARTMENT SET NAME=? WHERE ID=?";
		
		Department result = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, name);
			statement.setInt(2, id);
			statement.executeUpdate();
			
			result = new Department(name);
			result.setId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot update Department data", e);
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
		
		return result;
	}
	
	public Department createDepartment(String name) throws DaoException {
		String sql = "INSERT INTO DEPARTMENT (NAME) VALUES ('" + name + "')";
		
		Department result = null; 
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
			
			result = new Department(name);
			result.setId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot create Department data", e);
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
	
	public void dropDepartment(Integer id) throws DaoException {
		String sql = "DELETE FROM DEPARTMENT WHERE ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
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