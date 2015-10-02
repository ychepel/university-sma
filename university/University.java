package university;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class University {
	private String name;
	private Address address;
	private Set<Faculty> faculties;
	
	public Integer getLabourHour(Lecturer lecturer, Calendar period) {
		Integer result = 0;
		Set<CourseSchedule> lecturerSchedule = new HashSet<>();
		for(Faculty faculty : faculties) {
			lecturerSchedule.addAll(faculty.getSchedule(lecturer));
		}
		for(CourseSchedule courseSchedule : lecturerSchedule) {
			Set<Calendar> timetables = new HashSet<>();
			for(Calendar timetable : timetables) {
				if(timetable.get(Calendar.YEAR) != period.get(Calendar.YEAR)) continue;
				if(timetable.get(Calendar.MONTH) != period.get(Calendar.MONTH)) continue;
				result++;
			}
		}
		return result;
	}
	
	public Map<Faculty, Set<CourseSchedule>> getUniversitySchedule() {
		Map<Faculty, Set<CourseSchedule>> schedule = new HashMap<>();
		for(Faculty faculty : faculties) {
			Set<CourseSchedule> facultySchedule = new HashSet<>();
			for(Lecturer lecturer : faculty.getLecturers()) {
				facultySchedule.addAll(faculty.getSchedule(lecturer));
			}
			schedule.put(faculty, facultySchedule);
		}
		return schedule;
	}
	
	public void changeStudentFaculty(Student student, Faculty newFaculty) {
		StudentGroup oldStudentGroup = student.getStudentGroup();
		Faculty oldStudentFaculty = null;
		for(Faculty faculty : faculties) {
			Set<StudentGroup> facultyStudentGroups = faculty.getStudentGroups();
			if(facultyStudentGroups.contains(oldStudentGroup)) {
				oldStudentFaculty = faculty;
				break;
			}
		}
		if(oldStudentFaculty != null) {
			oldStudentFaculty.removeStudent(student);
		}
		newFaculty.addStudent(student);
	}
	
}
