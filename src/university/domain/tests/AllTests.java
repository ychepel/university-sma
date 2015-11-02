package university.domain.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import university.dao.DaoException;
import university.domain.DomainException;

@RunWith(Suite.class)
@SuiteClasses({ 
		CourseTest.class,
		DepartmentTest.class, 
		FacultyTest.class, 
		StudentGroupTest.class,
		StudentTest.class, 
		UniversityTest.class 
	})

public class AllTests {
	public static SampleData sampleData = new SampleData();
	
	@BeforeClass
	public static void fulfillDB() throws DomainException, DaoException {
		sampleData.fillDBWithSampleData();
	}
	
	@AfterClass
	public static void clearDB() throws DaoException, DomainException {
		sampleData.clearDBFromSampleData();
	}
	
}
