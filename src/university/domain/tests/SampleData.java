package university.domain.tests;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import university.dao.*;
import university.domain.*;

public class SampleData {
	
	private University testUniversity;
	private Faculty testFaculty;
	private Department testDepartment;
	private Course testCourse1;
	private Course testCourse2;
	private Course testCourse3;
	private Course testCourse4;
	private Address testStudentAddress;
	private Student testStudent;
	private Lecturer testLecturer;
	private Address addressTestLecturer;
	private CourseSchedule testCourseSchedule;

	private static Logger log = Logger.getLogger(SampleData.class);
	
	public void printDBData() throws DaoException, DomainException {
		
		for(Faculty faculty : testUniversity.getFaculties()) {
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
			for(StudentGroup studentGroup : faculty.getStudentGroups()) {
				System.out.println("   G# " + studentGroup.getName().trim() + ":");
				for(Student student : studentGroup.getStudents()) {
					System.out.println("       S# " + student.getFullName() 
						+ " (" + student.getBirthDate() + ", grade " + student.getGrade() 
						+ ", " + student.getAddress().getCity().trim() 
						+ ", average mark - " + student.getAverageMark() + ")");
				}
			}
		}
	}
	
	public void fillDBWithSampleData() throws DaoException, DomainException {
		testUniversity = new University("Test.University");
		testFaculty = testUniversity.createFaculty("Testing Faculty");
		testDepartment = testFaculty.createDepartment("Testing Department");
		
		testCourse1 = new Course("Testing Course 1");
		testCourse2 = new Course("Testing Course 2");
		testCourse3 = new Course("Testing Course 3");
		testCourse4 = new Course("Testing Course 4");
		testDepartment.addCourse(testCourse1);
		testDepartment.addCourse(testCourse2);
		testDepartment.addCourse(testCourse3);
		testCourse1.setGrade(1);
		testCourse2.setGrade(2);
		testCourse3.setGrade(3);
		testCourse4.setGrade(4);
		
		testStudentAddress = new Address();
		testStudentAddress.setCity("Odessa");
		testStudentAddress.setEmail("aaa@c.com");
		testStudentAddress.setFlat(3);
		testStudentAddress.setHouse("4");
		testStudentAddress.setPhone("5-432-43-54");
		testStudentAddress.setProvince("Odesska");
		testStudentAddress.setStreet("Nekrasova");
		
		Long maxId = (new StudentDao()).getMaxStudentId();
		Student.setStudentCount(maxId);
		testStudent = new Student();
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
		testStudent.setAddress(testStudentAddress);
		
		testFaculty.addStudent(testStudent);
		testDepartment.enrollStudent(testStudent, testCourse1);
		testDepartment.enrollStudent(testStudent, testCourse2);
		
		testStudent.addMark(testCourse1, 22);
		testStudent.addMark(testCourse2, 0);

		testLecturer = new Lecturer();
		testLecturer.setBirthDate((new GregorianCalendar(1976, Calendar.MAY, 13)).getTime());
		testLecturer.setCurrentPosition("Professor");
		testLecturer.setFirstName("Mariya");
		testLecturer.setGender('f');
		testLecturer.setLastName("Kozlova");
		testLecturer.setNationality("md");
		testLecturer.setPassport("FD47382");
		testLecturer.setPatronymicName("Nikolaevna");
		testLecturer.setScienceDegree("doctor");
		
		addressTestLecturer = new Address();
		addressTestLecturer.setCity("Kiev");
		addressTestLecturer.setEmail("adds@cs.net");
		addressTestLecturer.setFlat(3);
		addressTestLecturer.setHouse("4a");
		addressTestLecturer.setPhone("5435-543-54");
		addressTestLecturer.setProvince("Kievska");
		addressTestLecturer.setStreet("Getmana");
		testLecturer.setAddress(addressTestLecturer);
		testFaculty.addLecturer(testDepartment, testLecturer);
		
		testCourseSchedule = testCourse1.createCourseSchedule(testLecturer, testStudent.getStudentGroup());
		testCourseSchedule.addTimetable(new GregorianCalendar(1966, Calendar.OCTOBER, 25, 10, 55, 00));
		testCourseSchedule.addTimetable(new GregorianCalendar(1966, Calendar.OCTOBER, 26, 10, 55, 00));
		testCourseSchedule.addTimetable(new GregorianCalendar(1966, Calendar.OCTOBER, 27, 11, 55, 00));
		
		log.debug("Sample data uploaded");
	}
	
	public void clearDBFromSampleData() throws DomainException {
		log.debug("Clearing sample data");
		testFaculty.removeStudent(testStudent);
		testDepartment.excludeStudentGroup(testStudent.getStudentGroup());
		testDepartment.removeCourse(testCourse1);
		testDepartment.removeCourse(testCourse2);
		testDepartment.removeCourse(testCourse3);
		testDepartment.removeLecturer(testLecturer);
		testFaculty.removeDepartment(testDepartment);
		testUniversity.removeFaculty(testFaculty);
	}

	public University getTestUniversity() {
		return testUniversity;
	}

	public Faculty getTestFaculty() {
		return testFaculty;
	}

	public Department getTestDepartment() {
		return testDepartment;
	}

	public Course getTestCourse1() {
		return testCourse1;
	}

	public Course getTestCourse2() {
		return testCourse2;
	}

	public Course getTestCourse3() {
		return testCourse3;
	}

	public Student getTestStudent() {
		return testStudent;
	}

	public Lecturer getTestLecturer() {
		return testLecturer;
	}

	public CourseSchedule getTestCourseSchedule() {
		return testCourseSchedule;
	}
	
	public Course getTestCourse4() {
		return testCourse4;
	}
	
}
