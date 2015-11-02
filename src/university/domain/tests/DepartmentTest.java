package university.domain.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import university.domain.*;

public class DepartmentTest {
	private SampleData  sampleData = AllTests.sampleData;

	@Test
	public void testEnrollStudent() throws DomainException {
		Course course3 = sampleData.getTestCourse3();
		Course course4 = sampleData.getTestCourse4();
		Student student = sampleData.getTestStudent();
		Faculty faculty = sampleData.getTestFaculty();
		
		assertTrue(faculty.enrollStudent(student, course3));
		assertFalse(faculty.enrollStudent(student, course4));
	}

}
