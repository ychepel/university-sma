package university.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import university.dao.CourseDao;
import university.dao.CourseScheduleDao;
import university.dao.DaoException;

public class Course {
	private String name;
	private int grade = 0;
	private Integer id; 
	
	private static Logger log = Logger.getLogger(Course.class);
	
	private CourseDao courseDao;
	private CourseScheduleDao courseScheduleDao;
	
	public CourseSchedule createCourseSchedule(Lecturer lecturer, StudentGroup studentGroup) throws DomainException {
		log.info("Create schedule for Course '" + this.name + "' (id=" + this.id + "): "
				+ "Lecturer '" + lecturer.getFullName() + "' (id=" + lecturer.getLecturerId() + "), "
				+ "Student Group '" + studentGroup.getName() + "' (id=" + studentGroup.getId() + ")");
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			Lecturer courseLecturer = courseSchedule.getLecturer();
			if(courseLecturer.equals(lecturer)) {
				log.debug("Adding Group to existing Course Schedule");
				courseSchedule.addStudentGroup(studentGroup);
				return courseSchedule;
			}
		}
		log.debug("Creating new Course Schedule");
		CourseSchedule newCourseSchedule = new CourseSchedule(this, lecturer);
		try {
			courseScheduleDao.createCourseSchedule(newCourseSchedule, this);
		}
		catch (DaoException e ) {
			throw new DomainException("Cannot create Course Schedule", e);
		}
		newCourseSchedule.addStudentGroup(studentGroup);
		this.addCourseSchedule(newCourseSchedule);
		return newCourseSchedule;
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
	
	public void clearCourseSchedules() throws DomainException {
		log.info("Delete all schedules for Course '" + this.name + "' (id=" + this.id + ")");
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			Integer courseScheduleId = courseSchedule.getId();
			log.debug("Delete CourseSchedule with id=" + courseScheduleId);
			try {
				courseScheduleDao.dropCourseScheduleById(courseScheduleId);
			}
			catch (DaoException e) {
				throw new DomainException("Cannot delete all CourseSchedules for Course", e);
			}
		}
	}
	
	public void removeCourseSchedule(CourseSchedule courseSchedule) throws DomainException {
		Integer courseScheduleId = courseSchedule.getId();
		log.debug("Delete Schedule (id=" + courseScheduleId + ") for Course '" + this.name + "' (id=" + this.id + ")");
		try {
			courseScheduleDao.dropCourseScheduleById(courseScheduleId);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot delete CourseSchedule for Course", e);
		}
	}
	
	public Boolean isStudentGroupScheduled(StudentGroup studentGroup) throws DomainException {
		Integer searchingStudentGroupId = studentGroup.getId();
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			Set<StudentGroup> studentGroups = courseSchedule.getStudentGroups();
			for(StudentGroup stGroup : studentGroups) {
				Integer basicStudentGroupId = stGroup.getId();
				if(searchingStudentGroupId.equals(basicStudentGroupId)) {
					log.debug("Student Group '" + studentGroup.getName() + "' (id=" + studentGroup.getId() + ") scheduled");
					return true;
				}
			}
		}
		log.debug("Student Group '" + studentGroup.getName() + "' (id=" + studentGroup.getId() + ") didn't scheduled");
		return false;
	}
	
	public void excludeStudentGroup(StudentGroup studentGroup) throws DomainException {
		log.debug("Removing Student Group '" + studentGroup.getName() + "' (id=" + studentGroup.getId() + ") "
				+ "from Course '" + this.name + "' (id=" + this.id + ")");
		Set<CourseSchedule> courseSchedules = getCourseSchedules();
		Iterator<CourseSchedule> iterator = courseSchedules.iterator();
		while(iterator.hasNext()) {
			CourseSchedule courseSchedule = iterator.next();
			courseSchedule.remove(studentGroup);
			Set<StudentGroup> studentGroups = courseSchedule.getStudentGroups(); 
			if(studentGroups.size() == 0) {
				log.info("Removing empty Course Schedule");
				removeCourseSchedule(courseSchedule);
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
		log.info("Add new Course Schedule");
		try {
			courseScheduleDao.createCourseSchedule(courseSchedule, this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot create Course CourseSchedule", e);
		}
	}
	
	public Course(String name) {
		log.info("Create new Course '" +  name + "'");
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
		updateCourseDB();
	}

	public void setName(String name) throws DomainException {
		this.name = name;
		updateCourseDB();
	}
	
	private void updateCourseDB() throws DomainException {
		log.info("Update Course information in DB: '" + this.name + "' (id=" + this.id + ")");
		if(this.getId() == null) {
			log.warn("Cannot update Course because it have not been created yet");
			return;
		}
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
