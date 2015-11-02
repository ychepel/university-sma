package university.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

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
	
	private static Logger log = Logger.getLogger(Department.class);
	
	public Set<CourseSchedule> getSchedule(Lecturer lecturer) throws DomainException {
		Set<CourseSchedule> schedule = new HashSet<>();
		for(Course course : getCourses()) {
			Set<CourseSchedule> courseSchedules = course.getScheduleByLecturer(lecturer);
			schedule.addAll(courseSchedules);
		}
		return schedule;
	}
	
	protected Boolean enrollStudent(Student student, Course course) throws DomainException {
		Integer courseId = course.getId();
		if(courseId ==  null) {
			log.warn("Course '" + course.getName() + "' wasn't added to any Department");
			return false;
		}
		log.info("Enroll Student '" + student.getFullName() + "' (id=" + student.getStudentId() + ") "
				+ "to Course '" + course.getName() + "' (id=" + courseId + ")");
		
		Set<Course> courses = getCourses();
		Boolean courseAvailable = false;
		for(Course departmentCourse : courses) {
			Integer departmentCourseId = departmentCourse.getId();
			if(courseId.equals(departmentCourseId)) {
				courseAvailable = true;
				break;
			}
		}
		if(!courseAvailable) return false;
		
		StudentGroup studentGroup = student.getStudentGroup();
		if(course.isStudentGroupScheduled(studentGroup)) {
			student.addMark(course, -1);
			return true;
		}
		return false;
	}
	
	protected void addLecturer(Lecturer lecturer) throws DomainException {
		log.info("Add Lecturer '" + lecturer.getFullName() + "' (id=" + lecturer.getLecturerId() + ") "
				+ "to Department '" + this.name + "' (id=" + this.id + ")");
		try {
			lecturerDao.createLecturer(lecturer, this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot create Department Lecturer", e);
		}
	}
	
	public void addCourse(Course course) throws DomainException {
		log.info("Add Course '" + course.getName() + "' (id=" + course.getId() + ") "
				+ "to Department '" + this.name + "' (id=" + this.id + ")");
		try {
			courseDao.createCourse(course, this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot create Department Course", e);
		}
	}
	
	public void removeLecturer(Lecturer lecturer) throws DomainException {
		log.info("Remove Lecturer '" + lecturer.getFullName() + "' (id=" + lecturer.getLecturerId() + ") "
				+ "from Department '" + this.name + "' (id=" + this.id + ")");
		Integer lecturerId = lecturer.getLecturerId();
		try {
			lecturerDao.dropLecturerById(lecturerId);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot drop Department Lecturer", e);
		}
	}
	
	public void removeCourse(Course course) throws DomainException {
		log.info("Remove Course '" + course.getName() + "' (id=" + course.getId() + ") "
				+ "from Department '" + this.name + "' (id=" + this.id + ")");
		course.clearCourseSchedules();
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

	protected void excludeStudentGroup(StudentGroup studentGroup) throws DomainException {
		for(Course course : getCourses()) {
			course.excludeStudentGroup(studentGroup);
		}
	}
	
	public Department(String name) {
		log.info("Create new Department '" +  name + "'");
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
