package university.domain.tests;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import university.dao.DaoException;
import university.domain.*;

public class DepartmentTest {
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
	public void testEnrollStudent() throws DomainException {
		Course course3 = sampleData.getTestCourse3();
		Course course4 = sampleData.getTestCourse4();
		Student student = sampleData.getTestStudent();
		Department department = sampleData.getTestDepartment();
		
		assertTrue(department.enrollStudent(student, course3));
		assertFalse(department.enrollStudent(student, course4));
	}

}
