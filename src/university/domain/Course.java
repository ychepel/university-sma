package university.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import university.dao.CourseDao;
import university.dao.CourseScheduleDao;
import university.dao.DaoException;

public class Course {
	private String name;
	private int grade = 0;
	private Integer id; 
	
	private CourseDao courseDao;
	private CourseScheduleDao courseScheduleDao;
	
	public void create(Lecturer lecturer, StudentGroup studentGroup) throws DomainException {
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			Lecturer courseLecturer = courseSchedule.getLecturer();
			if(courseLecturer.equals(lecturer)) {
				courseSchedule.addStudentGroup(studentGroup);
				return;
			}
		}
		
		CourseSchedule newCourseSchedule = new CourseSchedule(this, lecturer);
		newCourseSchedule.addStudentGroup(studentGroup);
		this.addCourseSchedule(newCourseSchedule);
	}
	
	public Set<CourseSchedule> getScheduleByLecturer(Lecturer person) throws DomainException {
		Set<CourseSchedule> schedule = new HashSet<CourseSchedule>();
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			Lecturer lecturer = courseSchedule.getLecturer();
			if(lecturer.equals(person)) {
				schedule.add(courseSchedule);
			}
		}
		return schedule;
	}
	
	public Set<CourseSchedule> getScheduleByStudentGroup(StudentGroup studentGroup) throws DomainException {
		Set<CourseSchedule> schedule = new HashSet<CourseSchedule>();
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			Set<StudentGroup> studentGroups = courseSchedule.getStudentGroups();
			if(studentGroups.contains(studentGroup)) {
				schedule.add(courseSchedule);
			}
		}
		return schedule;
	}
	
	public Boolean isStudentGroupScheduled(StudentGroup studentGroup) throws DomainException {
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			Set<StudentGroup> studentGroups = courseSchedule.getStudentGroups();
			if(studentGroups.contains(studentGroup)) {
				return true;
			}
		}
		return false;
	}
	
	public void excludeStudent(StudentGroup studentGroup) throws DomainException {
		Set<CourseSchedule> courseSchedules = getCourseSchedules();
		Iterator<CourseSchedule> iterator = courseSchedules.iterator();
		while(iterator.hasNext()) {
			CourseSchedule courseSchedule = iterator.next();
			courseSchedule.remove(studentGroup);
			Set<StudentGroup> studentGroups = courseSchedule.getStudentGroups(); 
			if(studentGroups.size() == 0) {
				courseSchedules.remove(courseSchedule);
			}
		}
	}
	
	public Set<CourseSchedule> getCourseSchedules() throws DomainException {
		Set<CourseSchedule> result = new HashSet<>();
		try {
			result = courseScheduleDao.getCourseSchedules(this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot receive Course CourseSchedules", e);
		}
		return result;
	}
	
	public void addCourseSchedule(CourseSchedule courseSchedule) throws DomainException {
		try {
			courseScheduleDao.createCourseSchedule(courseSchedule, this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot create Course CourseSchedule", e);
		}
	}
	
	public Course(String name) {
		this.name = name;
		
		courseDao = new CourseDao();
		courseScheduleDao = new CourseScheduleDao(); 
	}

	public String getName() {
		return name;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) throws DomainException {
		this.grade = grade;
		updateCourseDao();
	}

	public void setName(String name) throws DomainException {
		this.name = name;
		updateCourseDao();
	}
	
	private void updateCourseDao() throws DomainException {
		try {
			courseDao.updateCourse(this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot update Course CourseSchedule", e);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}