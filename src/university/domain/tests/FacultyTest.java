package university.domain.tests;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import university.dao.DaoException;
import university.domain.Course;
import university.domain.CourseSchedule;
import university.domain.DomainException;
import university.domain.Faculty;
import university.domain.Lecturer;
import university.domain.Student;
import university.domain.StudentGroup;

public class FacultyTest {
	private SampleData  sampleData = AllTests.sampleData;
	
	@Test
	public void testGetGroupsQuantityOnGrade() throws DaoException, DomainException {
		Faculty sampleFaculty = sampleData.getTestFaculty();
		Student sampleStudent = sampleData.getTestStudent();
		
		for(int i = 0; i <= sampleFaculty.getMaxQuantityInGroupParameter(); i++) {
			Student student = new Student() {
				@Override
				protected Calendar getCalendar() {
					return new GregorianCalendar(2015, Calendar.NOVEMBER, 1);
				}
			};
			student.setEntranceDate(new GregorianCalendar(2015, Calendar.SEPTEMBER, 1));
			sampleFaculty.addStudent(student);
		}

		Integer expectedQuantity = 2;
		Integer testedGrade = 1;
		assertEquals(expectedQuantity, sampleFaculty.getGroupsQuantityOnGrade(testedGrade));
		
		for(StudentGroup studentGroup : sampleFaculty.getStudentGroups()) {
			for(Student student : studentGroup.getStudents()) {
				if(!sampleStudent.getStudentId().equals(student.getStudentId()))
					sampleFaculty.removeStudent(student);
			}
		}
	}

	@Test
	public void testGetUnderachievementStudent() throws DomainException {
		Faculty sampleFaculty = sampleData.getTestFaculty();
		Student sampleStudent = sampleData.getTestStudent();
		Integer underachievmentLevel = sampleFaculty.getUnderachievmentLevelParameter();
		Map<Course, Integer> sampleMarks = sampleStudent.getMarks();
		for(Map.Entry<Course, Integer> entry : sampleMarks.entrySet()) {
			Course course = entry.getKey();
			sampleStudent.addMark(course, underachievmentLevel-1);
		}
		Set<Long> expectedResult = new HashSet<>();
		expectedResult.add(sampleStudent.getStudentId());
		
		Set<Student> students = sampleFaculty.getUnderachievementStudent();
		Set<Long> result = new HashSet<>();
		for(Student student : students) {
			result.add(student.getStudentId());
		}
		
		assertEquals(expectedResult, result);
		
		Course sampleCourse1 = sampleData.getTestCourse1();
		Course sampleCourse2 = sampleData.getTestCourse2();
		Course sampleCourse3 = sampleData.getTestCourse3();
		sampleStudent.addMark(sampleCourse1, 22);
		sampleStudent.addMark(sampleCourse2, -1);
		sampleStudent.addMark(sampleCourse3, 0);
	}
	
	@Test
	public void testGetStudentSchedule() throws DomainException {
		Faculty sampleFaculty = sampleData.getTestFaculty();
		Student sampleStudent = sampleData.getTestStudent();
		CourseSchedule sampleCourseSchedule = sampleData.getTestCourseSchedule2();
		Integer expectedSampleCourseScheduleId = sampleCourseSchedule.getId();
		Set<Integer> expectedResult = new HashSet<>();
		expectedResult.add(expectedSampleCourseScheduleId);
		
		Set<CourseSchedule> resultCourseSchedules = sampleFaculty.getStudentSchedule(sampleStudent);
		Set<Integer> result = new HashSet<>();
		for(CourseSchedule resultCourseSchedule : resultCourseSchedules) {
			Integer courseScheduleId = resultCourseSchedule.getId();
			result.add(courseScheduleId);
		}
		
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void testLecturerSchedule() throws DomainException {
		Faculty sampleFaculty = sampleData.getTestFaculty();
		Lecturer sampleLecturer = sampleData.getTestLecturer();
		CourseSchedule sampleCourseSchedule1 = sampleData.getTestCourseSchedule1();
		CourseSchedule sampleCourseSchedule2 = sampleData.getTestCourseSchedule2();
		CourseSchedule sampleCourseSchedule3 = sampleData.getTestCourseSchedule3();
		Integer expectedSampleCourseSchedule1Id = sampleCourseSchedule1.getId();
		Integer expectedSampleCourseSchedule2Id = sampleCourseSchedule2.getId();
		Integer expectedSampleCourseSchedule3Id = sampleCourseSchedule3.getId();
		Set<Integer> expectedResult = new HashSet<>();
		expectedResult.add(expectedSampleCourseSchedule1Id);
		expectedResult.add(expectedSampleCourseSchedule2Id);
		expectedResult.add(expectedSampleCourseSchedule3Id);
		
		Set<CourseSchedule> resultCourseSchedules = sampleFaculty.getLecturerSchedule(sampleLecturer);
		Set<Integer> result = new HashSet<>();
		for(CourseSchedule resultCourseSchedule : resultCourseSchedules) {
			Integer courseScheduleId = resultCourseSchedule.getId();
			result.add(courseScheduleId);
		}
		
		assertEquals(expectedResult, result);
	}

}
