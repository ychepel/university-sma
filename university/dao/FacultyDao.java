package university.dao;

import java.util.Set;

import university.Faculty;

public interface FacultyDao {
	public Set<Faculty> get() throws DAOException;
	public Faculty get(Integer id) throws DAOException;
}
