package university.domain;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import university.dao.AddressDao;
import university.dao.DaoException;
import university.dao.FacultyDao;

public class University {
	private String name;
	private Address address;
	
	private FacultyDao facultyDao;
	private AddressDao addressDao;
	
	public Integer getLabourHour(Lecturer lecturer, Calendar period) throws DomainException {
		Integer result = 0;
		Set<CourseSchedule> lecturerSchedule = new HashSet<>();
		for(Faculty faculty : getFaculties()) {
			Set<CourseSchedule> courseSchedules = faculty.getSchedule(lecturer); 
			lecturerSchedule.addAll(courseSchedules);
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
	
	public Map<Faculty, Set<CourseSchedule>> getSchedule() throws DomainException {
		Map<Faculty, Set<CourseSchedule>> schedule = new HashMap<>();
		for(Faculty faculty : getFaculties()) {
			Set<CourseSchedule> facultySchedule = new HashSet<>();
			for(Lecturer lecturer : faculty.getLecturers()) {
				Set<CourseSchedule> courseSchedules = faculty.getSchedule(lecturer);
				facultySchedule.addAll(courseSchedules);
			}
			schedule.put(faculty, facultySchedule);
		}
		return schedule;
	}
	
	public void removeStudentToFaculty(Student student, Faculty newFaculty) throws DomainException {
		StudentGroup oldStudentGroup = student.getStudentGroup();
		Faculty oldStudentFaculty = null;
		for(Faculty faculty : getFaculties()) {
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
	
	public University(String name) {
		this.name = name;
		
		this.facultyDao = new FacultyDao();
		this.addressDao = new AddressDao();
	}
	
	public Faculty createFaculty(String name) throws DomainException {
		Faculty faculty = new Faculty(name);
		try {
			faculty = facultyDao.createFaculty(faculty);
		}
		catch (DaoException e) {
			throw new DomainException("Cannnot create faculty", e); 
		}
		return faculty;
	}
	
	public void removeFaculty(Faculty faculty) throws DomainException {
		Integer id = faculty.getId();
		try {
			facultyDao.dropFacultyById(id);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot drop the faculty", e);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() throws DomainException{
		if(address == null) {
			try {
				this.address = addressDao.getAddressById(0L);
			}
			catch (DaoException e) {
				throw new DomainException("Cannot get university address", e);
			}
		}
		return address;
	}

	public void setAddress(Address address) throws DomainException {
		address.setId(0L);
		this.address = address;
		try {
			addressDao.updateAddress(address);
		}
		catch (DaoException e) {
			throw new DomainException("Cannot update university address", e);
		}
	}

	public Set<Faculty> getFaculties() throws DomainException {
		Set<Faculty> faculties = new HashSet<>();
		try {
			faculties = facultyDao.getFaculties();
		}
		catch (DaoException e) {
			throw new DomainException("Cannot get faculties from db", e);
		}
		return faculties;
	}

}
