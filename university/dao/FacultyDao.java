package university.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import university.domain.Faculty;

public class FacultyDao extends DaoAbstract {

	@Override
	protected String getAllSQL() {
		return "select * from faculty";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> Set<T> parseAllResultSet(ResultSet resultSet) throws SQLException {
		Set<Faculty> faculties = new HashSet<>(); 
		while(resultSet.next()) {
			Faculty faculty = new Faculty(resultSet.getString("name"));
			faculty.setId(resultSet.getInt("id"));
				
			faculties.add(faculty);
				
		}
		return (Set<T>) faculties;
	}

	@Override
	protected String getElementByIdSQL() {
		return "select * from faculty where id=?";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T parseOneResultSet(ResultSet resultSet) throws SQLException {
		resultSet.next();
		Faculty faculty = null; 
		faculty = new Faculty(resultSet.getString("name"));
		Integer id = resultSet.getInt("id");
		faculty.setId(id);
		
		return (T) faculty;
	}

	@Override
	protected <T> String getInsertElementSQL(T preparedElement) {
		Faculty preparedFaculty = (Faculty) preparedElement;
		String name = preparedFaculty.getName();
		String query = "insert into faculty (name) values ('" + name + "')"; 
		
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
	protected String getDeleteSQL() {
		return "delete from faculty where id=?";
	}
	
}
