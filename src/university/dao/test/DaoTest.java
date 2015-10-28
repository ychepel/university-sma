package university.dao.test;


import java.util.Calendar;
import java.util.GregorianCalendar;

import university.dao.*;
import university.domain.*;

public class DaoTest {
	
	private static University university = new University("CAMBREDGE");

	public static void printDBData() throws DaoException, DomainException {
		
		for(Faculty faculty : university.getFaculties()) {
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
//			for(StudentGroup studentGroup : faculty.getStudentGroups()) {
//				System.out.println("   G# " + studentGroup.getName().trim() + ":");
//				for(Student student : studentGroup.getStudents()) {
//					System.out.println("       S# " + student.getFullName() 
//						+ " (" + student.getBirthDate() + ", grade " + student.getGrade() 
//						+ ", " + student.getAddress().getCity().trim() 
//						+ ", average mark - " + student.getAverageMark() + ")");
//				}
//			}
		}
	}
	
	public static void main (String[] args) throws DaoException, DomainException {
		
		//printDBData();
		
		Faculty testFaculty = university.createFaculty("Testing Faculty");
		Department testDepartment = testFaculty.createDepartment("Testing Department");
		Course testCourse1 = new Course("Testing Course 1");
		Course testCourse2 = new Course("Testing Course 2");
		Course testCourse3 = new Course("Testing Course 3");
		testDepartment.addCourse(testCourse1);
		testDepartment.addCourse(testCourse2);
		testDepartment.addCourse(testCourse3);
		testCourse1.setGrade(1);
		testCourse2.setGrade(2);
		testCourse3.setGrade(3);
		
		Address testAddress = new Address();
		testAddress.setCity("Odessa");
		testAddress.setEmail("aaa@c.com");
		testAddress.setFlat(3);
		testAddress.setHouse("4");
		testAddress.setPhone("5-432-43-54");
		testAddress.setProvince("Odesska");
		testAddress.setStreet("Nekrasova");
		
		Long maxId = (new StudentDao()).getMaxStudentId();
		Student.setStudentCount(maxId+1);
		Student testStudent = new Student();
		testStudent.setBirthDate((new GregorianCalendar(1993, Calendar.JUNE, 4)).getTime());
		testStudent.setEntranceDate(new GregorianCalendar(2013, Calendar.SEPTEMBER, 1));
		testStudent.setFirstName("Oleg");
		testStudent.setLastName("Olegov");
		testStudent.setPatronymicName("Olegovich");
		testStudent.setGender('m');
		testStudent.setGovernmentFinanced(false);
		testStudent.setNationality("ua");
		testStudent.setPassport("DF53743");
		testStudent.setSchoolGraduateSertificate("423543543");
		testStudent.setAddress(testAddress);
		
		testFaculty.addStudent(testStudent);
		testDepartment.enrollStudent(testStudent, testCourse1);
		testDepartment.enrollStudent(testStudent, testCourse2);
		testDepartment.enrollStudent(testStudent, testCourse3);
		
		testStudent.addMark(testCourse1, 22);
		testStudent.addMark(testCourse2, 0);
		testStudent.addMark(testCourse3, -1);
	
		System.out.println("************************************************");
		printDBData();
		
		testDepartment.removeCourse(testCourse1);
		testDepartment.removeCourse(testCourse2);
		testDepartment.removeCourse(testCourse3);
		testFaculty.removeDepartment(testDepartment);
		university.removeFaculty(testFaculty);
		(new StudentDao()).dropStudentById(testStudent.getStudentId());
		
		System.out.println("************************************************");
		printDBData();
		
		
//		Lecturer lecturer = new Lecturer();
//		lecturer.setBirthDate((new GregorianCalendar(1976, Calendar.MAY, 13)).getTime());
//		lecturer.setCurrentPosition("Professor");
//		lecturer.setFirstName("Mariya");
//		lecturer.setGender('f');
//		lecturer.setLastName("Kozlova");
//		lecturer.setNationality("md");
//		lecturer.setPassport("FD47382");
//		lecturer.setPatronymicName("Nikolaevna");
//		lecturer.setScienceDegree("doctor");
//		
//		Address addressL = new Address();
//		addressL.setCity("Kiev");
//		addressL.setEmail("adds@cs.net");
//		addressL.setFlat(3);
//		addressL.setHouse("4a");
//		addressL.setPhone("5435-543-54");
//		addressL.setProvince("Kievska");
//		addressL.setStreet("Getmana");
//		lecturer.setAddress(addressL);
//		
//		CourseSchedule courseScheduleA = new CourseSchedule(courseA, lecturer);
//		courseA.add(courseScheduleA);
//		courseScheduleA.addStudentGroup(student.getStudentGroup());
//		courseScheduleA.addTimetable(new GregorianCalendar(1966, Calendar.OCTOBER, 25, 10, 55, 00));
//		courseScheduleA.addTimetable(new GregorianCalendar(1966, Calendar.OCTOBER, 26, 10, 55, 00));
//		courseScheduleA.addTimetable(new GregorianCalendar(1966, Calendar.OCTOBER, 27, 11, 55, 00));
//		
//		System.out.println("********************************");
//		facultyA = facultyDao.createFaculty(facultyA);
//		StudentGroup studentGroup = studentGroupDao.createStudentGroup(student.getStudentGroup(), facultyA);
//		student.setStudentGroup(studentGroup);
//		studentDao.createStudent(student);
//		departmentA = departmentDao.createDepartment(departmentA, facultyA);
//		courseA = courseDao.createCourse(courseA, departmentA);
//		courseB = courseDao.createCourse(courseB, departmentA);
//		courseC = courseDao.createCourse(courseC, departmentA);
//		lecturer = lecturerDao.createLecturer(lecturer, departmentA);
//		courseScheduleA.setLecturer(lecturer);
//		courseScheduleA = courseScheduleDao.createCourseSchedule(courseScheduleA, courseA);
//		
//		printDBData();
//		
//		System.out.println("********************************");
//		studentDao.dropStudentById(student.getStudentId());
//		facultyDao.dropFacultyById(facultyA.getId());
//		departmentDao.dropDepartmentById(departmentA.getId());
//		courseDao.dropCourseById(courseA.getId());
//		courseDao.dropCourseById(courseB.getId());
//		courseDao.dropCourseById(courseC.getId());
//		courseScheduleDao.dropCourseScheduleById(courseScheduleA.getId());
//		studentGroupDao.dropStudentGroupById(student.getStudentGroup().getId());
//		lecturerDao.dropLecturerById(lecturer.getLecturerId());
//		printDBData();
		
	}
}
