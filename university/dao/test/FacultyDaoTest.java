package university.dao.test;

import static org.junit.Assert.*;

import org.junit.Test;

import university.dao.DaoException;
import university.dao.FacultyDao;
import university.domain.Faculty;

public class FacultyDaoTest {
	@Test
	public void testGetById() {
		FacultyDao facultyDao = new FacultyDao();
		Faculty faculty = null;
			
		try {
			faculty = facultyDao.getFacultyById(1);
		}
		catch(DaoException e) {
			e.printStackTrace();
		}
			
		String name = faculty.getName().trim();
		assertEquals("Faculty of Mathematics", name);
			
	}
}
