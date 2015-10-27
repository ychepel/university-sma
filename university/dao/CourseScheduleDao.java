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

import university.domain.Course;
import university.domain.CourseSchedule;
import university.domain.Lecturer;
import university.domain.StudentGroup;

public class CourseScheduleDao {
	private DaoFactory daoFactory = new DaoFactory();
	private LecturerDao lecturerDao = new LecturerDao();
	private StudentGroupDao studentGroupDao = new StudentGroupDao();
	
	public Set<CourseSchedule> getCourseSchedules(Course course) throws DaoException {
		String sql = "SELECT * FROM COURSE_SCHEDULE WHERE COURSE_ID = ?";
		
		Set<CourseSchedule> set = new HashSet<>(); 
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		Integer courseId = course.getId();

		try {
			connection = daoFactory.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, courseId);
			resultSet = statement.executeQuery();

			while(resultSet.next()) {
				Integer lecturerId = resultSet.getInt("LECTURER_ID");
				Lecturer lecturer = lecturerDao.getLecturerById(lecturerId);
				
				CourseSchedule element = new CourseSchedule(course, lecturer);
				Integer courseScheduleId = resultSet.getInt("COURSE_SCHEDULE_ID");
				element.setId(courseScheduleId);
				
				Set<StudentGroup> studentGroups = getStudentGroups(courseScheduleId);
				element.setStudentGroups(studentGroups);
				Set<Calendar> timetables = getTimetables(courseScheduleId);
				element.setTimetables(timetables);
				
				set.add(element);
				
			}
		}
		catch (SQLException e) {
			throw new DaoException("Cannot get Course Schedule data", e);
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
				set.add(studentGroup);
			}
		}
		catch (SQLException e) {
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
				Calendar calendar = new GregorianCalendar();
				calendar.setTimeInMillis(timeStamp.getTime());
				set.add(calendar);
			}
		}
		catch (SQLException e) {
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
		
		CourseSchedule result = null; 
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
			
			result = new CourseSchedule(course, lecturer);
			result.setId(id);
		}
		catch (SQLException e) {
			throw new DaoException("Cannot create Course Schedule data", e);
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
		
		return result;
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
