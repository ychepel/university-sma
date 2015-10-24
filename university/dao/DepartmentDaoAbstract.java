package university.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import university.domain.Department;

public class DepartmentDaoAbstract extends DaoAbstract {

	@Override
	protected String getSelectAllQuery() {
		return "SELECT * FROM DEPARTMENT";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> Set<T> parseAllResultSet(ResultSet resultSet) throws SQLException {
		Set<Department> set = new HashSet<>(); 
		while(resultSet.next()) {
			Department element = new Department(resultSet.getString("NAME"));
			element.setId(resultSet.getInt("ID"));
				
			set.add(element);
				
		}
		return (Set<T>) set;
	}

	@Override
	protected String getSelectByIdQuery() {
		return "SELECT * FROM DEPARTMENT WHERE ID=?";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T parseOneResultSet(ResultSet resultSet) throws SQLException {
		resultSet.next();
		Department result = null; 
		result = new Department(resultSet.getString("NAME"));
		Integer id = resultSet.getInt("ID");
		result.setId(id);
		
		return (T) result;
	}

	@Override
	protected <T> String getInsertQuery(T preparedElement) {
		Department preparedFaculty = (Department) preparedElement;
		String name = preparedFaculty.getName();
		String query = "INSERT INTO DEPARTMENT (NAME) VALUES ('" + name + "')"; 
		
		return query;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <T> T makeElement(T preparedElement, ResultSet resultSet) throws SQLException {
		resultSet.next();
		Integer id = resultSet.getInt(1);
		
		Department preparedFaculty = (Department) preparedElement;
		String name = preparedFaculty.getName();
		
		Department result = new Department(name);
		result.setId(id);
		
		return (T) result;
	}

	@Override
	protected String getDeleteQuery() {
		return "DELETE FROM DEPARTMENT WHERE ID=?";
	}
	
}
