package university.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import university.*;

public class CourseScheduleTest {

	@Test
	public void testExcludeStudent() {
		Student student1 = new Student();
		Student student2 = new Student();
		StudentGroup studentGroup = new StudentGroup("Gr-101");
		student1.setStudentGroup(studentGroup);
		student2.setStudentGroup(studentGroup);

		Course course = new Course("A");
		Lecturer lecturer = new Lecturer();
		CourseSchedule courseSchedule = new CourseSchedule(course, lecturer);
		courseSchedule.add(studentGroup);

		student1.addMark(course, -1);
		student2.addMark(course, -1);

		courseSchedule.excludeStudent(student1);
		assertEquals(Integer.valueOf(0), student1.getMark(course));
		
		courseSchedule.excludeStudent(student2);
		assertTrue(courseSchedule.getStudentGroups().isEmpty());
	}

}
