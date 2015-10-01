package university;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CourseSchedule {
	private Course course;
	private Lecturer lecturer;
	private Set<StudentGroup> studentCourseGroups;
	private Set<Calendar> timetables;
	
	public CourseSchedule(Course course, Lecturer lecturer) {
		this.course = course;
		this.lecturer = lecturer;
		this.studentCourseGroups = new HashSet<StudentGroup>();
		this.timetables = new HashSet<Calendar>();
	}
	
	public void addToCourseGroup(StudentGroup studentGroup) {
		studentCourseGroups.add(studentGroup);
	}
	
	public void removeFromCourseGroup(StudentGroup studentGroup) {
		studentCourseGroups.remove(studentGroup);
	}
	
	public void addToTimetables(Calendar calendar) {
		timetables.add(calendar);
	}
	
	public void removeFromTimetables(Calendar calendar) {
		timetables.remove(calendar);
	}
	
	public Lecturer getLecturer() {
		return lecturer;
	}
	
}
