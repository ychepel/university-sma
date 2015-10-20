package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;


public abstract class DaoAbstract {

	private DaoFactory daoFactory = new DaoFactory();
	
	protected abstract String getSelectAllQuery();
	protected abstract <T> Set<T> parseAllResultSet(ResultSet resultSet) throws SQLException;
	
	public <T> Set<T> getAll() throws DaoException {
		String sql = getSelectAllQuery();
		
		Set<T> set = new HashSet<>(); 
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			set = parseAllResultSet(resultSet);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot select all records", e);
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
	
	protected abstract String getSelectByIdQuery();
	protected abstract <T> T parseOneResultSet(ResultSet resultSet) throws SQLException;
	
	public <T> T getById(Integer id) throws DaoException {
		String sql = getSelectByIdQuery();
		
		T element = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			
			element = parseOneResultSet(resultSet);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get record by id", e);
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
		
		return element;
	}
	
	protected abstract <T> String getInsertQuery(T preparedElement);
	protected abstract <T> T makeElement(T preparedElement, ResultSet resultSet) throws SQLException;
	
	public <T> T createRecord(T preparedElement) throws DaoException {
		String sql = getInsertQuery(preparedElement);
		
		T element = null; 
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			resultSet = statement.getGeneratedKeys();
			
			element = makeElement(preparedElement, resultSet);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot create record", e);
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
		
		return element;
	}
	
	protected abstract String getDeleteQuery();
	
	public void dropById(Integer id) throws DaoException {
		String sql = getDeleteQuery();
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			throw new DaoException("Cannot delete the record by id", e);
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
