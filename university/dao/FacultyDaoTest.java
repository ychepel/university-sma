package university.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import university.domain.Faculty;

public class FacultyDaoTest {

	@Test
	public void testCreateFaculty() {
		FacultyDao facultyDao = new FacultyDao();

		try {
			facultyDao.dropFaculty(5);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		
		fail("Not yet implemented");
	}

}
