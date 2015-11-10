package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import university.domain.DomainException;
import university.domain.Student;
import university.domain.StudentGroup;
import university.domain.Person;
import university.domain.Course;

public class StudentDao {
	private ConnectionFactory daoFactory = new ConnectionFactory();
	private PersonDao personDao = new PersonDao();
	private StudentGroupDao studentGroupDao = new StudentGroupDao();
	private CourseDao courseDao = new CourseDao();

	private static Logger log = Logger.getLogger(StudentDao.class);
	
	private Student parseResultSet(ResultSet resultSet) throws SQLException, DaoException, DomainException {
		Long studentId = resultSet.getLong("STUDENT_ID");
		log.warn("Student id=" + studentId);
		Student student = new Student(studentId);
		
		Map<Course, Integer> marks = getStudentMarks(studentId);
		student.setMarks(marks);
		
		Long personId = resultSet.getLong("PERSON_ID");
		log.warn("Student person.id=" + personId);
		
		student = (Student) personDao.getPersonById(personId, student);
		student.setGovernmentFinanced(resultSet.getBoolean("GOVERNMENT_FINANCED"));
		student.setSchoolGraduateSertificate(resultSet.getString("SCHOOL_CERTIFICATE"));
		
		Integer studentGroupId = resultSet.getInt("STUDENT_GROUP_ID");
		log.warn("Student StudentGroup.id=" + studentGroupId);
		StudentGroup studentGroup = studentGroupDao.getStudentGroupById(studentGroupId);
		log.debug("Student StudentGroup=" + studentGroup);
		student.setStudentGroup(studentGroup);
		
		SimpleDateFormat formatCalendar = new SimpleDateFormat("yyyy-MM-dd"); 
		
		Date entranceDate = resultSet.getDate("ENTRANCE_DATE");
		Calendar entranceCalendar = new GregorianCalendar();
		entranceCalendar.setTime(entranceDate);
		log.debug("Student Entrance Date=" + formatCalendar.format(entranceDate));
		student.setEntranceDate(entranceCalendar);

		Date completionDate = resultSet.getDate("COMPLETION_DATE");
		Calendar completionCalendar = new GregorianCalendar();
		completionCalendar.setTime(completionDate);
		log.debug("Student Completion Date=" + formatCalendar.format(completionDate));
		student.setCompletionDate(completionCalendar);
		
		return student;
	}
	
	private Map<String, Object> parseStudent(Student student) {
		Map<String, Object> result = new HashMap<>();
		
		Boolean governmentFinanced = student.getGovernmentFinanced();
		result.put("governmentFinanced", governmentFinanced);
		
		String schoolGraduateSertificate = student.getSchoolGraduateSertificate();
		result.put("schoolGraduateSertificate", schoolGraduateSertificate);
		
		StudentGroup studentGroup = student.getStudentGroup();
		Integer studentGroupId;
		if(studentGroup == null) {
			studentGroupId = 0;
		}
		else {
			studentGroupId = studentGroup.getId();
		}
		result.put("studentGroupId", studentGroupId);
		
		SimpleDateFormat formatCalendar = new SimpleDateFormat("yyyy-MM-dd");   
		Calendar entranceCalendar = student.getEntranceDate();
		java.sql.Date entranceDate = new java.sql.Date(entranceCalendar.getTimeInMillis());
		result.put("entranceDate", entranceDate);
		
		Calendar completionCalendar = student.getCompletionDate();
		java.sql.Date completioDate = new java.sql.Date(completionCalendar.getTimeInMillis());
		result.put("completioDate", completioDate);
		
		log.debug("Parse Student: studentGroupId=" + studentGroupId 
				+ "; entranceCalendar=" + formatCalendar.format(entranceDate)
				+ "; completioDate=" + formatCalendar.format(completioDate));
		
		return result;
	}
	
