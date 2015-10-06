package university.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import university.*;

public class DepartmentTest {

	@Test
	public void testEnrollStudent() {
		Course courseA = new Course("course A");
		Course courseB = new Course("course B");
		Department department = new Department("department B");
		department.add(courseA);
		
		Student student1 = new Student();
		StudentGroup studentGroup = new StudentGroup("group - 11");
		student1.setStudentGroup(studentGroup);
		studentGroup.addToGroup(student1);
		courseA.addCourseSchedule(new Lecturer() , studentGroup);
		
		assertTrue(department.enrollStudent(student1, courseA));
		assertFalse(department.enrollStudent(student1, courseB));
		assertEquals(Integer.valueOf(-1), student1.getMark(courseA));
		
		Student student2 = new Student();
		assertFalse(department.enrollStudent(student2, courseA));
	}

}
