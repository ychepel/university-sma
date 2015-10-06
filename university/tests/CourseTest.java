package university.tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import university.*;

public class CourseTest {

	@Test
	public void testAddGroupSchedule() {
		Lecturer lecturer1 = new Lecturer();
		Lecturer lecturer2 = new Lecturer();
		Course course = new Course("A");
		StudentGroup studentGroup101 = new StudentGroup("gr-101");
		StudentGroup studentGroup102 = new StudentGroup("gr-102");
		
		assertEquals(new HashSet<>(), course.getCourseSchedules());
		
		course.addCourseSchedule(lecturer1, studentGroup101);
		assertEquals(1, course.getCourseSchedules().size());
		
		course.addCourseSchedule(lecturer1, studentGroup102);
		assertEquals(1, course.getCourseSchedules().size());
		
		course.addCourseSchedule(lecturer2, studentGroup102);
		assertEquals(2, course.getCourseSchedules().size());
	}

	@Test
	public void testIsStudentGroupScheduled() {
		Course course = new Course("A");
		Lecturer lecturer = new Lecturer();
		StudentGroup studentGroup = new StudentGroup("gr-101");
		course.addCourseSchedule(lecturer, studentGroup);
		
		assertTrue(course.isStudentGroupScheduled(studentGroup));
	}

	@Test
	public void testExcludeStudent() {
		Student student1 = new Student();
		Student student2 = new Student();
		StudentGroup studentGroup = new StudentGroup("Gr-101");
		student1.setStudentGroup(studentGroup);
		student2.setStudentGroup(studentGroup);
		studentGroup.addToGroup(student1);
		studentGroup.addToGroup(student2);

		Course course = new Course("A");
		Lecturer lecturer = new Lecturer();
		course.addCourseSchedule(lecturer, studentGroup);
		student1.addMark(course, -1);
		student2.addMark(course, -1);
		assertEquals(1, course.getCourseSchedules().size());
		
		course.excludeStudent(student1);
		assertEquals(1, course.getCourseSchedules().size());
		
		course.excludeStudent(student2);
		assertTrue(course.getCourseSchedules().isEmpty());
	}

}
