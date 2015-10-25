package university.dao.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import university.dao.DaoException;
import university.dao.DepartmentDao;
import university.domain.Department;
import university.domain.Faculty;

public class DepartmentDaoTest {

	@Test
	public void testGetDepartments() {
		DepartmentDao departmentDao = new DepartmentDao();
		Set<Department> departments = new HashSet<>();
		try {
			departmentDao.dropDepartmentById(2);;
			departments = departmentDao.getDepartments(new Faculty(""));
		}
		catch (DaoException e) {
			e.printStackTrace();
		}
		for (Department department : departments) 
			System.out.println(department.getName());
		fail("Not yet implemented");
	}

}
