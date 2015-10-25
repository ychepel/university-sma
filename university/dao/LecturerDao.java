package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import university.domain.Lecturer;
import university.domain.Person;

public class LecturerDao {
	private DaoFactory daoFactory = new DaoFactory();
	private PersonDao personDao = new PersonDao();
	
	public Set<Lecturer> getLecturers() throws DaoException {
		String sql = "SELECT * FROM LECTURER";
		
		Set<Lecturer> set = new HashSet<>(); 
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			while(resultSet.next()) {
				Long personId = resultSet.getLong("PERSONAL_ID");
				Lecturer lecturer = (Lecturer) personDao.getPersonById(personId, new Lecturer());
				lecturer.setLecturerId(resultSet.getInt("LECTURER_ID"));
				lecturer.setCurrentPosition(resultSet.getString("CURRENT_POSITION"));
				lecturer.setScienceDegree(resultSet.getString("SCIENCE_DEGREE"));

				set.add(lecturer);
			}
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get Lecturer data", e);
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
	
	public Lecturer getLecturerById(Integer id) throws DaoException {
		String sql = "SELECT * FROM LECTURER WHERE LECTURER_ID=?";
		
		Lecturer lecturer = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			Long personId = resultSet.getLong("PERSONAL_ID");
			lecturer = (Lecturer) personDao.getPersonById(personId, new Lecturer());
			lecturer.setLecturerId(id);
			lecturer.setCurrentPosition(resultSet.getString("CURRENT_POSITION"));
			lecturer.setScienceDegree(resultSet.getString("SCIENCE_DEGREE"));
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get Lecturer data", e);
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
		
		return lecturer;
	}
	
	public Lecturer createLecturer(Lecturer lecturer) throws DaoException {
		String scienceDegree = lecturer.getScienceDegree();
		String currentPosition = lecturer.getCurrentPosition();
		
		Person person = personDao.createPerson(lecturer);
		Long personId = person.getPersonId();
		
		String sql = "INSERT INTO LECTURER (SCIENCE_DEGREE, CURRENT_POSITION, PERSON_ID) VALUES ("
				+ "'" + scienceDegree + "', '" + currentPosition + "', " + personId + ")";
		
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
			lecturer.setLecturerId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot create Lecturer data", e);
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
		
		return lecturer;
	}
	
	public void dropLecturerById(Integer id) throws DaoException {
		String sql = "DELETE FROM LECTURER WHERE LECTURER_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		Lecturer lecturer = getLecturerById(id);
		Long personId = lecturer.getPersonId();
		personDao.dropPersonById(personId);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			throw new DaoException("Cannot delete Lecturer data", e);
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
