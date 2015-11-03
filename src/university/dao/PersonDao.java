package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;

import university.domain.Address;
import university.domain.Person;

public class PersonDao {
	private DaoFactory daoFactory = new DaoFactory();
	private AddressDao addressDao = new AddressDao();
	
	private static Logger log = Logger.getLogger(PersonDao.class);
	
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
				+ " VALUES ('" + firstName + "', '" + lastName + "', '" + patronymicName + "', '" + birthDate + "', "
						+ "'" + gender + "', '" + passport + "', '" + nationality + "', " + addressId+ ")";
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
			log.warn("New Person Id=" + id);
			person.setPersonId(id);
		}
		catch (SQLException e) {
			log.error("Cannot create Person", e);
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
	
	protected void updatePerson(Person person) throws DaoException {
		String sql = "UPDATE PERSON SET"
				+ " FIRST_NAME=?,"
				+ " LAST_NAME=?,"
				+ " PATRONYMIC_NAME=?,"
				+ " BIRTH_DATE=?,"
				+ " GENDER=?,"
				+ " PASSPORT=?,"
				+ " NATIONALITY=?"
				+ " WHERE PERSON_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		String firstName = person.getFirstName();
		String lastName  =person.getLastName();
		String patronymicName = person.getPatronymicName();
		Date birthDate = person.getBirthDate();
		char gender = person.getGender();
		String passport = person.getPassport();
		String nationality = person.getNationality();
		Long personId = person.getPersonId();
		log.debug("Updating Person with person.id=" + personId + "; birthDate=" + birthDate);
		
		Address address = person.getAddress();
		log.warn("Updating Person Address=" + address + "; Address.id=" + address.getId());
		if(address != null) {
			log.debug("Updating Person with address.id=" + address.getId() + " and full address=" + address.getFullAdress());
			addressDao.updateAddress(address);
		}
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			statement.setString(3, patronymicName);
			statement.setDate(4, new java.sql.Date(birthDate.getTime()));
			statement.setString(5, String.valueOf(gender));
			statement.setString(6, passport);
			statement.setString(7, nationality);
			statement.setLong(8, personId);
			
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot update Person", e);
			throw new DaoException("Cannot update Person", e);
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

	protected void deletePersonById(Long id) throws DaoException {
		String sql = "DELETE FROM PERSON WHERE PERSON_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		Long addressId = getAddressIdByPersonId(id);
		addressDao.deleteAddressById(addressId);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot delete Person", e);
			throw new DaoException("Cannot delete Person", e);
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
		log.debug("Selecting Address.Id for Person with id=" + personId);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, personId);
			resultSet = statement.executeQuery();
			resultSet.next();
			result = resultSet.getLong("ADDRESS_ID");
			log.debug("Person Address.id=" + result);
		}
		catch (SQLException e) {
			log.error("Cannot get Address Id", e);
			throw new DaoException("Cannot get Address Id", e);
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
		log.warn("Select `" + cleanPerson.getClass().getSimpleName() + "` with id=" + id);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			String firstName = resultSet.getString("FIRST_NAME").trim();
			String lastName = resultSet.getString("LAST_NAME").trim();
			String patronymicName = resultSet.getString("PATRONYMIC_NAME").trim();
			String nationality = resultSet.getString("NATIONALITY");
			String passport = resultSet.getString("PASSPORT");
			Date birthDate = resultSet.getDate("BIRTH_DATE");
			char gender = resultSet.getString("GENDER").charAt(0);
			log.debug("Person Last name=" + lastName + "; birthDate=" + birthDate);
			
			cleanPerson.setFirstName(firstName);
			cleanPerson.setLastName(lastName);
			cleanPerson.setPatronymicName(patronymicName);
			cleanPerson.setNationality(nationality);
			cleanPerson.setPassport(passport);
			cleanPerson.setBirthDate(birthDate);
			cleanPerson.setGender(gender);
			cleanPerson.setPersonId(id);
			
			Long addressId = resultSet.getLong("ADDRESS_ID");
			log.warn("Person Address.id=" +  addressId);
			Address address = addressDao.getAddressById(addressId);
			log.debug("Get Person with address.id=" + address.getId() + " and full address" + address.getFullAdress());
			cleanPerson.setAddress(address);
		}
		catch (SQLException e) {
			log.error("Cannot get Person", e);
			throw new DaoException("Cannot get Person", e);
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
