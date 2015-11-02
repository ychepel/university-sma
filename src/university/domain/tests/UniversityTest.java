package university.domain.tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import university.domain.DomainException;
import university.domain.Lecturer;
import university.domain.University;

public class UniversityTest {
	
	private SampleData  sampleData = AllTests.sampleData;
	
	@Test
	public void testGetLabourHour() throws DomainException {
		University sampleUniversity = sampleData.getTestUniversity();
		Lecturer sampleLecturer = sampleData.getTestLecturer();
		Calendar calendar = new GregorianCalendar(2015, Calendar.OCTOBER, 1);
		
		Integer expectedResult = 3;
		Integer result = sampleUniversity.getLabourHour(sampleLecturer, calendar);
		assertEquals(expectedResult, result);
	}

}
