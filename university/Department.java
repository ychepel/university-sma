package university;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Department {
	private String name;
	private Set<Lecturer> lecturers;
	private Set<Course> courses;
	
	public Set<CourseSchedule> getLecturerSchedule(Lecturer person) {
		Set<CourseSchedule> schedule = new HashSet<>();
		for(Course course : getCourses()) {
				schedule.addAll(course.getLecturerCourseSchedule(person));
		}
		return schedule;
	}
	
	public Set<Course> getCourses() {
		return courses;
	}
}
