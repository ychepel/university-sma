package university.domain.tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import university.dao.DaoException;
import university.domain.*;


public class StudentGroupTest {
	
	private SampleData  sampleData = AllTests.sampleData;
	
	@Test
	public void testGetStudentsOnCourse() throws DomainException {
		Student sampleStudent = sampleData.getTestStudent();
		Set<Long> expectedResult = new HashSet<>();
		expectedResult.add(sampleStudent.getStudentId());
		
		StudentGroup studentGroup = sampleStudent.getStudentGroup();
		Course sampleCourse = sampleData.getTestCourse2();
		Set<Student> resultStudents = studentGroup.getStudentsOnCourse(sampleCourse);;
		Set<Long> result = new HashSet<>();
		for(Student student : resultStudents) {
			Long resutStudentId = student.getStudentId();
			result.add(resutStudentId);
		}
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testGetGrade() throws DomainException, DaoException {
		Faculty sampleFaculty = sampleData.getTestFaculty();
		Student student = new Student() {
			@Override
			protected Calendar getCalendar() {
				return new GregorianCalendar(2015, Calendar.NOVEMBER, 1);
			}
		};
		student.setCompletionDate(new GregorianCalendar(2016, Calendar.MAY, 31));
		sampleFaculty.addStudent(student);
		StudentGroup studentGroup = student.getStudentGroup();
		
		assertEquals(Integer.valueOf(5) , studentGroup.getGrade());
		
		sampleFaculty.removeStudent(student);
	}
	
	@Test
	public void testGetSuccessRating() throws DomainException {
		Student sampleStudent = sampleData.getTestStudent();
		Course sampleCourse = sampleData.getTestCourse1();
		Long sampleStudentId = sampleStudent.getStudentId();
		Integer mark = sampleStudent.getCourseMark(sampleCourse);
		
		Map<Long, Integer> expectedResult = new LinkedHashMap<>();
		expectedResult.put(sampleStudentId, mark);

		StudentGroup studentGroup = sampleStudent.getStudentGroup();
		Map<Student, Integer> resultSuccessRating = studentGroup.getSuccessRating(sampleCourse);
		Map<Long, Integer> result = new LinkedHashMap<>();
		for(Map.Entry<Student, Integer> entry : resultSuccessRating.entrySet()) {
			result.put(entry.getKey().getStudentId(), entry.getValue());
		}
		assertEquals(expectedResult, result);
		
	}

}
