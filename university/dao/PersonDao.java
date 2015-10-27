package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import university.domain.Address;
import university.domain.Person;

public class PersonDao {
	private DaoFactory daoFactory = new DaoFactory();
	private AddressDao addressDao = new AddressDao();
	
	protected Person createPerson(Person person) throws DaoException {
		String firstName = person.getFirstName();
		String lastName  =person.getLastName();
		String patronymicName = person.getPatronymicName();
		Date birthDate = person.getBirthDate();
		char gender = person.getGender();
		String passport = person.getPassport();
		String nationality = person.getNationality();
		
		Address address = person.getAddress();
		addressDao.createAddress(address);
		Long addressId = address.getId();
	
		String sql = "INSERT INTO PERSON (FIRST_NAME, LAST_NAME, PATRONYMIC_NAME, BIRTH_DATE, GENDER, PASSPORT, NATIONALITY, ADDRESS_ID)"
				+ "VALUES ('" + firstName + "', '" + lastName + "', '" + patronymicName + "', '" + birthDate + "', "
						+ "'" + gender + "', '" + passport + "', '" + nationality + "', " + addressId+ ")";
		
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
			person.setPersonId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot create Person", e);
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
		
		return person;
	}

	protected void dropPersonById(Long id) throws DaoException {
		String sql = "DELETE FROM PERSON WHERE PERSON_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		Long addressId = getAddressIdByPersonId(id);
		addressDao.dropAddressById(addressId);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			throw new DaoException("Cannot delete Person data", e);
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
	
	protected Long getAddressIdByPersonId(Long personId) throws DaoException {
		String sql = "SELECT ADDRESS_ID FROM PERSON WHERE PERSON_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Long result = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, personId);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			result = resultSet.getLong("ADDRESS_ID");
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get Person data", e);
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

	protected Person getPersonById(Long id, Person cleanPerson) throws DaoException {
		String sql = "SELECT * FROM PERSON WHERE PERSON_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			cleanPerson.setFirstName(resultSet.getString("FIRST_NAME").trim());
			cleanPerson.setLastName(resultSet.getString("LAST_NAME").trim());
			cleanPerson.setPatronymicName(resultSet.getString("PATRONYMIC_NAME").trim());
			cleanPerson.setNationality(resultSet.getString("NATIONALITY"));
			cleanPerson.setPassport(resultSet.getString("PASSPORT"));
			cleanPerson.setBirthDate(resultSet.getDate("BIRTH_DATE"));
			cleanPerson.setGender(resultSet.getString("GENDER").charAt(0));
			cleanPerson.setPersonId(id);
			
			Long addressId = resultSet.getLong("ADDRESS_ID");
			Address address = addressDao.getAddressById(addressId);
			cleanPerson.setAddress(address);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get Person data", e);
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
		
		return cleanPerson;
	}
}
