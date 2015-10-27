package university.dao.test;


import java.util.Calendar;
import java.util.GregorianCalendar;

import university.dao.*;
import university.domain.*;

public class DaoTest {
	
	static CourseDao courseDao = new CourseDao();
	static FacultyDao facultyDao = new FacultyDao();
	static DepartmentDao departmentDao = new DepartmentDao();
	static CourseScheduleDao courseScheduleDao = new CourseScheduleDao();
	static StudentDao studentDao = new StudentDao();
	static LecturerDao lecturerDao = new LecturerDao();
	static StudentGroupDao studentGroupDao = new StudentGroupDao();

	public static void printDBData() throws DaoException {
		for(Faculty faculty : facultyDao.getFaculties()) {
			System.out.println(faculty.getName().trim() + ":");
			for(Department department : faculty.getDepartments()) {
				System.out.println("   D# " + department.getName().trim() + ":");
				for(Lecturer lecturer : department.getLecturers()) {
					System.out.println("      L# " + lecturer.getFullName() 
					+ "(phone " + lecturer.getAddress().getPhone().trim() + ")");
				}
				for(Course course : department.getCourses()) {
					System.out.println("      C# " + course.getName().trim() + " - "  + course.getGrade() + ":");
					for(CourseSchedule courseSchedule : course.getCourseSchedules()) {
						System.out.println("         L# " + courseSchedule.getLecturer().getFullName() 
								+ "(" + courseSchedule.getLecturer().getAddress().getStreet().trim() + " Street)");
						for(Calendar calendar : courseSchedule.getTimetables()) {
							System.out.println("         T# " + calendar.getTime());
						}
						for(StudentGroup studentGroup : courseSchedule.getStudentGroups()) {
							System.out.println("         G# " + studentGroup.getName().trim() + ":");
							studentGroup.setStudents(studentDao.getStudentsByGroup(studentGroup));
							for(Student student : studentGroup.getStudents()) {
								System.out.println("             S# " + student.getFullName() 
									+ " (" + student.getBirthDate() + ", grade " + student.getGrade() 
									+ ", " + student.getAddress().getCity().trim() 
									+ ", average mark - " + student.getAverageMark() + ")");
							}
						}
					}
				}	
			}
			for(StudentGroup studentGroup : faculty.getStudentGroups()) {
				System.out.println("   G# " + studentGroup.getName().trim() + ":");
				studentGroup.setStudents(studentDao.getStudentsByGroup(studentGroup));
				for(Student student : studentGroup.getStudents()) {
					System.out.println("       S# " + student.getFullName() 
						+ " (" + student.getBirthDate() + ", grade " + student.getGrade() 
						+ ", " + student.getAddress().getCity().trim() 
						+ ", average mark - " + student.getAverageMark() + ")");
				}
			}
		}
	}
	
	public static void main (String[] args) throws DaoException {
		
		printDBData();
	
		Long maxId = studentDao.getMaxStudentId();
		Student student = new Student(maxId+1);
		student.setStudentCount(maxId+1);
		student.setBirthDate((new GregorianCalendar(1996, Calendar.MAY, 2)).getTime());
		student.setEntranceDate(new GregorianCalendar(2012, Calendar.SEPTEMBER, 1));
		student.setFirstName("Petr");
		student.setLastName("Petrov");
		student.setPatronymicName("Petrovich");
		student.setGender('m');
		student.setGovernmentFinanced(false);
		student.setNationality("ua");
		student.setPassport("HR5743");
		student.setSchoolGraduateSertificate("dhsajfhkdsa fda");
		
		Address address = new Address();
		address.setCity("Odessa");
		address.setEmail("c@c.com");
		address.setFlat(34);
		address.setHouse("46");
		address.setPhone("5435-543-54");
		address.setProvince("Odesska");
		address.setStreet("Kutuzova");
		
		student.setAddress(address);
		
		Faculty facultyA = new Faculty("AAA faculty");
		facultyA.addStudent(student);
		
		Department departmentA = new Department("AAAA department");
		facultyA.addDepartment(departmentA);
		
		Course courseA = new Course("Course A");
		Course courseB = new Course("Course B");
		Course courseC = new Course("Course C");
		
		departmentA.addCourse(courseA);
		courseA.setGrade(2);
		departmentA.addCourse(courseB);
		courseB.setGrade(2);
		departmentA.addCourse(courseC);
		courseA.setGrade(4);
		
		student.addMark(courseA, 34);
		student.addMark(courseB, 0);
		student.addMark(courseC, -1);
		
		Lecturer lecturer = new Lecturer();
		lecturer.setBirthDate((new GregorianCalendar(1966, Calendar.APRIL, 22)).getTime());
		lecturer.setCurrentPosition("Professor");
		lecturer.setFirstName("Gadya");
		lecturer.setGender('f');
		lecturer.setLastName("Hrenova");
		lecturer.setNationality("kz");
		lecturer.setPassport("GT47382");
		lecturer.setPatronymicName("Petrovich");
		lecturer.setScienceDegree("doctor");
		
		Address addressL = new Address();
		addressL.setCity("Kiev");
		addressL.setEmail("cas@cs.com");
		addressL.setFlat(3);
		addressL.setHouse("4a");
		addressL.setPhone("5435-543-54");
		addressL.setProvince("Kievska");
		addressL.setStreet("Getmana");
		lecturer.setAddress(addressL);
		
		CourseSchedule courseScheduleA = new CourseSchedule(courseA, lecturer);
		courseA.add(courseScheduleA);
		courseScheduleA.addStudentGroup(student.getStudentGroup());
		courseScheduleA.addTimetable(new GregorianCalendar(1966, Calendar.OCTOBER, 25, 10, 55, 00));
		courseScheduleA.addTimetable(new GregorianCalendar(1966, Calendar.OCTOBER, 26, 10, 55, 00));
		courseScheduleA.addTimetable(new GregorianCalendar(1966, Calendar.OCTOBER, 27, 11, 55, 00));
		
		System.out.println("********************************");
		facultyA = facultyDao.createFaculty(facultyA);
		StudentGroup studentGroup = studentGroupDao.createStudentGroup(student.getStudentGroup(), facultyA);
		student.setStudentGroup(studentGroup);
		studentDao.createStudent(student);
		departmentA = departmentDao.createDepartment(departmentA, facultyA);
		courseA = courseDao.createCourse(courseA, departmentA);
		courseB = courseDao.createCourse(courseB, departmentA);
		courseC = courseDao.createCourse(courseC, departmentA);
		lecturer = lecturerDao.createLecturer(lecturer, departmentA);
		courseScheduleA.setLecturer(lecturer);
		courseScheduleA = courseScheduleDao.createCourseSchedule(courseScheduleA, courseA);
		
		printDBData();
		
		System.out.println("********************************");
		studentDao.dropStudentById(student.getStudentId());
		facultyDao.dropFacultyById(facultyA.getId());
		departmentDao.dropDepartmentById(departmentA.getId());
		courseDao.dropCourseById(courseA.getId());
		courseDao.dropCourseById(courseB.getId());
		courseDao.dropCourseById(courseC.getId());
		courseScheduleDao.dropCourseScheduleById(courseScheduleA.getId());
		studentGroupDao.dropStudentGroupById(student.getStudentGroup().getId());
		lecturerDao.dropLecturerById(lecturer.getLecturerId());
		printDBData();
		
	}
}
