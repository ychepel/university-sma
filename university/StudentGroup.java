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
				if(studentMarks.get(course) == -1) continue;
				result.put(student, studentMarks.get(course));
			}
		}
		return sortByValue(result);
	}
	
	private <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
		List<Map.Entry<K, V>> list =  new LinkedList<>(map.entrySet());
		Collections.sort( list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		} );

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}
	
	public Integer getGrade() {
		for(Student student : getStudents()) {
			if(student.getGrade() != 0) {
				return student.getGrade();
			}
		}
		return 0;
	}
	
	public Set<Student> getStudentsOnCourse(Course course) {
		Set<Student> students = new HashSet<>();
		for(Student student : getStudents()) {
			if(student.getMarks().containsKey(course)) {
				if(student.getMarks().get(course) == -1) {
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
	
	public void add(Student student) {
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

	public void remove(Student student) {
		this.students.remove(student);
	}
	
}
