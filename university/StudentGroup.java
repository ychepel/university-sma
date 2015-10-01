package university;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StudentGroup {
	private String name;
	private Set<Student> students;
	
	public Map<Student, Integer> getSuccessRating(Course course) {
		Map<Student, Integer> result = new HashMap<>();
		for(Student student : getStudents()) {
			Map<Course, Integer> studentMarks = student.getMarks();
			if(studentMarks.containsKey(course))
				result.put(student, studentMarks.get(course));
		}
		return result;
	}
	
	public Integer getStudentGroupGrade() {
		Integer grade = 0;
		for(Student student : getStudents()) {
			if(student.getGrade() != 0) {
				grade = student.getGrade();
				break;
			}
		}
		return grade;
	}
	
	public Set<Student> getStudentsOnCourse(Course course) {
		Set<Student> students = new HashSet<>();
		for(Student student : getStudents()) {
			if(student.getMarks().containsKey(course)) {
				if(student.getMarks().get(course) == null) {
					students.add(student);
				}
			}
		}
		return students;
	}
	
	public StudentGroup(String name) {
		this.name = name;
		this.students = new HashSet<Student>();
	}
	
	public void addStudentToGroup(Student student) {
		students.add(student);
	}

	public Set<Student> getStudents() {
		return students;
	}

	public String getName() {
		return name;
	}
	
	public Integer getStudentQuantity() {
		return getStudents().size();
	}
	
}
