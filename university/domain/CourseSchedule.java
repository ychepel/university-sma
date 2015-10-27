package university.domain;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class CourseSchedule {
	private Course course;
	private Lecturer lecturer;
	private Set<StudentGroup> studentGroups;
	private Set<Calendar> timetables;
	private Integer id;
	
	public CourseSchedule(Course course, Lecturer lecturer) {
		this.course = course;
		this.lecturer = lecturer;
		this.studentGroups = new HashSet<StudentGroup>();
		this.timetables = new HashSet<Calendar>();
	}
	
	public void addStudentGroup(StudentGroup studentGroup) {
		studentGroups.add(studentGroup);
	}
	
	public void remove(StudentGroup studentGroup) {
		studentGroups.remove(studentGroup);
	}
	
	public void addTimetable(Calendar calendar) {
		timetables.add(calendar);
	}
	
	public void remove(Calendar calendar) {
		timetables.remove(calendar);
	}
	
	public Lecturer getLecturer() {
		return lecturer;
	}
	
	public Set<StudentGroup> getStudentGroups() {
		return studentGroups;
	}

	public Set<Calendar> getTimetables() {
		return timetables;
	}
	
	public Course getCourse() {
		return course;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setStudentGroups(Set<StudentGroup> studentGroups) {
		this.studentGroups = studentGroups;
	}

	public void setTimetables(Set<Calendar> timetables) {
		this.timetables = timetables;
	}
	public void setLecturer(Lecturer lecturer) {
		this.lecturer = lecturer;
	}
	
}
