package university.dao;

import static org.junit.Assert.*;
import org.junit.Test;
import university.domain.Faculty;

public class FacultyDaoTest {
	@Test
	public void testGetById() {
		FacultyDao facultyDao = new FacultyDao();
		Faculty faculty = null;
			
		try {
			faculty = facultyDao.getById(1);
		}
		catch(DaoException e) {
		}
			
		String name = faculty.getName().trim();
		assertEquals("Faculty of Mathematics", name);
			
	}
}
