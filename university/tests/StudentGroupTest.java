package university.tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import university.*;


public class StudentGroupTest {

	@Test
	public void testGetStudentsOnCourse() {
		StudentGroup studentGroup = new StudentGroup("Group-001");
		
		assertEquals(new HashSet<Student>(), studentGroup.getStudents());
		
		Student student1 = new Student();
		student1.setStudentGroup(studentGroup);
		Student student2 = new Student();
		student2.setStudentGroup(studentGroup);
		Student student3 = new Student();
		student3.setStudentGroup(studentGroup);
		
		
		Course courseA = new Course("A");
		Course courseB = new Course("B");
		
		student1.addMark(courseA, -1);
		student1.addMark(courseB, 5);
		student2.addMark(courseA, -1);
		student2.addMark(courseB, -1);
		student3.addMark(courseB, 10);
		
		Set<Student> expectedResult = new HashSet<>();
		expectedResult.add(student1);
		expectedResult.add(student2);
		assertEquals(expectedResult, studentGroup.getStudentsOnCourse(courseA));
		
		expectedResult = new HashSet<>();
		expectedResult.add(student2);
		assertEquals(expectedResult, studentGroup.getStudentsOnCourse(courseB));
	}
	
	@Test
	public void testGetGrade() {
		StudentGroup studentGroup = new StudentGroup("Group-001");
		Student student1 = new Student() {
			@Override
			protected Calendar getCalendar() {
				return new GregorianCalendar(2015, Calendar.NOVEMBER, 1);
			}
		};
		student1.setCompletionDate(new GregorianCalendar(2016, Calendar.MAY, 31));
		
		student1.setStudentGroup(studentGroup);
		assertEquals(Integer.valueOf(5) , studentGroup.getGrade());
	}
	
	@Test
	public void testGetSuccessRating() {
		StudentGroup studentGroup = new StudentGroup("Group-001");
		
		Student student1 = new Student();
		student1.setStudentGroup(studentGroup);
		Student student2 = new Student();
		student2.setStudentGroup(studentGroup);
		Student student3 = new Student();
		student3.setStudentGroup(studentGroup);
		
		Course courseA = new Course("A");
		Course courseB = new Course("B");
		
		student1.addMark(courseA, -1);
		student1.addMark(courseB, 8);
		student2.addMark(courseA, -1);
		student2.addMark(courseB, 5);
		student3.addMark(courseB, 10);
		
		Map<Student, Integer> expectedResult = new LinkedHashMap<>();
		assertEquals(expectedResult, studentGroup.getSuccessRating(courseA));
		
		expectedResult.put(student3, 10);
		expectedResult.put(student1, 8);
		expectedResult.put(student2, 5);

		Map<Student, Integer> result= studentGroup.getSuccessRating(courseB);
		fail("wrong order");
		assertEquals(expectedResult, result);
		
	}

}
