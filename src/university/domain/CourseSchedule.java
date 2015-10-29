package university.domain;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import university.dao.CourseScheduleDao;
import university.dao.DaoException;

public class CourseSchedule {
	private Course course;
	private Lecturer lecturer;
	private Set<StudentGroup> studentGroups;
	private Set<Calendar> timetables;
	private Integer id;
	
	private static Logger log = Logger.getLogger(CourseSchedule.class);
	
	private CourseScheduleDao courseScheduleDao;
	
	public CourseSchedule(Course course, Lecturer lecturer) {
		log.info("Create new Course Schedule for Course '" + course.getName() + "' (id=" + course.getId() + ") "
				+ "and Lecturer '" + lecturer.getFullName() + "' (id=" + lecturer.getLecturerId() + ")");
		this.course = course;
		this.lecturer = lecturer;
		this.studentGroups = new HashSet<StudentGroup>();
		this.timetables = new HashSet<Calendar>();
		
		courseScheduleDao = new CourseScheduleDao();
	}
	
	public void addStudentGroup(StudentGroup studentGroup) throws DomainException {
		studentGroups.add(studentGroup);
		updateCourseScheduleDB();
	}
	
	public void remove(StudentGroup studentGroup) throws DomainException {
		studentGroups.remove(studentGroup);
		updateCourseScheduleDB();
	}
	
	public void addTimetable(Calendar calendar) throws DomainException {
		timetables.add(calendar);
		updateCourseScheduleDB();
	}
	
	public void remove(Calendar calendar) throws DomainException {
		timetables.remove(calendar);
		updateCourseScheduleDB();
	}
	
	public Lecturer getLecturer() {
		return lecturer;
	}
	
	public Set<StudentGroup> getStudentGroups() {
		return studentGroups;
	}

	public Set<Calendar> getTimetables() {
		return timetables;
	}
	
	public Course getCourse() {
		return course;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setStudentGroups(Set<StudentGroup> studentGroups) {
		this.studentGroups = studentGroups;
	}

	public void setTimetables(Set<Calendar> timetables) {
		this.timetables = timetables;
	}

	public void setLecturer(Lecturer lecturer) throws DomainException {
		this.lecturer = lecturer;
		updateCourseScheduleDB();
	}
	
	private void updateCourseScheduleDB() throws DomainException {
		log.info("Update Course Schedule information in DB: id=" + this.id);
		try {
			courseScheduleDao.updateCourseSchedule(this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot update CourseSchedule", e);
		}
	}
	
}
