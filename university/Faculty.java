package university;

import java.util.Map;
import java.util.Set;

public class Faculty {
	private String name;
	private Set <StudentGroup> studentGroups;
	private Set <Department> departments;
	private final Integer MAX_QUANTITY_IN_GROUP = 30;
	
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
			StudentGroup newStudentGroup = new StudentGroup(studentGrade);
			newStudentGroup.addStudentToGroup(student);
			student.setStudentGroup(newStudentGroup);
		}
	}
	
	public void addLecturer(Lecturer person, Department department) {
		
	}
	
	public void addCourse(Course course, Department department, Lecturer lecturer) {
		
	}
	
	public void enrollStudent(Student person, Course course) {
		
	}
	
	public Map<Course, CourseSchedule> getStudentSchedule(Student person) {
		throw new UnsupportedOperationException();
	}
	
	public Set<Student> getUnderachievementStudent() {
		throw new UnsupportedOperationException();
	}
	
	public Set<StudentGroup> getStudentGroups() {
		return studentGroups;
	}
}
