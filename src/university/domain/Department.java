package university.domain;

import java.util.HashSet;
import java.util.Set;

import university.dao.CourseDao;
import university.dao.DaoException;
import university.dao.DepartmentDao;
import university.dao.LecturerDao;

public class Department {
	private String name;
	private Integer id;
	
	private DepartmentDao departmentDao;
	private CourseDao courseDao;
	private LecturerDao lecturerDao;
	
	public Set<CourseSchedule> getSchedule(Lecturer lecturer) throws DomainException {
		Set<CourseSchedule> schedule = new HashSet<>();
		for(Course course : getCourses()) {
			Set<CourseSchedule> courseSchedules = course.getScheduleByLecturer(lecturer);
			schedule.addAll(courseSchedules);
		}
		return schedule;
	}
	
	public Boolean enrollStudent(Student student, Course course) throws DomainException {
		Set<Course> courses = getCourses();
		if(!courses.contains(course)) return false;
		
		StudentGroup studentGroup = student.getStudentGroup();
		if(course.isStudentGroupScheduled(studentGroup)) {
			student.addMark(course, -1);
			return true;
		}
		return false;
	}
	
	public void addLecturer(Lecturer lecturer) throws DomainException {
		Set<Lecturer> lecturers = getLecturers();
		if(!lecturers.contains(lecturer)) {
			try {
				lecturerDao.createLecturer(lecturer, this);
			}
			catch (DaoException e) {
				throw new DomainException("Cannot create Department Lecturer", e);
			}
		}
	}
	
	public void addCourse(Course course) throws DomainException {
		Set<Course> courses = getCourses();
		if(!courses.contains(course)) {
			try {
				courseDao.createCourse(course, this);
			}
			catch (DaoException e) {
				throw new DomainException("Cannot create Department Course", e);
			}
		}
	}
	
	public void removeLecturer(Lecturer lecturer) throws DomainException {
		Integer lecturerId = lecturer.getLecturerId();
		try {
			lecturerDao.dropLecturerById(lecturerId);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot drop Department Lecturer", e);
		}
	}
	
	public void removeCourse(Course course) throws DomainException {
		Integer courseId = course.getId();
		try {
			courseDao.dropCourseById(courseId);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot drop Department Course", e);
		}
	}
	
	public Set<Course> getCourses() throws DomainException {
		Set<Course> result = new HashSet<>();
		try {
			result = courseDao.getCourses(this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot receive Department Courses", e);
		}
		return result;
	}

	public Set<Lecturer> getLecturers() throws DomainException {
		Set<Lecturer> result = new HashSet<>();
		try {
			result = lecturerDao.getLecturers(this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot receive Department Lecturers", e);
		}
		return result;
	}

	public void excludeStudentGroup(StudentGroup studentGroup) throws DomainException {
		for(Course course : getCourses()) {
			course.excludeStudent(studentGroup);
		}
	}
	
	public Department(String name) {
		this.name = name;
		
		this.departmentDao = new DepartmentDao();
		this.courseDao = new CourseDao();
		this.lecturerDao = new LecturerDao();
	}
	
	public String getName() {
		return name;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setName(String name) throws DomainException {
		this.name = name;
		try {
			departmentDao.updateDepartment(this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot update faculty", e);
		}
	}
	
	
}
