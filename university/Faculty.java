package university;

import java.util.HashSet;
import java.util.Set;

public class Faculty {
	
	private final Integer MAX_QUANTITY_IN_GROUP = 30;
	private final Integer UNDERACHIEVMENT_AVG_MARK_LEVEL = 35;
	
	private String name;
	private Set<StudentGroup> studentGroups;
	private Set<Department> departments;
	
	public void add(Student student) {
		Integer studentGrade = student.getGrade();
		for(StudentGroup studentGroup : getStudentGroups()) {
			if(studentGrade == studentGroup.getGrade()) {
				if(studentGroup.getStudentQuantity() < MAX_QUANTITY_IN_GROUP) {
					studentGroup.addToGroup(student);
					student.setStudentGroup(studentGroup);
					break;
				}
			}
		}
		if(student.getStudentGroup() == null) {
			String newStudentGroupName = "G" + studentGrade + "-" + (getGroupsQuantityOnGrade(studentGrade) + 1); 
			StudentGroup newStudentGroup = new StudentGroup(newStudentGroupName);
			newStudentGroup.addToGroup(student);
			student.setStudentGroup(newStudentGroup);
		}
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
	
	public void addLecturer(Department department, Lecturer lecturer) {
		department.add(lecturer);
	}
	
	public void addCourse(Department department, Course course) {
		department.add(course);
	}
	
	public Boolean enrollStudent(Student person, Course course) {
		for(Department department : departments) {
			if(department.enrollStudent(person, course)) {
				return true;
			}
		}
		return false;
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

	public Set<CourseSchedule> getSchedule(Student student) {
		Set<CourseSchedule> schedule = new HashSet<>();
		StudentGroup studentGroup =  student.getStudentGroup();
		for(Department department : departments) {
			for(Course course : department.getCourses()) {
				if(studentGroup.getStudentsOnCourse(course).contains(student)) {
					schedule.addAll(course.getSchedule(studentGroup));
				}
			}
		}
		return schedule;
	}
	
	public Set<CourseSchedule> getSchedule(Lecturer lecturer) {
		Set<CourseSchedule> schedule = new HashSet<>();
		for(Department department :departments) {
				schedule.addAll(department.getSchedule(lecturer));
		}
		return schedule;
	}
	
	public Set<Lecturer> getLecturers() {
		Set<Lecturer> lecturers = new HashSet<>();
		for(Department department :departments) {
			lecturers.addAll(department.getLecturers());
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
	}

	public void removeStudent(Student student) {
		for(Department department :departments) {
			department.excludeStudent(student);
		}
		student.setStudentGroup(null);
	}
	
}
