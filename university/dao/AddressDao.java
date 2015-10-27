package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import university.domain.Address;

public class AddressDao {
	private DaoFactory daoFactory = new DaoFactory();
	
	protected Address createAddress(Address address) throws DaoException {
		String city = address.getCity();
		String email = address.getEmail();
		Integer flat = address.getFlat();
		String house = address.getHouse();
		String phone = address.getPhone();
		String province = address.getProvince();
		String street = address.getStreet();
		
		String sql = "INSERT INTO ADDRESS (CITY, EMAIL, FLAT, HOUSE, PHONE, PROVINCE, STREET) VALUES "
				+ "('" + city + "', '" + email + "', " + flat + ", '" + house + "', '" + phone + "', "
						+ "'" + province + "', '" + street + "')";
		
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			resultSet = statement.getGeneratedKeys();
			resultSet.next();
			Long id = resultSet.getLong(1);
			address.setId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot create Address", e);
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
		
		return address;
	}

	protected void dropAddressById(Long id) throws DaoException {
		String sql = "DELETE FROM ADDRESS WHERE ADDRESS_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			throw new DaoException("Cannot delete Address data", e);
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
	
	protected Address getAddressById(Long id) throws DaoException {
		String sql = "SELECT * FROM ADDRESS WHERE ADDRESS_ID=?";
		
		Address result = new Address(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			result.setCity(resultSet.getString("CITY"));
			result.setEmail(resultSet.getString("EMAIL"));
			result.setFlat(resultSet.getInt("FLAT"));
			result.setHouse(resultSet.getString("HOUSE"));
			result.setPhone(resultSet.getString("PHONE"));
			result.setProvince(resultSet.getString("PROVINCE"));
			result.setStreet(resultSet.getString("STREET"));
			result.setId(id);
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
}
