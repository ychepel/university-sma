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
				courseSchedule.addToCourseGroup(studentGroup);
				scheduleExist = true;
				break;
			}
			if(!scheduleExist) {
				CourseSchedule newCourseSchedule = new CourseSchedule(this, person);
				newCourseSchedule.addToCourseGroup(studentGroup);
				this.addCourseSchedule(newCourseSchedule);
			}
		}
	}
	
	public Set<CourseSchedule> getCourseSchedule(Lecturer person) {
		Set<CourseSchedule> schedule = new HashSet<CourseSchedule>();
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			if(courseSchedule.getLecturer().equals(person)) {
				schedule.add(courseSchedule);
			}
		}
		return schedule;
	}
	
	public Set<CourseSchedule> getCourseSchedule(StudentGroup studentGroup) {
		Set<CourseSchedule> schedule = new HashSet<CourseSchedule>();
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			if(courseSchedule.getStudentCourseGroups().contains(studentGroup)) {
				schedule.add(courseSchedule);
			}
		}
		return schedule;
	}
	
	public Boolean isStudentGroupScheduled(StudentGroup studentGroup) {
		for(CourseSchedule courseSchedule : getCourseSchedules()) {
			if(courseSchedule.getStudentCourseGroups().contains(studentGroup)) {
				return true;
			}
		}
		return false;
	}
	
	public void excludeStudent(Student student) {
		for(Iterator<CourseSchedule> iterator = courseSchedules.iterator(); iterator.hasNext(); ) {
			CourseSchedule courseSchedule = iterator.next();
			courseSchedule.excludeStudent(student);
			if(courseSchedule.getStudentCourseGroups().size() == 0) {
				courseSchedules.remove(courseSchedule);
			}
		}
	}
	
	public Set<CourseSchedule> getCourseSchedules() {
		return courseSchedules;
	}
	
	public void addCourseSchedule(CourseSchedule courseSchedule) {
		courseSchedules.add(courseSchedule);
	}
}
