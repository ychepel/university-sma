package university.domain.tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import university.domain.*;

public class FacultyTest {
	
	private Faculty faculty = new Faculty("faculty F");
	
	private void fillStudentGroup() {
		Student student = new Student();
		for(int i = 0; i <= faculty.getMaxQuantityInGroupParameter(); i++) {
			student = new Student() {
				@Override
				protected Calendar getCalendar() {
					return new GregorianCalendar(2015, Calendar.NOVEMBER, 1);
				}
			};
			student.setCompletionDate(new GregorianCalendar(2018, Calendar.MAY, 31));
			faculty.addStudent(student);
		}
	}
	
	@Test
	public void testAdd() {
		
		Student student = new Student() {
			@Override
			protected Calendar getCalendar() {
				return new GregorianCalendar(2015, Calendar.NOVEMBER, 1);
			}
		};
		student.setCompletionDate(new GregorianCalendar(2018, Calendar.MAY, 31));
		faculty.addStudent(student);
		
		Set<StudentGroup> result = faculty.getStudentGroups();
		StudentGroup studentGroup = result.iterator().next();
		assertEquals("G3-1", studentGroup.getName());
		
		assertEquals(1, result.size());
		fillStudentGroup();
		result = faculty.getStudentGroups();
		assertEquals(2, result.size());
	}

	@Test
	public void testGetGroupsQuantityOnGrade() {
		fillStudentGroup();
		Integer expectedQuantity = 2;
		Integer testedGrade = 3;
		assertEquals(expectedQuantity, faculty.getGroupsQuantityOnGrade(testedGrade));
	}

	@Test
	public void testGetUnderachievementStudent() {
		Integer UnderacievmentLevel = faculty.getUnderachievmentLevelParameter();
		
		Student student1 = new Student();
		student1.addMark(new Course("A"), UnderacievmentLevel-1);
		student1.addMark(new Course("B"), UnderacievmentLevel-1);
		student1.setCompletionDate(new GregorianCalendar(2018, Calendar.MAY, 31));
		faculty.addStudent(student1);
		
		Student student2 = new Student();
		student2.addMark(new Course("A"), UnderacievmentLevel+1);
		student2.addMark(new Course("B"), UnderacievmentLevel+1);
		student2.setCompletionDate(new GregorianCalendar(2018, Calendar.MAY, 31));
		faculty.addStudent(student2);
		
		Student student3 = new Student();
		student3.addMark(new Course("A"), UnderacievmentLevel-1);
		student3.addMark(new Course("B"), 0);
		student3.setCompletionDate(new GregorianCalendar(2018, Calendar.MAY, 31));
		faculty.addStudent(student3);
		
		Set<Student> expectedResult = new HashSet<>();
		expectedResult.add(student1);
		expectedResult.add(student3);
		
		assertEquals(expectedResult, faculty.getUnderachievementStudent());
	}

}
