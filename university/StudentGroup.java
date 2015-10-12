package university;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StudentGroup {
	private String name;
	private Set<Student> students;
	
	public Map<Student, Integer> getSuccessRating(Course course) {
		Map<Student, Integer> result = new HashMap<>();
		for(Student student : getStudents()) {
			Map<Course, Integer> studentMarks = student.getMarks();
			if(studentMarks.containsKey(course)) {
				Integer mark = studentMarks.get(course);
				if(mark.equals(-1)) continue;
				result.put(student, studentMarks.get(course));
			}
		}
		result = sortStudentByMark(result);
		return result;
	}

	private Map<Student, Integer> sortStudentByMark( Map<Student, Integer> map ) {
		List<Map.Entry<Student, Integer>> list =  new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Student, Integer>>() {
			@Override
			public int compare(Map.Entry<Student, Integer> o1, Map.Entry<Student, Integer> o2 ) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		} );

		Map<Student, Integer> result = new LinkedHashMap<>();
		for (Map.Entry<Student, Integer> entry : list) {
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}
	
	public Integer getGrade() {
		for(Student student : getStudents()) {
			if(!student.getGrade().equals(0)) {
				return student.getGrade();
			}
		}
		return 0;
	}
	
	public Set<Student> getStudentsOnCourse(Course course) {
		Set<Student> students = new HashSet<>();
		for(Student student : getStudents()) {
			Map<Course, Integer> marks = student.getMarks(); 
			if(marks.containsKey(course)) {
				Integer mark = marks.get(course);
				if(mark.equals(-1)) {
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
	
	public void addStudent(Student student) {
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

	public void removeStudent(Student student) {
		this.students.remove(student);
	}
	
}
