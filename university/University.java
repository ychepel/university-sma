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
			Set<Calendar> timetables = courseSchedule.getTimetables();
			for(Calendar timetable : timetables) {
				if(timetable.get(Calendar.YEAR) != period.get(Calendar.YEAR)) continue;
				if(timetable.get(Calendar.MONTH) != period.get(Calendar.MONTH)) continue;
				result++;
			}
		}
		return result;
	}
	
	public Map<Faculty, Set<CourseSchedule>> getSchedule() {
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
	
	public void removeToFaculty(Student student, Faculty newFaculty) {
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

		newFaculty.add(student);
	}
	
	public University(String name) {
		this.name = name;
		this.address = new Address();
		this.faculties = new HashSet<>();
	}
	
	public void add(Faculty faculty) {
		this.faculties.add(faculty);
	}
	
	public void remove(Faculty faculty) {
		this.faculties.remove(faculty);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<Faculty> getFaculties() {
		return faculties;
	}
	
	
	
}
