package university.domain.tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import university.dao.DaoException;
import university.domain.Course;
import university.domain.DomainException;
import university.domain.Faculty;
import university.domain.Student;

public class StudentTest {
	
	private SampleData  sampleData = AllTests.sampleData;
	
	@Test
	public void testGetAverageMark1() throws DomainException {
		Student sampleStudent = sampleData.getTestStudent();
		Course sampleCourse1 = sampleData.getTestCourse1();
		Course sampleCourse2 = sampleData.getTestCourse2();
		Course sampleCourse3 = sampleData.getTestCourse3();
		sampleStudent.addMark(sampleCourse1, 22);
		sampleStudent.addMark(sampleCourse2, 10);
		sampleStudent.addMark(sampleCourse3, 13);
		
		Integer result = sampleStudent.getAverageMark();
		assertEquals(Integer.valueOf(15) , result);
		
		sampleStudent.addMark(sampleCourse1, 22);
		sampleStudent.addMark(sampleCourse2, -1);
		sampleStudent.addMark(sampleCourse3, 0);
	}
	
	@Test
	public void testGetAverageMark2() throws DomainException {
		Student sampleStudent = sampleData.getTestStudent();
		Course sampleCourse1 = sampleData.getTestCourse1();
		Course sampleCourse2 = sampleData.getTestCourse2();
		Course sampleCourse3 = sampleData.getTestCourse3();
		sampleStudent.addMark(sampleCourse1, -1);
		sampleStudent.addMark(sampleCourse2, 0);
		sampleStudent.addMark(sampleCourse3, 0);
		
		Integer result = sampleStudent.getAverageMark();
		assertEquals(Integer.valueOf(0) , result);
		
		sampleStudent.addMark(sampleCourse1, 22);
		sampleStudent.addMark(sampleCourse2, -1);
		sampleStudent.addMark(sampleCourse3, 0);
	}
	
	@Test
	public void testGetAverageMark3() throws DomainException {
		Student sampleStudent = sampleData.getTestStudent();
		Course sampleCourse1 = sampleData.getTestCourse1();
		Course sampleCourse2 = sampleData.getTestCourse2();
		Course sampleCourse3 = sampleData.getTestCourse3();
		sampleStudent.addMark(sampleCourse1, -1);
		sampleStudent.addMark(sampleCourse2, -1);
		sampleStudent.addMark(sampleCourse3, -1);
		
		Integer result = sampleStudent.getAverageMark();
		assertEquals(Integer.valueOf(-1) , result);
		
		sampleStudent.addMark(sampleCourse1, 22);
		sampleStudent.addMark(sampleCourse2, -1);
		sampleStudent.addMark(sampleCourse3, 0);
	}
	
	@Test
	public void testGetGrade() throws DaoException, DomainException {
		Faculty sampleFaculty = sampleData.getTestFaculty();
		Student student = new Student() {
			@Override
			protected Calendar getCalendar() {
				return new GregorianCalendar(2014, Calendar.SEPTEMBER, 1);
			}
		};
		student.setCompletionDate(new GregorianCalendar(2016, Calendar.MAY, 31));
		sampleFaculty.addStudent(student);
		
		Integer result = student.getGrade();
		assertEquals(Integer.valueOf(4) , result);
		
		sampleFaculty.removeStudent(student);
	}
	
	@Test
	public void testSetEntranceDate() throws DomainException, DaoException {
		Faculty sampleFaculty = sampleData.getTestFaculty();
		Student student = new Student();
		student.setEntranceDate(new GregorianCalendar(2015, Calendar.SEPTEMBER, 1));
		sampleFaculty.addStudent(student);
		Calendar expectedResult = new GregorianCalendar(2020, Calendar.MAY, 31);
		assertEquals(expectedResult , student.getCompletionDate());
		
		sampleFaculty.removeStudent(student);
	}
	
}
