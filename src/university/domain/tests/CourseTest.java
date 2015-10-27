package university.domain.tests;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import university.domain.*;

public class CourseTest {

	@Test
	public void testAddGroupSchedule() {
		Lecturer lecturer1 = new Lecturer();
		Lecturer lecturer2 = new Lecturer();
		Course course = new Course("A");
		StudentGroup studentGroup101 = new StudentGroup("gr-101");
		StudentGroup studentGroup102 = new StudentGroup("gr-102");
		
		assertEquals(new HashSet<>(), course.getCourseSchedules());
		
		course.create(lecturer1, studentGroup101);
		assertEquals(1, course.getCourseSchedules().size());
		
		course.create(lecturer1, studentGroup102);
		assertEquals(1, course.getCourseSchedules().size());
		
		course.create(lecturer2, studentGroup102);
		assertEquals(2, course.getCourseSchedules().size());
	}

	@Test
	public void testIsStudentGroupScheduled() {
		Course course = new Course("A");
		Lecturer lecturer = new Lecturer();
		StudentGroup studentGroup = new StudentGroup("gr-101");
		course.create(lecturer, studentGroup);
		
		assertTrue(course.isStudentGroupScheduled(studentGroup));
	}
}
