package university.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import university.dao.DaoException;
import university.dao.DepartmentDao;
import university.dao.FacultyDao;
import university.dao.StudentGroupDao;

public class Faculty {
	
	private final Integer MAX_QUANTITY_IN_GROUP = 3;
	private final Integer UNDERACHIEVMENT_AVG_MARK_LEVEL = 35;
	
	private Integer id;
	private String name;
	
	private DepartmentDao departmentDao;
	private StudentGroupDao studentGroupDao;
	private FacultyDao facultyDao;
	
	private static Logger log = Logger.getLogger(Faculty.class);
	
	public void addStudent(Student student) throws DomainException {
		log.info("Add Student '" + student.getFullName() + "' (id=" + student.getStudentId() + ") to existing Student Group or create new Group");
		Integer studentGrade = student.getGrade();
		for(StudentGroup studentGroup : getStudentGroups()) {
			if(studentGrade == studentGroup.getGrade()) {
				if(studentGroup.getStudentQuantity() < MAX_QUANTITY_IN_GROUP) {
					student.setStudentGroup(studentGroup);
					return;
				}
			}
		}
		
		String newStudentGroupName = "G" + studentGrade + "-" + (getGroupsQuantityOnGrade(studentGrade) + 1); 
		StudentGroup newStudentGroup = new StudentGroup(newStudentGroupName);
		newStudentGroup = createStudentGroup(newStudentGroup);
		student.setStudentGroup(newStudentGroup);
	}
	
	public Integer getGroupsQuantityOnGrade(Integer grade) throws DomainException {
		log.info("Calculation Groups quantity on grade " + grade + " for Faculty '" + this.getName() + "' (id=" + this.getId() + ")");
		Integer quantity = 0;
		for(StudentGroup studentGroup : getStudentGroups()) {
			if(grade == studentGroup.getGrade()) {
				quantity++;
			}
		}
		return quantity;
	}
	
	public Set<Student> getUnderachievementStudent() throws DomainException {
		Set<Student> students = new HashSet<>();
		for(StudentGroup studentGroup : getStudentGroups()) {
			for(Student student : studentGroup.getStudents()) {
				Integer averageMark = student.getAverageMark();
				if(averageMark < 0 ) continue;
				if(averageMark < UNDERACHIEVMENT_AVG_MARK_LEVEL) {
					students.add(student);
				}
			}
		}
		return students;
	}
	
	public void addLecturer(Department department, Lecturer lecturer) throws DomainException {
		department.addLecturer(lecturer);
	}
	
	public Boolean enrollStudent(Student person, Course course) throws DomainException {
		for(Department department : getDepartments()) {
			if(department.enrollStudent(person, course)) {
				return true;
			}
		}
		return false;
	}
	
	public Set<CourseSchedule> getStudentSchedule(Student student) throws DomainException {
		Long studentId = student.getStudentId();
		log.info("Get CourseSchedules for Student '" + student.getFullName() +"' (id=" + studentId + ")");
		Set<CourseSchedule> schedule = new HashSet<>();
		StudentGroup studentGroup =  student.getStudentGroup();
		for(Department department : getDepartments()) {
			Set<Course> courses = department.getCourses();
			for(Course course : courses) {
				Set<Student> students = studentGroup.getStudentsOnCourse(course);
				for(Student courseStudent : students) {
					Long courseStudentId = courseStudent.getStudentId();
					if(courseStudentId.equals(studentId)) {
						Set<CourseSchedule> courseSchedules = course.getScheduleByStudentGroup(studentGroup);
						schedule.addAll(courseSchedules);
					}
				}
			}
		}
		return schedule;
	}
	
	public Set<CourseSchedule> getLecturerSchedule(Lecturer lecturer) throws DomainException {
		Set<CourseSchedule> schedule = new HashSet<>();
		for(Department department :getDepartments()) {
			Set<CourseSchedule> courseSchedules = department.getSchedule(lecturer);
			schedule.addAll(courseSchedules);
		}
		return schedule;
	}
	
	public Set<Lecturer> getLecturers() throws DomainException {
		Set<Lecturer> lecturers = new HashSet<>();
		for(Department department : getDepartments()) {
			Set<Lecturer> departmentLecturers = department.getLecturers();
			lecturers.addAll(departmentLecturers);
		}
		return lecturers;
	}
	
	public Set<StudentGroup> getStudentGroups() throws DomainException {
		Set<StudentGroup> result = new HashSet<>();
		try {
			result = studentGroupDao.getStudentGroups(this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot receive Faculty Student Group", e);
		}
		return result;
	}
	
	public Faculty(String name) {
		log.info("Create new Faculty '" + name + "'");
		this.name = name;
		
		this.facultyDao = new FacultyDao();
		this.departmentDao = new DepartmentDao();
		this.studentGroupDao = new StudentGroupDao();
	}

	public void removeStudent(Student student) throws DomainException {
		log.info("Remove Student '" + student.getFullName() + "' (id=" + student.getStudentId() + ")");
		StudentGroup studentGroup = student.getStudentGroup();
		studentGroup.removeStudent(student);
		if(studentGroup.getStudentQuantity() == 0) {
			this.removeStudentGroup(studentGroup);
		}
	}
	
	public void removeStudentGroup(StudentGroup studentGroup) throws DomainException {
		Integer studentGroupId = studentGroup.getId();
		log.info("Remove Student Group '" + studentGroup.getName() + "' (id=" + studentGroupId + ")");
		try {
			studentGroupDao.deleteStudentGroupById(studentGroupId);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot drop Faculty Student Group", e);
		}
		
		for(Department department : getDepartments()) {
			department.excludeStudentGroup(studentGroup);
		}
	}

	protected StudentGroup createStudentGroup(StudentGroup studentGroup) throws DomainException {
		log.info("Create Student Group '" + studentGroup.getName() + "' on Faculty '" + this.getName() + "' (id=" + this.getId() + ")");
		try {
			studentGroup = studentGroupDao.createStudentGroup(studentGroup, this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot create Faculty Student Group", e);
		}
		return studentGroup;
	}
	
	public Integer getMaxQuantityInGroupParameter() {
		return MAX_QUANTITY_IN_GROUP;
	}
	
	public Integer getUnderachievmentLevelParameter() {
		return UNDERACHIEVMENT_AVG_MARK_LEVEL;
	}
	
	public Department createDepartment(String name) throws DomainException {
		log.info("Create Department '" + name + "'");
		Department department = new Department(name);
		try {
			department = departmentDao.createDepartment(department, this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannnot create department", e); 
		}
		return department;
	}
	
	public void removeDepartment(Department department) throws DomainException {
		Integer id = department.getId();
		log.info("Remove Department '" + department.getName() + "' (id=" + id + ")");
		try {
			departmentDao.deleteDepartmentById(id);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot drop the department", e);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public Set<Department> getDepartments() throws DomainException {
		Set<Department> result = new HashSet<>();
		try {
			result = departmentDao.getDepartments(this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot receive Faculty Department", e);
		}
		return result;
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
			facultyDao.updateFaculty(this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot update faculty", e);
		}
	}
	
}
