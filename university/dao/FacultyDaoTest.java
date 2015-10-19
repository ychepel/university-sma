package university.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import university.domain.Faculty;

public class FacultyDaoTest {

	@Test
	public void testCreateFaculty() {
		FacultyDao facultyDao = new FacultyDao();
		Faculty faculty = null;
		try {
			faculty = facultyDao.createFaculty("Test");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(faculty.getId());
		
		fail("Not yet implemented");
	}

}
