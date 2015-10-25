package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import university.domain.Student;
import university.domain.StudentGroup;
import university.domain.Person;

public class StudentDao {
	private DaoFactory daoFactory = new DaoFactory();
	private PersonDao personDao = new PersonDao();
	private StudentGroupDao studentGroupDao = new StudentGroupDao();

	public Set<Student> getStudents() throws DaoException {
		String sql = "SELECT * FROM STUDENT";
		
		Set<Student> set = new HashSet<>(); 
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			while(resultSet.next()) {
				Long personId = resultSet.getLong("PERSON_ID");
				Student student = new Student(resultSet.getLong("STUDENT_ID")); 
				student = (Student) personDao.getPersonById(personId, student);
				student.setGovernmentFinanced(resultSet.getBoolean("GOVERNMENT_FINANCED"));
				student.setSchoolGraduateSertificate(resultSet.getString("SCHOOL_CERTFICATE"));
				
				Integer studentGroupId = resultSet.getInt("STUDENT_GROUP_ID");
				StudentGroup studentGroup = studentGroupDao.getStudentGroupById(studentGroupId);
				student.setStudentGroup(studentGroup);
				
				Date entranceDate = resultSet.getDate("ENTRANCE_DATE");
				Calendar entranceCalendar = new GregorianCalendar();
				entranceCalendar.setTime(entranceDate);
				student.setEntranceDate(entranceCalendar);
				
				Date completionDate = resultSet.getDate("COMPLETION_DATE");
				Calendar completionCalendar = new GregorianCalendar();
				completionCalendar.setTime(completionDate);
				student.setCompletionDate(completionCalendar);
				
				set.add(student);
			}
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get Student data", e);
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
	
	public Student getStudentById(Long id) throws DaoException {
		String sql = "SELECT * FROM STUDENT WHERE STUDENT_ID=?";
		
		Student student = new Student(id); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			
			resultSet.next();
			
			Long personId = resultSet.getLong("PERSON_ID");
			student = (Student) personDao.getPersonById(personId, student);
			student.setGovernmentFinanced(resultSet.getBoolean("GOVERNMENT_FINANCED"));
			student.setSchoolGraduateSertificate(resultSet.getString("SCHOOL_CERTFICATE"));
			
			Integer studentGroupId = resultSet.getInt("STUDENT_GROUP_ID");
			StudentGroup studentGroup = studentGroupDao.getStudentGroupById(studentGroupId);
			student.setStudentGroup(studentGroup);
			
			Date entranceDate = resultSet.getDate("ENTRANCE_DATE");
			Calendar entranceCalendar = new GregorianCalendar();
			entranceCalendar.setTime(entranceDate);
			student.setEntranceDate(entranceCalendar);
			
			Date completionDate = resultSet.getDate("COMPLETION_DATE");
			Calendar completionCalendar = new GregorianCalendar();
			completionCalendar.setTime(completionDate);
			student.setCompletionDate(completionCalendar);
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
		
		return student;
	}
	
	public void createStudent(Student student) throws DaoException {
		String sql = "INSERT INTO LECTURER (STUDENT_ID, GOVERNMENT_FINANCED, SCHOOL_CERTFICATE, STUDENT_GROUP_ID, "
				+ "ENTRANCE_DATE, COMPLETION_DATE, PERSON_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		Person person = personDao.createPerson(student);
		Long personId = person.getPersonId();
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			
			statement.setLong(1, student.getStudentId());
			statement.setBoolean(2, student.getGovernmentFinanced());
			statement.setString(3, student.getSchoolGraduateSertificate());
			StudentGroup studentGroup = student.getStudentGroup();
			statement.setInt(4, studentGroup.getId());
			Calendar entranceCalendar = student.getEntranceDate();
			statement.setDate(5, (java.sql.Date) entranceCalendar.getTime());
			Calendar completionCalendar = student.getCompletionDate();
			statement.setDate(6, (java.sql.Date) completionCalendar.getTime());
			statement.setLong(7, personId);
			
			resultSet = statement.executeQuery();
		}
		catch (SQLException e) {
			throw new DaoException("Cannot create Student", e);
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
	
	public void dropStudentById(Long id) throws DaoException {
		String sql = "DELETE FROM STUDENT WHERE STUDENT_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		Student student = getStudentById(id);
		Long personId = student.getPersonId();
		personDao.dropPersonById(personId);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			throw new DaoException("Cannot delete Student data", e);
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
