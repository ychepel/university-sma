package university.domain;

import java.util.HashSet;
import java.util.Set;

public class Department {
	private String name;
	private Set<Lecturer> lecturers;
	private Set<Course> courses;
	private Integer id;
	
	public Set<CourseSchedule> getSchedule(Lecturer lecturer) {
		Set<CourseSchedule> schedule = new HashSet<>();
		for(Course course : getCourses()) {
			Set<CourseSchedule> courseSchedules = course.getScheduleByLecturer(lecturer);
			schedule.addAll(courseSchedules);
		}
		return schedule;
	}
	
	public Boolean enrollStudent(Student student, Course course) {
		if(!getCourses().contains(course)) return false;
		
		StudentGroup studentGroup = student.getStudentGroup();
		if(course.isStudentGroupScheduled(studentGroup)) {
			student.addMark(course, -1);
			return true;
		}
		return false;
	}
	
	public void addLecturer(Lecturer lecturer) {
		if(!lecturers.contains(lecturer)) {
			lecturers.add(lecturer);
		}
	}
	
	public void addCourse(Course course) {
		if(!courses.contains(course)) {
			courses.add(course);
		}
	}
	
	public void removeLecturer(Lecturer lecturer) {
		lecturers.remove(lecturer);
	}
	
	public void removeCourse(Course course) {
		courses.remove(course);
	}
	
	public Set<Course> getCourses() {
		return courses;
	}

	public Set<Lecturer> getLecturers() {
		return lecturers;
	}

	public void excludeStudentGroup(StudentGroup studentGroup) {
		for(Course course : getCourses()) {
			course.excludeStudent(studentGroup);
		}
	}
	
	public Department(String name) {
		this.name = name;
		lecturers = new HashSet<>();
		courses = new HashSet<>();
	}
	
	public String getName() {
		return name;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
	public void setLecturers(Set<Lecturer> lecturers) {
		this.lecturers = lecturers;
	}
}
