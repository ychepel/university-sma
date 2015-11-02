package university.domain.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import university.dao.DaoException;
import university.domain.*;

public class CourseTest {
	private SampleData  sampleData = AllTests.sampleData;

	@Test
	public void testIsStudentGroupScheduled() throws DomainException, DaoException {
		Course course = sampleData.getTestCourse1();
		Student student = sampleData.getTestStudent();
		StudentGroup studentGroup = student.getStudentGroup();
		assertTrue(course.isStudentGroupScheduled(studentGroup));
	}
}
