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
		student.setBirthDate((new GregorianCalendar(1993, Calendar.JUNE, 4)).getTime());
		student.setEntranceDate(new GregorianCalendar(2013, Calendar.SEPTEMBER, 1));
		student.setFirstName("Oleg");
		student.setLastName("Olegov");
		student.setPatronymicName("Olegovich");
		student.setGender('m');
		student.setGovernmentFinanced(false);
		student.setNationality("ua");
		student.setPassport("DF53743");
		student.setSchoolGraduateSertificate("423543543");
		
		Address address = new Address();
		address.setCity("Odessa");
		address.setEmail("aaa@c.com");
		address.setFlat(3);
		address.setHouse("4");
		address.setPhone("5-432-43-54");
		address.setProvince("Odesska");
		address.setStreet("Nekrasova");
		
		student.setAddress(address);
		
		Faculty facultyA = new Faculty("B faculty");
		facultyA.addStudent(student);
		
		Department departmentA = new Department("BB department");
		facultyA.addDepartment(departmentA);
		
		Course courseA = new Course("Course B-A1");
		Course courseB = new Course("Course B-B1");
		Course courseC = new Course("Course B-C1");
		
		departmentA.addCourse(courseA);
		courseA.setGrade(1);
		departmentA.addCourse(courseB);
		courseB.setGrade(1);
		departmentA.addCourse(courseC);
		courseC.setGrade(3);
		
		student.addMark(courseA, 22);
		student.addMark(courseB, 0);
		student.addMark(courseC, -1);
		
		Lecturer lecturer = new Lecturer();
		lecturer.setBirthDate((new GregorianCalendar(1976, Calendar.MAY, 13)).getTime());
		lecturer.setCurrentPosition("Professor");
		lecturer.setFirstName("Mariya");
		lecturer.setGender('f');
		lecturer.setLastName("Kozlova");
		lecturer.setNationality("md");
		lecturer.setPassport("FD47382");
		lecturer.setPatronymicName("Nikolaevna");
		lecturer.setScienceDegree("doctor");
		
		Address addressL = new Address();
		addressL.setCity("Kiev");
		addressL.setEmail("adds@cs.net");
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
