package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import university.domain.Address;

public class AddressDao {
	private ConnectionFactory daoFactory = new ConnectionFactory();
	
	private static Logger log = Logger.getLogger(AddressDao.class);
	
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
			Long id = resultSet.getLong(1);
			address.setId(id);
			log.warn("New Adress Id=" + id);
		} 
		catch (SQLException e) {
			log.error("Cannot create Address", e);
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
	
	public void updateAddress(Address address) throws DaoException {
		String sql = "UPDATE ADDRESS"
				+ " SET CITY=?, "
				+ " EMAIL=?,"
				+ " FLAT=?,"
				+ " HOUSE=?,"
				+ " PHONE=?,"
				+ " PROVINCE=?,"
				+ " STREET=?"
				+ " WHERE ADDRESS_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Long addressId = address.getId();
		String city = address.getCity();
		String email = address.getEmail();
		Integer flat = address.getFlat();
		String house = address.getHouse();
		String phone = address.getPhone();
		String province = address.getProvince();
		String street = address.getStreet();
		
		log.debug("Address update info: addressId=" + addressId + "; city=" + city + "; street=" + street);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, city);
			statement.setString(2, email);
			statement.setInt(3, flat);
			statement.setString(4, house);
			statement.setString(5, phone);
			statement.setString(6, province);
			statement.setString(7, street);
			statement.setLong(8, addressId);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot update Address", e);
			throw new DaoException("Cannot update Address", e);
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

	protected void deleteAddressById(Long id) throws DaoException {
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
			log.error("Cannot delete Address data", e);
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
	
	public Address getAddressById(Long id) throws DaoException {
		String sql = "SELECT * FROM ADDRESS WHERE ADDRESS_ID=?";
		
		Address result = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		log.warn("Getting Address by Id=" + id);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			result = parseResultSet(resultSet);
		}
		catch (SQLException e) {
			log.error("Cannot get Address data by Id", e);
			throw new DaoException("Cannot get Address data by Id", e);
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
	
	private Address parseResultSet(ResultSet resultSet) throws SQLException {
		Address result = new Address();
		
		result.setId(resultSet.getLong("ADDRESS_ID"));
		result.setCity(resultSet.getString("CITY").trim());
		result.setEmail(resultSet.getString("EMAIL").trim());
		result.setFlat(resultSet.getInt("FLAT"));
		result.setHouse(resultSet.getString("HOUSE").trim());
		result.setPhone(resultSet.getString("PHONE").trim());
		result.setProvince(resultSet.getString("PROVINCE").trim());
		result.setStreet(resultSet.getString("STREET").trim());
		
		log.debug("Selected Address: id=" + result.getId() + "; city=" + result.getCity() + "; falt=" + result.getFlat());
		
		return result;
	}
}
