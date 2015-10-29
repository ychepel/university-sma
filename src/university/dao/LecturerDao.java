package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import university.domain.Lecturer;
import university.domain.Person;
import university.domain.Department;

public class LecturerDao {
	private DaoFactory daoFactory = new DaoFactory();
	private PersonDao personDao = new PersonDao();
	
	private static Logger log = Logger.getLogger(LecturerDao.class);
	
	public Set<Lecturer> getLecturers(Department department) throws DaoException {
		String sql = "SELECT * FROM LECTURER WHERE DEPARTMENT_ID=?";
		
		Set<Lecturer> set = new HashSet<>(); 
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
				Long personId = resultSet.getLong("PERSON_ID");
				Integer lecturerId = resultSet.getInt("LECTURER_ID");
				String currentPosition = resultSet.getString("CURRENT_POSITION");
				String scienceDegree = resultSet.getString("SCIENCE_DEGREE");
				log.warn("Get Lecturer with lecturer.id=" + lecturerId + " and person.id=" + personId);
				Lecturer lecturer = (Lecturer) personDao.getPersonById(personId, new Lecturer());
				lecturer.setLecturerId(lecturerId);
				lecturer.setCurrentPosition(currentPosition);
				lecturer.setScienceDegree(scienceDegree);
				set.add(lecturer);
			}
		}
		catch (SQLException e) {
			log.error("Cannot get Lecturers", e);
			throw new DaoException("Cannot get Lecturers", e);
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
		log.warn("Select Lecturer with id=" + id);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			Long personId = resultSet.getLong("PERSON_ID");
			String currentPosition = resultSet.getString("CURRENT_POSITION");
			String scienceDegree = resultSet.getString("SCIENCE_DEGREE");
			log.warn("Lecturer person.id=" + personId);
			lecturer = (Lecturer) personDao.getPersonById(personId, new Lecturer());
			lecturer.setLecturerId(id);
			lecturer.setCurrentPosition(currentPosition);
			lecturer.setScienceDegree(scienceDegree);
		}
		catch (SQLException e) {
			log.error("Cannot get Lecturer", e);
			throw new DaoException("Cannot get Lecturer", e);
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
	
	public Lecturer createLecturer(Lecturer lecturer, Department department) throws DaoException {
		String scienceDegree = lecturer.getScienceDegree();
		String currentPosition = lecturer.getCurrentPosition();
		
		Person person = personDao.createPerson(lecturer);
		Long personId = person.getPersonId();
		Integer departmentId = department.getId();
		
		String sql = "INSERT INTO LECTURER (SCIENCE_DEGREE, CURRENT_POSITION, PERSON_ID, DEPARTMENT_ID) "
				+ "VALUES (" + "'" + scienceDegree + "', '" + currentPosition + "', " 
				+ personId + ", " + departmentId + ")";
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
			log.warn("New Lecturer Id=" + id);
			lecturer.setLecturerId(id);
		}
		catch (SQLException e) {
			log.error("Cannot create Lecturer", e);
			throw new DaoException("Cannot create Lecturer", e);
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
	
	public void updateLecturer(Lecturer lecturer, Department department) throws DaoException {
		String sql = "UPDATE LECTURER SET "
				+ "SCIENCE_DEGREE=?, "
				+ "CURRENT_POSITION=?, "
				+ "DEPARTMENT_ID=? "
				+ "WHERE LECTURER_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		String scienceDegree = lecturer.getScienceDegree();
		String currentPosition = lecturer.getCurrentPosition();
		Integer departmentId = department.getId();
		Integer lecturerId = lecturer.getLecturerId();
		log.debug("Updating Lecturer with id=" + lecturerId + "; departmentId=" + departmentId);
		
		personDao.updatePerson(lecturer);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, scienceDegree);
			statement.setString(2, currentPosition);
			statement.setInt(3, departmentId);
			statement.setInt(4, lecturerId);
			
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot update Lecturer", e);
			throw new DaoException("Cannot update Lecturer", e);
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
			log.error("Cannot delete Lecturer", e);
			throw new DaoException("Cannot delete Lecturer", e);
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
