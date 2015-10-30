package university.domain.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import university.dao.DaoException;
import university.domain.*;

public class CourseTest {
	private University university;
	private Faculty faculty;
	private Department department;
	private Lecturer lecturer1;
	private Lecturer lecturer2;
	private Course course;
	private StudentGroup studentGroup101;
	private StudentGroup studentGroup102;
	
	@Before
	public void fulfillDB() throws DomainException {
		university = new University("Tets.university");
		faculty = university.createFaculty("Test.faculty");
		lecturer1 = new Lecturer();
		lecturer2 = new Lecturer();
		course = new Course("Test.courseA");
		studentGroup101 = new StudentGroup("Test.group-101");
		studentGroup102 = new StudentGroup("Test.group-102");
		
		department = faculty.createDepartment("Test.Dep");
		department.addLecturer(lecturer1);
		department.addLecturer(lecturer2);
		department.addCourse(course);
		
	}
	
	@After
	public void clearDB() throws DaoException, DomainException {
		department.excludeStudentGroup(studentGroup101);
		department.excludeStudentGroup(studentGroup102);
		department.removeCourse(course);
		department.removeLecturer(lecturer1);
		department.removeLecturer(lecturer2);
		faculty.removeDepartment(department);
		university.removeFaculty(faculty);
	}

	@Test
	public void testCreateGroupSchedule() throws DomainException, DaoException {
		course.createCourseSchedule(lecturer1, studentGroup101);
		course.createCourseSchedule(lecturer1, studentGroup102);
		course.createCourseSchedule(lecturer2, studentGroup102);
		assertEquals(2, course.getCourseSchedules().size());
	}

	@Test
	public void testIsStudentGroupScheduled() throws DomainException, DaoException {
		assertTrue(course.isStudentGroupScheduled(studentGroup102));
	}
}
