package university;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class University {
	private String name;
	private Address address;
	private Set<Faculty> faculties;
	
	public Integer getLabourHour(Lecturer person, Date month) {
		throw new UnsupportedOperationException();
	}
	
	public Map<Faculty, CourseSchedule> getUniversitySchedule() {
		throw new UnsupportedOperationException();
	}
	
	public void changeStudentFaculty(Student person, Faculty newFaculty) {
		throw new UnsupportedOperationException();
	}
}
