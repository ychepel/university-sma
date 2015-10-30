package university.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import university.domain.Course;
import university.domain.CourseSchedule;
import university.domain.Lecturer;
import university.domain.StudentGroup;

public class CourseScheduleDao {
	private DaoFactory daoFactory = new DaoFactory();
	private LecturerDao lecturerDao = new LecturerDao();
	private StudentGroupDao studentGroupDao = new StudentGroupDao();
	
	private static Logger log = Logger.getLogger(CourseScheduleDao.class); 
	
	public Set<CourseSchedule> getCourseSchedules(Course course) throws DaoException {
		String sql = "SELECT * FROM COURSE_SCHEDULE WHERE COURSE_ID = ?";
		
		Set<CourseSchedule> set = new HashSet<>(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Integer courseId = course.getId();
		
		log.debug("Get Course Schedules for Course.id=" + courseId + "; name=" + course.getName());

		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, courseId);
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				Integer lecturerId = resultSet.getInt("LECTURER_ID");
				log.warn("Course Schedule Lecturer.id=" + lecturerId);
				Lecturer lecturer = lecturerDao.getLecturerById(lecturerId);
				log.debug("Course Schedule Lecturer.name=" + lecturer.getFullName());
				
				CourseSchedule courseSchedule = new CourseSchedule(course, lecturer);
				Integer courseScheduleId = resultSet.getInt("COURSE_SCHEDULE_ID");
				log.debug("Course Schedule id=" + courseScheduleId);
				courseSchedule.setId(courseScheduleId);
				
				Set<StudentGroup> studentGroups = getStudentGroups(courseScheduleId);
				courseSchedule.setStudentGroups(studentGroups);
				Set<Calendar> timetables = getTimetables(courseScheduleId);
				courseSchedule.setTimetables(timetables);
				
				set.add(courseSchedule);
			}
		}
		catch (SQLException e) {
			log.error("Cannot get Course Schedule by Course", e);
			throw new DaoException("Cannot get Course Schedule by Course", e);
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
	
	private Set<StudentGroup> getStudentGroups(Integer courseScheduleId) throws DaoException {
		String sql = "SELECT * FROM COURSE_SCHEDULE_GROUP WHERE COURSE_SCHEDULE_ID = ?";
		log.debug("Selecting Student Groups for CourseSchedule.id=" + courseScheduleId);
		Set<StudentGroup> set = new HashSet<>(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, courseScheduleId);
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				Integer studentGroupId = resultSet.getInt("STUDENT_GROUP_ID");
				StudentGroup studentGroup = studentGroupDao.getStudentGroupById(studentGroupId);
				log.warn("Selected StudentGroup=" + studentGroup);
				if(studentGroup != null) {
					log.debug("Selected StudentGroup.id=" + studentGroupId + ", StudentGroup.name=" + studentGroup.getName());
					set.add(studentGroup);
				}
			}
		}
		catch (SQLException e) {
			log.error("Cannot get Course Schedule Student Groups", e);
			throw new DaoException("Cannot get Course Schedule Student Groups", e);
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
	
	private Set<Calendar> getTimetables(Integer courseScheduleId) throws DaoException {
		String sql = "SELECT * FROM COURSE_SCHEDULE_TIMETABLE WHERE COURSE_SCHEDULE_ID = ?";
		log.debug("Selecting Timetable for CourseSchedule.id=" + courseScheduleId);
		Set<Calendar> set = new HashSet<>(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, courseScheduleId);
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				Timestamp timeStamp = resultSet.getTimestamp("DATETIME");
				log.warn("Selected Timetable.timeStamp=" + timeStamp);
				Calendar calendar = new GregorianCalendar();
				calendar.setTimeInMillis(timeStamp.getTime());
				set.add(calendar);
			}
		}
		catch (SQLException e) {
			log.error("Cannot get Course Schedule Timetables", e);
			throw new DaoException("Cannot get Course Schedule Timetables", e);
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
	
	public CourseSchedule createCourseSchedule(CourseSchedule courseSchedule, Course course) throws DaoException {
		Integer courseId = course.getId();
		Lecturer lecturer = courseSchedule.getLecturer();
		Integer lecturerId = lecturer.getLecturerId();
		String sql = "INSERT INTO COURSE_SCHEDULE (COURSE_ID, LECTURER_ID) VALUES (" + courseId + ", " + lecturerId + ")";
		log.debug(sql);
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		createCourseScheduleGroup(courseSchedule);
		createCourseScheduleTimetable(courseSchedule);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.createStatement();
			
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			resultSet = statement.getGeneratedKeys();
			resultSet.next();
			Integer id = resultSet.getInt(1);
			log.debug("New CourseSchedule.id=" + id);
			courseSchedule.setId(id);
		}
		catch (SQLException e) {
			log.error("Cannot create Course Schedule for Course", e);
			throw new DaoException("Cannot create Course Schedule for Course", e);
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
		
		return courseSchedule;
	}
	
	private void createCourseScheduleGroup(CourseSchedule courseSchedule) throws DaoException {
		String sql = "INSERT INTO COURSE_SCHEDULE_GROUP (COURSE_SCHEDULE_ID, STUDENT_GROUP_ID) VALUES (?, ?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Integer courseScheduleId = courseSchedule.getId();
		Set<StudentGroup> studentGroups = courseSchedule.getStudentGroups();
		if(studentGroups.size() == 0) {
			log.warn("No Student Groups for Course Schedule (id=" + courseScheduleId + ")");
			return;
		}
		log.info("Inserting StudentGroups for CourseSchedule.id=" + courseScheduleId);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, courseScheduleId);
			
			for(StudentGroup studentGroup : studentGroups) {
				Integer studentGroupId = studentGroup.getId();
				log.debug("Inserting StudentGroup.id=" + studentGroupId);
				statement.setInt(2, studentGroupId);
				statement.executeUpdate();
			}
		}
		catch (SQLException e) {
			log.error("Cannot create Course Schedule Student Groups", e);
			throw new DaoException("Cannot create Course Schedule Student Groups", e);
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
	
	private void createCourseScheduleTimetable(CourseSchedule courseSchedule) throws DaoException {
		String sql = "INSERT INTO COURSE_SCHEDULE_TIMETABLE (COURSE_SCHEDULE_ID, DATETIME) VALUES (?, ?)";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Integer courseScheduleId = courseSchedule.getId();
		Set<Calendar> timetables = courseSchedule.getTimetables();
		if(timetables.size() == 0) {
			log.warn("No Timetables for Course Schedule (id=" + courseScheduleId + ")");
			return;
		}
		log.info("Inserting Timetables for CourseSchedule.id=" + courseScheduleId);
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, courseScheduleId);
			
			for(Calendar calendar : timetables) {
				Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
				log.debug("Inserting StudentGroup timestamp=" + timestamp);
				statement.setTimestamp(2, timestamp);
				statement.executeUpdate();
			}
		}
		catch (SQLException e) {
			log.error("Cannot create Course Schedule Timetables", e);
			throw new DaoException("Cannot create Course Schedule Timetables", e);
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
	
	public void updateCourseSchedule(CourseSchedule courseSchedule) throws DaoException {
		String sql = "UPDATE COURSE_SCHEDULE SET LECTURER_ID=? WHERE COURSE_SCHEDULE_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Integer courseScheduleId = courseSchedule.getId(); 
		Lecturer lecturer = courseSchedule.getLecturer();
		Integer lecturerId = lecturer.getLecturerId();
		log.warn("Updating of Course Schedule (id=" + courseScheduleId + "): Lecturer.id=" + lecturerId);
		
		if(courseScheduleId != null) {
			dropCourseScheduleStudentGroups(courseScheduleId);
			dropCourseScheduleTimetables(courseScheduleId);
		}
		createCourseScheduleGroup(courseSchedule);
		createCourseScheduleTimetable(courseSchedule);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			
			statement.setInt(1, lecturerId);
			statement.setInt(2, courseScheduleId);
			
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot update Course Schedule data", e);
			throw new DaoException("Cannot update Course Schedule data", e);
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
	
	public void dropCourseScheduleById(Integer id) throws DaoException {
		String sql = "DELETE FROM COURSE_SCHEDULE WHERE COURSE_SCHEDULE_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		dropCourseScheduleStudentGroups(id);
		dropCourseScheduleTimetables(id);
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot delete Course Schedule data", e);
			throw new DaoException("Cannot delete Course Schedule data", e);
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
	
	private void dropCourseScheduleStudentGroups(Integer id) throws DaoException {
		String sql = "DELETE FROM COURSE_SCHEDULE_GROUP WHERE COURSE_SCHEDULE_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot delete Course Schedule Student Groups", e);
			throw new DaoException("Cannot delete Course Schedule Student Groups", e);
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
	
	private void dropCourseScheduleTimetables(Integer id) throws DaoException {
		String sql = "DELETE FROM COURSE_SCHEDULE_TIMETABLE WHERE COURSE_SCHEDULE_ID=?";
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.executeUpdate();
		}
		catch (SQLException e) {
			log.error("Cannot delete Course Schedule Timetables", e);
			throw new DaoException("Cannot delete Course Schedule Timetables", e);
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
