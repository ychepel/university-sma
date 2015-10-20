package university.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import university.domain.Faculty;

public class FacultyDao extends DaoAbstract {

	@Override
	protected String getSelectAllQuery() {
		return "SELECT * FROM FACULTY";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> Set<T> parseAllResultSet(ResultSet resultSet) throws SQLException {
		Set<Faculty> faculties = new HashSet<>(); 
		while(resultSet.next()) {
			Faculty faculty = new Faculty(resultSet.getString("NAME"));
			faculty.setId(resultSet.getInt("ID"));
				
			faculties.add(faculty);
				
		}
		return (Set<T>) faculties;
	}

	@Override
	protected String getSelectByIdQuery() {
		return "SELECT * FROM FACULTY WHERE ID=?";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T parseOneResultSet(ResultSet resultSet) throws SQLException {
		resultSet.next();
		Faculty faculty = null; 
		faculty = new Faculty(resultSet.getString("NAME"));
		Integer id = resultSet.getInt("ID");
		faculty.setId(id);
		
		return (T) faculty;
	}

	@Override
	protected <T> String getInsertQuery(T preparedElement) {
		Faculty preparedFaculty = (Faculty) preparedElement;
		String name = preparedFaculty.getName();
		String query = "INSERT INTO FACULTY (NAME) VALUES ('" + name + "')"; 
		
		return query;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T makeElement(T preparedElement, ResultSet resultSet) throws SQLException {
		resultSet.next();
		Integer id = resultSet.getInt(1);
		
		Faculty preparedFaculty = (Faculty) preparedElement;
		String name = preparedFaculty.getName();
		
		Faculty faculty = new Faculty(name);
		faculty.setId(id);
		
		return (T) faculty;
	}

	@Override
	protected String getDeleteQuery() {
		return "DELETE FROM FACULTY WHERE ID=?";
	}
	
}
