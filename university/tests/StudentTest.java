package university.tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import university.Course;
import university.Student;

public class StudentTest {

	@Test
	public void testGetAverageMark1() {
		Student student = new Student();
		student.addMark(new Course(), 12);
		student.addMark(new Course(), 7);
		Integer result = student.getAverageMark();
		assertEquals(Integer.valueOf(9) , result);
	}
	
	@Test
	public void testGetAverageMark2() {
		Student student = new Student();
		student.addMark(new Course(), 0);
		Integer result = student.getAverageMark();
		assertEquals(Integer.valueOf(0) , result);
	}
	
	@Test
	public void testGetAverageMark3() {
		Student student = new Student();
		Integer result = student.getAverageMark();
		assertEquals(Integer.valueOf(0) , result);
	}

	@Test
	public void testGetAverageMark4() {
		Student student = new Student();
		student.addMark(new Course(), -1);
		student.addMark(new Course(), 0);
		Integer result = student.getAverageMark();
		assertEquals(Integer.valueOf(0) , result);
	}
	
	@Test
	public void testGetAverageMark5() {
		Student student = new Student();
		student.addMark(new Course(), -1);
		student.addMark(new Course(), -1);
		Integer result = student.getAverageMark();
		assertEquals(Integer.valueOf(-1) , result);
	}
	
	@Test
	public void testGetGrade1() {
		Student student = new Student() {
			@Override
			protected Calendar getCalendar() {
				return new GregorianCalendar(2014, Calendar.SEPTEMBER, 1);
			}
		};
		student.setCompletionDate(new GregorianCalendar(2016, Calendar.MAY, 31));
		Integer result = student.getGrade();
		assertEquals(Integer.valueOf(4) , result);
	}
	
	@Test
	public void testGetGrade2() {
		Student student = new Student() {
			@Override
			protected Calendar getCalendar() {
				return new GregorianCalendar(2015, Calendar.NOVEMBER, 1);
			}
		};
		student.setCompletionDate(new GregorianCalendar(2016, Calendar.MAY, 31));
		Integer result = student.getGrade();
		assertEquals(Integer.valueOf(5) , result);
	}
	
	@Test
	public void testGetGrade3() {
		Student student = new Student() {
			@Override
			protected Calendar getCalendar() {
				return new GregorianCalendar(2015, Calendar.NOVEMBER, 1);
			}
		};
		student.setCompletionDate(new GregorianCalendar(2014, Calendar.MAY, 31));
		Integer result = student.getGrade();
		assertEquals(Integer.valueOf(0) , result);
	}
	
}
