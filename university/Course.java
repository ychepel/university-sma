package university;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Course {
	private String name;
	private int grade;
	private Boolean obligatory;
	private Set<CourseSchedule> courseSchedules;
	
	public void create(Lecturer lecturer, StudentGroup studentGroup) {
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			Lecturer courseLecturer = courseSchedule.getLecturer();
			if(courseLecturer.equals(lecturer)) {
				courseSchedule.add(studentGroup);
				return;
			}
		}
		
		CourseSchedule newCourseSchedule = new CourseSchedule(this, lecturer);
		newCourseSchedule.add(studentGroup);
		this.add(newCourseSchedule);
	}
	
	public Set<CourseSchedule> getScheduleByLecturer(Lecturer person) {
		Set<CourseSchedule> schedule = new HashSet<CourseSchedule>();
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			Lecturer lecturer = courseSchedule.getLecturer();
			if(lecturer.equals(person)) {
				schedule.add(courseSchedule);
			}
		}
		return schedule;
	}
	
	public Set<CourseSchedule> getScheduleByStudentGroup(StudentGroup studentGroup) {
		Set<CourseSchedule> schedule = new HashSet<CourseSchedule>();
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			Set<StudentGroup> studentGroups = courseSchedule.getStudentGroups();
			if(studentGroups.contains(studentGroup)) {
				schedule.add(courseSchedule);
			}
		}
		return schedule;
	}
	
	public Boolean isStudentGroupScheduled(StudentGroup studentGroup) {
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			Set<StudentGroup> studentGroups = courseSchedule.getStudentGroups();
			if(studentGroups.contains(studentGroup)) {
				return true;
			}
		}
		return false;
	}
	
	public void excludeStudent(Student student) {
		Iterator<CourseSchedule> iterator = courseSchedules.iterator();
		while(iterator.hasNext()) {
			CourseSchedule courseSchedule = iterator.next();
			courseSchedule.excludeStudent(student);
			Set<StudentGroup> studentGroups = courseSchedule.getStudentGroups(); 
			if(studentGroups.size() == 0) {
				courseSchedules.remove(courseSchedule);
			}
		}
	}
	
	public Set<CourseSchedule> getCourseSchedules() {
		return courseSchedules;
	}
	
	public void add(CourseSchedule courseSchedule) {
		courseSchedules.add(courseSchedule);
	}
	
	public void setCourseSchedules(Set<CourseSchedule> courseSchedules) {
		this.courseSchedules = courseSchedules;
	}
	
	public Course(String name) {
		this.name = name;
		courseSchedules =  new HashSet<>();
	}

	public String getName() {
		return name;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public Boolean getObligatory() {
		return obligatory;
	}

	public void setObligatory(Boolean obligatory) {
		this.obligatory = obligatory;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
