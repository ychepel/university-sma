package university;

import java.util.HashSet;
import java.util.Set;

public class Department {
	private String name;
	private Set<Lecturer> lecturers;
	private Set<Course> courses;
	
	public Set<CourseSchedule> getSchedule(Lecturer lecturer) {
		Set<CourseSchedule> schedule = new HashSet<>();
		for(Course course : getCourses()) {
				schedule.addAll(course.getSchedule(lecturer));
		}
		return schedule;
	}
	
	public Boolean enrollStudent(Student student, Course course) {
		if(!getCourses().contains(course)) return false;
		if(course.isStudentGroupScheduled(student.getStudentGroup())) {
			student.addMark(course, -1);
			return true;
		}
		return false;
	}
	
	public void add(Lecturer lecturer) {
		if(!lecturers.contains(lecturer)) {
			lecturers.add(lecturer);
		}
	}
	
	public void add(Course course) {
		if(!courses.contains(course)) {
			courses.add(course);
		}
	}
	
	public Set<Course> getCourses() {
		return courses;
	}

	public Set<Lecturer> getLecturers() {
		return lecturers;
	}

	public void excludeStudent(Student student) {
		for(Course course : getCourses()) {
			course.excludeStudent(student);
		}
	}
	
	public Department(String name) {
		this.name = name;
		lecturers = new HashSet<>();
		courses = new HashSet<>();
	}
}
