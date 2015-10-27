package university.domain.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CourseTest.class,
		DepartmentTest.class, FacultyTest.class, StudentGroupTest.class,
		StudentTest.class, UniversityTest.class })
public class AllTests {

}
