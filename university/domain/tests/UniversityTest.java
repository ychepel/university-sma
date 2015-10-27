package university.domain.tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import university.domain.Course;
import university.domain.CourseSchedule;
import university.domain.Department;
import university.domain.Faculty;
import university.domain.Lecturer;
import university.domain.Student;
import university.domain.StudentGroup;
import university.domain.University;

public class UniversityTest {

	@Test
	public void testGetLabourHour() {
		University university = new University("ABC Unitversity");
		Lecturer lecturer1 = new Lecturer();
		Lecturer lecturer2 = new Lecturer();
		
		Department departmentA1 = new Department("A1 Department");
		
		Course courseA1C1 = new Course("A1C1 Course");
		CourseSchedule courseScheduleA1C1S = new CourseSchedule(courseA1C1, lecturer1);
		courseScheduleA1C1S.addTimetable(new GregorianCalendar(2015, Calendar.NOVEMBER, 1, 2, 3, 4));
		courseScheduleA1C1S.addTimetable(new GregorianCalendar(2016, Calendar.JANUARY, 1, 2, 3, 4));
		courseA1C1.add(courseScheduleA1C1S);
		departmentA1.addCourse(courseA1C1);
		
		Course courseA1C2 = new Course("A1C2 Course");
		CourseSchedule courseScheduleA1C2S = new CourseSchedule(courseA1C2, lecturer1);
		courseScheduleA1C2S.addTimetable(new GregorianCalendar(2015, Calendar.NOVEMBER, 2, 3, 4, 5));
		courseA1C2.add(courseScheduleA1C2S);
		
		CourseSchedule courseScheduleA1C2S2 = new CourseSchedule(courseA1C2, lecturer2);
		courseScheduleA1C2S2.addTimetable(new GregorianCalendar(2015, Calendar.NOVEMBER, 3, 4, 5, 6));
		courseA1C2.add(courseScheduleA1C2S2);
		
		departmentA1.addCourse(courseA1C2);
		
		Faculty facultyA = new Faculty("A Faculty");
		facultyA.addDepartment(departmentA1);
		university.addFaculty(facultyA);
		
		Integer expectedResult = 2;
		assertEquals(expectedResult, university.getLabourHour(lecturer1, new GregorianCalendar(2015, Calendar.NOVEMBER, 1)));
	}

	@Test
	public void testRemoveToFaculty() {
		Student student = new Student() {
			@Override
			protected Calendar getCalendar() {
				return new GregorianCalendar(2014, Calendar.SEPTEMBER, 1);
			}
		};
		student.setCompletionDate(new GregorianCalendar(2016, Calendar.MAY, 31));
		
		StudentGroup studentGroup = new StudentGroup("group 1");
		student.setStudentGroup(studentGroup);
		Faculty facultyA = new Faculty("A Faculty");
		facultyA.addStudentGroup(studentGroup);
		
		University university = new University("Unitversity");
		university.addFaculty(facultyA);
		
		Faculty facultyB = new Faculty("B Faculty");
		university.addFaculty(facultyB);
		
		assertEquals(1, facultyA.getStudentGroups().size());
		assertEquals(0, facultyB.getStudentGroups().size());
		
		university.removeToFaculty(student, facultyB);
		assertEquals(0, facultyA.getStudentGroups().size());
		assertEquals(1, facultyB.getStudentGroups().size());
		
	}
	
}
