package university.domain.tests;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import university.dao.DaoException;
import university.domain.*;

public class CourseTest {
	private static SampleData sampleData = new SampleData();
	
	@BeforeClass
	public static void fulfillDB() throws DomainException, DaoException {
		sampleData.fillDBWithSampleData();
	}
	
	@AfterClass
	public static void clearDB() throws DaoException, DomainException {
		sampleData.clearDBFromSampleData();
	}

	@Test
	public void testIsStudentGroupScheduled() throws DomainException, DaoException {
		Course course = sampleData.getTestCourse1();
		Student student = sampleData.getTestStudent();
		StudentGroup studentGroup = student.getStudentGroup();
		assertTrue(course.isStudentGroupScheduled(studentGroup));
	}
}
