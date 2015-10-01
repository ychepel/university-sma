package university;

import java.util.HashSet;
import java.util.Set;

public class Faculty {
	private String name;
	private Set<StudentGroup> studentGroups;
	private Set<Department> departments;
	
	private final Integer MAX_QUANTITY_IN_GROUP = 30;
	private final Integer UNDERACHIEVMENT_AVG_MARK_LEVEL = 35;
	
	public void addStudent(Student student) {
		Integer studentGrade = student.getGrade();
		for(StudentGroup studentGroup : getStudentGroups()) {
			if(studentGrade == studentGroup.getStudentGroupGrade()) {
				if(studentGroup.getStudentQuantity() < MAX_QUANTITY_IN_GROUP) {
					studentGroup.addStudentToGroup(student);
					student.setStudentGroup(studentGroup);
					break;
				}
			}
		}
		if(student.getStudentGroup() == null) {
			String newStudentGroupName = "G" + studentGrade + "-" + (getGroupQuantityOnGrade(studentGrade) + 1); 
			StudentGroup newStudentGroup = new StudentGroup(newStudentGroupName);
			newStudentGroup.addStudentToGroup(student);
			student.setStudentGroup(newStudentGroup);
		}
	}
	
	public Integer getGroupQuantityOnGrade(Integer grade) {
		Integer quantity = 0;
		for(StudentGroup studentGroup : getStudentGroups()) {
			if(grade == studentGroup.getStudentGroupGrade()) {
				quantity++;
			}
		}
		return quantity;
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
	
	public Set<CourseSchedule> getStudentSchedule(Student student) {
		Set<CourseSchedule> schedule = new HashSet<>();
		StudentGroup studentGroup =  student.getStudentGroup();
		for(Department department : departments) {
			for(Course course : department.getCourses()) {
				if(studentGroup.getStudentsOnCourse(course).contains(student)) {
					schedule.addAll(course.getCourseSchedule(studentGroup));
				}
			}
		}
		return schedule;
	}
	
	public Set<Student> getUnderachievementStudent() {
		Set<Student> students = new HashSet<>();
		for(StudentGroup studentGroup : getStudentGroups()) {
			for(Student student : studentGroup.getStudents()) {
				if(student.getAverageMark() < UNDERACHIEVMENT_AVG_MARK_LEVEL) {
					students.add(student);
				}
			}
		}
		return students;
	}
	
	public Set<StudentGroup> getStudentGroups() {
		return studentGroups;
	}
	
	public Faculty(String name) {
		this.name = name;
		studentGroups = new HashSet<>();
		departments = new HashSet<>();
	}
}
