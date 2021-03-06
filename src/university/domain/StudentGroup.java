package university.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import university.dao.DaoException;
import university.dao.StudentDao;

public class StudentGroup {
	private String name;
	private Integer id;
	
	private StudentDao studentDao;
	
	private static Logger log = Logger.getLogger(StudentGroup.class);
	
	public Map<Student, Integer> getSuccessRating(Course course) throws DomainException {
		Map<Student, Integer> result = new HashMap<>();
		Integer courseId = course.getId();
		for(Student student : getStudents()) {
			Map<Course, Integer> studentMarks = student.getMarks();
			for(Map.Entry<Course, Integer> entry : studentMarks.entrySet()) {
				Course markedCourse = entry.getKey();
				Integer markedCourseId = markedCourse.getId();
				if(markedCourseId.equals(courseId)) {
					Integer mark = entry.getValue();
					if(mark.equals(-1)) break;
					result.put(student, mark);
					break;
				}
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
	
	public Integer getGrade() throws DomainException {
		for(Student student : getStudents()) {
			if(!student.getGrade().equals(0)) {
				return student.getGrade();
			}
		}
		return 0;
	}
	
	public Set<Student> getStudentsOnCourse(Course course) throws DomainException {
		Integer courseId = course.getId();
		Set<Student> students = new HashSet<>();
		for(Student student : getStudents()) {
			Map<Course, Integer> marks = student.getMarks(); 
			for(Map.Entry<Course, Integer> entry : marks.entrySet()) {
				Course markedCourse = entry.getKey();
				Integer markedCourseId = markedCourse.getId();
				if(markedCourseId.equals(courseId)) {
					Integer mark = entry.getValue();
					if(mark.equals(-1)) {
						students.add(student);
					}
					break;
				}
			}
		}
		return students;
	}
	
	public StudentGroup(String name) {
		log.info("Create new Student Group '" + name + "'");
		this.name = name;

		studentDao = new StudentDao();
	}
	
	protected void addStudent(Student student) throws DomainException {
		student.setStudentGroup(this);
	}

	public Set<Student> getStudents() throws DomainException {
		Set<Student> result = new HashSet<>();
		try {
			result = studentDao.getStudentsByGroup(this);
		}
		catch (DaoException e) {
			throw new DomainException ("Cannot receive Students for Student Group", e);
		}
		return result;
	}
	
	protected void removeStudent(Student student) throws DomainException {
		log.info("Remove Student '" + student.getFullName() + "' + (id=" + student.getStudentId() + ")");
		try {
			studentDao.deleteStudent(student);
		}
		catch (DaoException e) {
			throw new DomainException ("Cannot remove Student", e);
		}
	}

	public String getName() {
		return name;
	}
	
	public Integer getStudentQuantity() throws DomainException {
		Set<Student> students = getStudents();
		return students.size();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