	private Map<Course, Integer> getStudentMarks(Long studentId) throws DaoException {
		String sql = "SELECT * FROM STUDENT_MARK WHERE STUDENT_ID = ?";
		log.debug("Selecting Student Marks for Student.id=" + studentId);
		Map<Course, Integer> map = new HashMap<>(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, studentId);
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				Integer courseId = resultSet.getInt("COURSE_ID");
				log.warn("Selected Course.id=" + courseId);
				Course course = courseDao.getCourseById(courseId);
				log.warn("Selected Course=" + course);
				Integer mark = resultSet.getInt("MARK");
				log.debug("Student mark: " + course.getName() + " = " + mark);
				map.put(course, mark);
			}
		}
		catch (SQLException e) {
			log.error("Cannot get Student Marks", e);
			throw new DaoException("Cannot get Student Marks", e);
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
		
		return map;
	}
	
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
				Student student = parseResultSet(resultSet);
				set.add(student);
			}
		}
		catch (DomainException ignore) {/*NOP*/}
		catch (SQLException e) {
			log.error("Cannot get Students", e);
			throw new DaoException("Cannot get Students", e);
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
	
	public Set<Student> getStudentsByGroup(StudentGroup studentGroup) throws DaoException {
		String sql = "SELECT * FROM STUDENT WHERE STUDENT_GROUP_ID=?";
		
		Set<Student> set = new HashSet<>(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Integer studentGroupId = studentGroup.getId();
		log.warn("Get Students for StudentGroup with id=" + studentGroupId);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, studentGroupId);
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				Student student = parseResultSet(resultSet);
				set.add(student);
			}
		}
		catch (DomainException ignore) {/*NOP*/}
		catch (SQLException e) {
			log.error("Cannot get Students for StudentGroup", e);
			throw new DaoException("Cannot get Students for StudentGroup", e);
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
		
		Student student = null; 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		log.warn("Select Student with id=" + id);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			resultSet = statement.executeQuery();
			resultSet.next();
			student = parseResultSet(resultSet);
		}
		catch (DomainException ignore) {/*NOP*/}
		catch (SQLException e) {
			log.error("Cannot get Student by Id", e);
			throw new DaoException("Cannot get Student by Id", e);
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
		String sql = "INSERT INTO STUDENT (STUDENT_ID, GOVERNMENT_FINANCED, SCHOOL_CERTIFICATE, STUDENT_GROUP_ID, "
				+ "ENTRANCE_DATE, COMPLETION_DATE, PERSON_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Person person = personDao.createPerson(student);
		Long personId = person.getPersonId();
		Long studentId = student.getStudentId();
		log.warn("Create Student with id=" + studentId + " and person.id=" + personId);
		
		Map<String, Object> sqlParameters = parseStudent(student);
		createStudentMark(student);		
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			
			statement.setLong(1, studentId);
			statement.setBoolean(2, (Boolean) sqlParameters.get("governmentFinanced"));
			statement.setString(3, (String) sqlParameters.get("schoolGraduateSertificate"));
			statement.setInt(4, (Integer) sqlParameters.get("studentGroupId"));
			statement.setDate(5, (java.sql.Date) sqlParameters.get("entranceDate"));
			statement.setDate(6, (java.sql.Date) sqlParameters.get("completioDate"));
			statement.setLong(7, personId);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot create Student", e);
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
	
	private void createStudentMark(Student student) throws DaoException {
		String sql = "INSERT INTO STUDENT_MARK (STUDENT_ID, COURSE_ID, MARK) VALUES (?, ?, ?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Long studentId = student.getStudentId();
		Map<Course, Integer> marks = student.getMarks();
		log.warn("Create Student Mark for Student.id=" + studentId);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, studentId);
			
			for(Map.Entry<Course, Integer> entry : marks.entrySet()) {
				Course course = entry.getKey();
				Integer courseId = course.getId();
				Integer mark = entry.getValue();
				log.debug("Create Student Mark: courseId=" + courseId + "; mark=" + mark);
				statement.setInt(2, courseId);
				statement.setInt(3, mark);
				statement.executeUpdate();
			}
		}
		catch (SQLException e) {
			log.error("Cannot create Student Mark", e);
			throw new DaoException("Cannot create Student Mark", e);
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
	
	public void updateStudent(Student student) throws DaoException {
		String sql = "UPDATE STUDENT SET "
				+ "GOVERNMENT_FINANCED=?, "
				+ "SCHOOL_CERTIFICATE=?, "
				+ "STUDENT_GROUP_ID=?, "
				+ "ENTRANCE_DATE=?, "
				+ "COMPLETION_DATE=? "
				+ "WHERE STUDENT_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Long studentId = student.getStudentId();
		log.warn("Update Student with id=" + studentId);
		log.debug("Updating Student: Address=" + student.getAddress() + "; Address.id=" + student.getAddress().getId());
		Map<String, Object> sqlParameters = parseStudent(student);
		
		deleteStudentMarks(studentId);
		createStudentMark(student);
		
		personDao.updatePerson(student);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setBoolean(1, (Boolean) sqlParameters.get("governmentFinanced"));
			statement.setString(2, (String) sqlParameters.get("schoolGraduateSertificate"));
			statement.setInt(3, (Integer) sqlParameters.get("studentGroupId"));
			statement.setDate(4, (java.sql.Date) sqlParameters.get("entranceDate"));
			statement.setDate(5, (java.sql.Date) sqlParameters.get("completioDate"));
			statement.setLong(6, studentId);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot update Student", e);
			throw new DaoException("Cannot update Student", e);
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
	
	public void deleteStudent(Student student) throws DaoException {
		String sql = "DELETE FROM STUDENT WHERE STUDENT_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		Long personId = student.getPersonId();
		Long studentId = student.getStudentId();
		
		personDao.deletePersonById(personId);
		deleteStudentMarks(studentId);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, studentId);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot delete Student", e);
			throw new DaoException("Cannot delete Student", e);
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
	
	private void deleteStudentMarks(Long studentId) throws DaoException {
		String sql = "DELETE FROM STUDENT_MARK WHERE STUDENT_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		log.debug("Delete all marks for Student.id=" + studentId);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setLong(1, studentId);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot delete Student Marks", e);
			throw new DaoException("Cannot delete Student Marks", e);
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
	
	public Long getMaxStudentId() throws DaoException {
		String sql = "SELECT MAX(STUDENT_ID) FROM STUDENT";
		
		Long result = 0L; 
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
				
		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			resultSet.next();
			result = resultSet.getLong(1);
			log.debug("Max Student ID is " + result);
		}
		catch (SQLException e) {
			log.error("Cannot get Max Student Id", e);
			throw new DaoException("Cannot get Max Student Id", e);
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
