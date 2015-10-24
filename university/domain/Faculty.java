package university.domain;

import java.util.HashSet;
import java.util.Set;

import university.dao.DaoException;
import university.dao.DepartmentDao;

public class Faculty {
	
	private final Integer MAX_QUANTITY_IN_GROUP = 30;
	private final Integer UNDERACHIEVMENT_AVG_MARK_LEVEL = 35;
	
	private Integer id;
	private String name;
	private Set<StudentGroup> studentGroups;
	private Set<Department> departments;
	private DepartmentDao departmentDao;
	
	public void addStudent(Student student) {
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
		this.addStudentGroup(newStudentGroup);
		student.setStudentGroup(newStudentGroup);
	}
	
	public Integer getGroupsQuantityOnGrade(Integer grade) {
		Integer quantity = 0;
		for(StudentGroup studentGroup : getStudentGroups()) {
			if(grade == studentGroup.getGrade()) {
				quantity++;
			}
		}
		return quantity;
	}
	
	public Set<Student> getUnderachievementStudent() {
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
	
	public void addLecturer(Department department, Lecturer lecturer) {
		department.addLecturer(lecturer);
	}
	
	public void addCourse(Department department, Course course) {
		department.addCourse(course);
	}
	
	public Boolean enrollStudent(Student person, Course course) {
		for(Department department : departments) {
			if(department.enrollStudent(person, course)) {
				return true;
			}
		}
		return false;
	}
	
	public Set<CourseSchedule> getSchedule(Student student) {
		Set<CourseSchedule> schedule = new HashSet<>();
		StudentGroup studentGroup =  student.getStudentGroup();
		for(Department department : departments) {
			for(Course course : department.getCourses()) {
				Set<Student> students = studentGroup.getStudentsOnCourse(course);
				if(students.contains(student)) {
					Set<CourseSchedule> courseSchedules = course.getScheduleByStudentGroup(studentGroup);
					schedule.addAll(courseSchedules);
				}
			}
		}
		return schedule;
	}
	
	public Set<CourseSchedule> getSchedule(Lecturer lecturer) {
		Set<CourseSchedule> schedule = new HashSet<>();
		for(Department department :departments) {
			Set<CourseSchedule> courseSchedules = department.getSchedule(lecturer);
			schedule.addAll(courseSchedules);
		}
		return schedule;
	}
	
	public Set<Lecturer> getLecturers() {
		Set<Lecturer> lecturers = new HashSet<>();
		for(Department department :departments) {
			Set<Lecturer> departmentLecturers = department.getLecturers();
			lecturers.addAll(departmentLecturers);
		}
		return lecturers;
	}
	
	public Set<StudentGroup> getStudentGroups() {
		return studentGroups;
	}
	
	public Faculty(String name) {
		this.name = name;
		studentGroups = new HashSet<>();
		departments = new HashSet<>();
		
		this.departmentDao = new DepartmentDao();
	}

	public void removeStudent(Student student) {
		StudentGroup studentGroup = student.getStudentGroup();
		student.setStudentGroup(null);
		studentGroup.removeStudent(student);
		if(studentGroup.getStudentQuantity() == 0) {
			this.removeStudentGroup(studentGroup);
		}
	}
	
	private void removeStudentGroup(StudentGroup studentGroup) {
		this.studentGroups.remove(studentGroup);
		for(Department department :departments) {
			department.excludeStudentGroup(studentGroup);
		}
	}

	public void addStudentGroup(StudentGroup studentGroup) {
		studentGroups.add(studentGroup);
	}
	
	public Integer getMaxQuantityInGroupParameter() {
		return MAX_QUANTITY_IN_GROUP;
	}
	
	public Integer getUnderachievmentLevelParameter() {
		return UNDERACHIEVMENT_AVG_MARK_LEVEL;
	}
	
	public void createDepartment(String name) throws DomainException {
		Department department;
		try {
			department = departmentDao.createDepartment(name, this);
		}
		catch (DaoException e) {
			throw new DomainException("Cannnot create department", e); 
		}
		addDepartment(department);
	}
	
	public void addDepartment(Department department) {
		this.departments.add(department);
	}
	
	public void removeDepartment(Department department) throws DomainException {
		Integer id = department.getId();
		try {
			departmentDao.dropDepartmentById(id);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot drop the department", e);
		}
		this.departments.remove(department);
	}
	
	public String getName() {
		return name;
	}
	
	public Set<Department> getDepartments() {
		return departments;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	
}
