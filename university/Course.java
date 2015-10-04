package university;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Course {
	private String name;
	private int grade;
	private Boolean obligatory;
	private Set<CourseSchedule> courseSchedules;
	
	public void addGroupSchedule(Lecturer person, StudentGroup studentGroup) {
		Boolean scheduleExist = false;
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			if(courseSchedule.getLecturer().equals(person)) {
				courseSchedule.add(studentGroup);
				scheduleExist = true;
				break;
			}
			if(!scheduleExist) {
				CourseSchedule newCourseSchedule = new CourseSchedule(this, person);
				newCourseSchedule.add(studentGroup);
				this.add(newCourseSchedule);
			}
		}
	}
	
	public Set<CourseSchedule> getSchedule(Lecturer person) {
		Set<CourseSchedule> schedule = new HashSet<CourseSchedule>();
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			if(courseSchedule.getLecturer().equals(person)) {
				schedule.add(courseSchedule);
			}
		}
		return schedule;
	}
	
	public Set<CourseSchedule> getSchedule(StudentGroup studentGroup) {
		Set<CourseSchedule> schedule = new HashSet<CourseSchedule>();
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			if(courseSchedule.getStudentGroups().contains(studentGroup)) {
				schedule.add(courseSchedule);
			}
		}
		return schedule;
	}
	
	public Boolean isStudentGroupScheduled(StudentGroup studentGroup) {
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			if(courseSchedule.getStudentGroups().contains(studentGroup)) {
				return true;
			}
		}
		return false;
	}
	
	public void excludeStudent(Student student) {
		for(Iterator<CourseSchedule> iterator = courseSchedules.iterator(); iterator.hasNext(); ) {
			CourseSchedule courseSchedule = iterator.next();
			courseSchedule.excludeStudent(student);
			if(courseSchedule.getStudentGroups().size() == 0) {
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
}
