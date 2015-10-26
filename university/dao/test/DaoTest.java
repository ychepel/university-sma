package university.dao.test;


import java.util.Calendar;

import university.dao.*;
import university.domain.*;

public class DaoTest {

	public static void main (String[] args) throws DaoException {
		CourseDao courseDao = new CourseDao();
		FacultyDao facultyDao = new FacultyDao();
		DepartmentDao departmentDao = new DepartmentDao();
		CourseScheduleDao courseScheduleDao = new CourseScheduleDao();
		StudentDao studentDao = new StudentDao();
		
		Faculty faculty = facultyDao.getFacultyById(1);
		System.out.println(faculty.getName().trim() + ":");
		for(Department department : faculty.getDepartments()) {
			System.out.println("   D# " + department.getName().trim() + ":");
			for(Lecturer lecturer : department.getLecturers()) {
				System.out.println("      L# " + lecturer.getFullName() 
						+ "(phone " + lecturer.getAddress().getPhone().trim() + ")");
			}
			for(Course course : department.getCourses()) {
				System.out.println("      C# " + course.getName().trim() + " - "  + course.getGrade() + ":");
				for(CourseSchedule courseSchedule : course.getCourseSchedules()) {
					System.out.println("         L# " + courseSchedule.getLecturer().getFullName() 
							+ "(" + courseSchedule.getLecturer().getAddress().getStreet().trim() + " Street)");
					for(Calendar calendar : courseSchedule.getTimetables()) {
						System.out.println("         T# " + calendar.getTime());
					}
					for(StudentGroup studentGroup : courseSchedule.getStudentGroups()) {
						System.out.println("         G# " + studentGroup.getName().trim() + ":");
						studentGroup.setStudents(studentDao.getStudentsByGroup(studentGroup));
						for(Student student : studentGroup.getStudents()) {
							System.out.println("             S# " + student.getFullName() 
								+ " (" + student.getBirthDate() + ", grade " + student.getGrade() 
								+ ", " + student.getAddress().getCity().trim() 
								+ ", average mark - " + student.getAverageMark() + ")");
						}
					}
				}
			}
		}
		for(StudentGroup studentGroup : faculty.getStudentGroups()) {
			System.out.println("   G# " + studentGroup.getName().trim() + ":");
			studentGroup.setStudents(studentDao.getStudentsByGroup(studentGroup));
			for(Student student : studentGroup.getStudents()) {
				System.out.println("       S# " + student.getFullName() 
					+ " (" + student.getBirthDate() + ", grade " + student.getGrade() 
					+ ", " + student.getAddress().getCity().trim() 
					+ ", average mark - " + student.getAverageMark() + ")");
			}
		}
	}
}
